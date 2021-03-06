package com.dili.crm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dili.uap.sdk.domain.SystemConfig;
import com.dili.uap.sdk.rpc.SystemConfigRpc;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dili.crm.domain.Address;
import com.dili.crm.domain.City;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.CustomerExtensions;
import com.dili.crm.domain.IcardUserAccount;
import com.dili.crm.domain.IcardUserCard;
import com.dili.crm.domain.dto.IcardUserCardDTO;
import com.dili.crm.domain.toll.TollCustomer;
import com.dili.crm.service.AddressService;
import com.dili.crm.service.CityService;
import com.dili.crm.service.CustomerExtensionsService;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.ICardETLService;
import com.dili.crm.service.IcardUserAccountService;
import com.dili.crm.service.IcardUserCardService;
import com.dili.crm.service.toll.TollCustomerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.SystemConfigUtils;

/**
 * @author wangguofeng
 *
 */
@Component
public class ICardETLServiceImpl implements ICardETLService{
	private static final Logger logger=LoggerFactory.getLogger(ICardETLServiceImpl.class);
	@Autowired private IcardUserAccountService icardUserAccountService;
	@Autowired private IcardUserCardService icardUserCardService;
	@Autowired private CustomerService customerService;
	@Autowired private CustomerExtensionsService customerExtensionsService;
	@Autowired private AddressService addressService;
	@Autowired private TollCustomerService tollCustomerService;
	@Autowired private SystemConfigRpc systemConfigRpc;
	@Autowired private CityService cityService;
	/**
	 * @param customer ????????????????????????crm???????????????
	 * @param customerExtensionsList ???????????????????????????cmr???????????????
	 * @return true????????????,false ????????????
	 */
	@Transactional
	private boolean saveOrUpdate(Customer customer,List<CustomerExtensions> customerExtensionsList,List<Address>address) {
		
		
		if("toll".equals(customer.getSourceSystem())) {
			this.saveOrUpdateTollLatestTime(customer.getCreated());
		}else {
			this.saveOrUpdateSettlementLatestTime(customer.getCreated());
		}
		
		Customer customerQueryCondition=DTOUtils.newDTO(Customer.class);
		customerQueryCondition.setCertificateNumber(customer.getCertificateNumber());
		customerQueryCondition.setCertificateType(customer.getCertificateType());
		customerQueryCondition.setOrganizationType(customer.getOrganizationType());
		//??????????????????crm????????????????????????
		List<Customer>list=this.customerService.list(customerQueryCondition);
		if(list!=null&&list.size()==1) {
			Customer customerItem=list.get(0);
			//??????????????????
			if(customerItem.getSyncTime()==null) {
				customerItem.setSyncTime(customerItem.getCreated());
			}
			
			if(customerItem.getSyncTime()!=null&&customerItem.getSyncTime().before(customer.getSyncTime())) {
				String name=StringUtils.trimToEmpty(customer.getName());
				String phone=StringUtils.trimToEmpty(customer.getPhone());
				String sex=StringUtils.trimToEmpty(customer.getSex());
				if(StringUtils.isNotBlank(name)&&!name.equals(customerItem.getName())) {
					customerItem.setName(name);	
				}
				if(StringUtils.isNotBlank(phone)&&!phone.equals(customerItem.getPhone())) {
					customerItem.setPhone(phone);	
				}
				if(StringUtils.isNotBlank(sex)&&!sex.equals(customerItem.getSex())) {
					customerItem.setSex(sex);	
				}
				
			}
			customerItem.setSyncTime(customer.getSyncTime());
			customer=customerItem;
		}
		logger.info("customer id:{},name:{},certificateNumber:{},system:{}",customer.getId(),customer.getName(),customer.getCertificateNumber(),customer.getSourceSystem());
		this.customerService.saveOrUpdate(customer);
		
		//?????????????????????????????????????????????
		List<CustomerExtensions>insertExtensionList=new ArrayList<>();
		
		List<CustomerExtensions>updateExtensionList=new ArrayList<>();
		logger.info("customerExtensionsList size:{}",customerExtensionsList.size());
		for(CustomerExtensions customerExtensions:customerExtensionsList) {
			CustomerExtensions extensionsQueryCondition=DTOUtils.newDTO(CustomerExtensions.class);
			
			extensionsQueryCondition.setAcctId(customerExtensions.getAcctId());
			extensionsQueryCondition.setSystem(customerExtensions.getSystem());
			extensionsQueryCondition.setAcctType(customerExtensions.getAcctType());
			List<CustomerExtensions>extensions=this.customerExtensionsService.list(extensionsQueryCondition);
			if(extensions==null||extensions.size()==0) {
				customerExtensions.setCustomerId(customer.getId());
				insertExtensionList.add(customerExtensions);
			}else if(extensions.size()==1) {
				CustomerExtensions extensionItem=extensions.get(0);
				if(!StringUtils.trimToEmpty(extensionItem.getNotes()).equals(StringUtils.trimToEmpty(customerExtensions.getNotes()))) {
					extensionItem.setNotes(StringUtils.trimToEmpty(customerExtensions.getNotes()));
					updateExtensionList.add(extensionItem);
				}
			}
		}
		logger.info("insertExtensionList size:{}",insertExtensionList.size());
		if(insertExtensionList.size()>0) {
			this.customerExtensionsService.batchInsert(insertExtensionList);
		}
		logger.info("updateExtensionList size:{}",updateExtensionList.size());
		if(updateExtensionList.size()>0) {
			this.customerExtensionsService.batchUpdate(updateExtensionList);
		}
		
		Address defaultAddrCondtion=DTOUtils.newDTO(Address.class);
		defaultAddrCondtion.setIsDefault(1);
		defaultAddrCondtion.setCustomerId(customer.getId());
		boolean hasDefaultAddr=this.addressService.listByExample(defaultAddrCondtion).size()>0;
		
		//?????????????????????????????????????????????
		List<Address>addrList=new ArrayList<>();
		for(Address addr:address) {
			Address addrCondtion=DTOUtils.newDTO(Address.class);
			
			addrCondtion.setAddress(addr.getAddress());
			addrCondtion.setCustomerId(customer.getId());
//					customerExtensions.setSystem(customerExtensions.getSystem());
//					
			List<Address>existedAddress=this.addressService.listByExample(addrCondtion);
			if(existedAddress==null||existedAddress.size()==0) {
				addr.setCustomerId(customer.getId());
				addr.setIsDefault(0);
				//????????????????????????????????????,?????????????????????????????????
				if(!hasDefaultAddr) {
					if(StringUtils.isNotBlank(addr.getCityId())) {
						addr.setIsDefault(1);
						hasDefaultAddr=true;
					}
				}
				
				addrList.add(addr);
	
			}

		}

		if(addrList.size()>0) {
			this.addressService.batchInsert(addrList);
		}
		
		return true;
	}
	
	private String formatLatestTime(Date date) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	private Date parseLatestTime(String str) throws ParseException {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.parse(str);
	}
	private void saveOrUpdateTollLatestTime(Date date) {
		if(date==null) {
			return;
		}
		SystemConfig systemConfig = DTOUtils.newDTO(SystemConfig.class);
		systemConfig.setCode("toll_latest_time");
		BaseOutput<List<SystemConfig>> list = this.systemConfigRpc.list(systemConfig);
		if(list.isSuccess() && list.getData()!=null && list.getData().size()>=1) {
			systemConfig=list.getData().get(0);
		}else {
			systemConfig.setName("????????????????????????????????????????????????");
			systemConfig.setDescription("???????????????");
		}
		systemConfig.setValue(this.formatLatestTime(date));
		this.systemConfigRpc.saveOrUpdate(systemConfig);
	}
	private void saveOrUpdateSettlementLatestTime(Date date) {
		if(date==null) {
			return;
		}
		SystemConfig systemConfig=DTOUtils.newDTO(SystemConfig.class);
		systemConfig.setCode("settlement_latest_time");
		BaseOutput<List<SystemConfig>> list = this.systemConfigRpc.list(systemConfig);
		if(list.isSuccess() && list.getData()!=null && list.getData().size() >= 1) {
			systemConfig = list.getData().get(0);
		}else {
			systemConfig.setName("??????????????????????????????????????????????????????");
			systemConfig.setDescription("???????????????");
		}
		systemConfig.setValue(this.formatLatestTime(date));
		this.systemConfigRpc.saveOrUpdate(systemConfig);
	}
	
	/**
	 * ????????????????????????????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????
	 */
	private Date findTollLatestTime() {
		SystemConfig condtion=DTOUtils.newDTO(SystemConfig.class);
		condtion.setCode("toll_latest_time");
		BaseOutput<List<SystemConfig>> list = this.systemConfigRpc.list(condtion);
		if(list.isSuccess() && list.getData()!=null && list.getData().size() >= 1) {
			String value=list.getData().get(0).getValue();
			try {
				Date date=this.parseLatestTime(value);
				return date;
			}catch(Exception e) {
				return null;
			}
		}
		return null;
	}
	
	
	/**
	 * ??????????????????????????????????????????????????????????????????????????????????????????
	 * @return ??????????????????????????????
	 */
	private Date findSettlementLatestTime() {
		SystemConfig condtion=DTOUtils.newDTO(SystemConfig.class);
		condtion.setCode("settlement_latest_time");
		BaseOutput<List<SystemConfig>> list = this.systemConfigRpc.list(condtion);
		if(list.isSuccess() && list.getData()!=null && list.getData().size() >= 1) {
			String value=list.getData().get(0).getValue();
			try {
				Date date=this.parseLatestTime(value);
				return date;
			}catch(Exception e) {
				return null;
			}
			
		}
		return null;
	}
	
	@Override
	public boolean transIncrementData(Customer latestCustomer, int batchSize) {
		if(this.transICardData(batchSize)||this.transTollData(batchSize)) {
			return true;
		}
		return false;
	}
	
	/** ???????????????????????????CRM
	 * @param batchSize
	 * @return
	 */
	private boolean transTollData(int batchSize) {
		
		//????????????????????????????????????
		TollCustomer example=DTOUtils.newDTO(TollCustomer.class);
				example.setPage(1);
				example.setRows(batchSize);
				example.setSort("created");
				example.setOrder("ASC");
				example.setYn(1);
				Date latestTime=this.findTollLatestTime();
				if(latestTime!=null) {
					example.setCreated(latestTime);
				}
				//???????????????????????????
				BasePage<TollCustomer>page=this.tollCustomerService.listPageByExample(example);
				List<TollCustomer>data=page.getDatas();
				if(data!=null&&data.size()>0) {
					
					for(TollCustomer tollCustomer:data) {
						//????????????????????????crm????????????
						Customer customer=this.transTollCustomerAsCustomer(tollCustomer);
						if(customer==null) {
							this.saveOrUpdateTollLatestTime(tollCustomer.getCreated());
							continue;
						}
						
						List<Address>address=this.transTollCustomerAsAddress(tollCustomer);
						
						//??????????????????
						List<CustomerExtensions> customerExtensionsList=this.transTollCustomerAsCustomerExtensions(tollCustomer);
						//???????????????crm
						this.saveOrUpdate(customer, customerExtensionsList,address);
					}
					return true;
				}
				return false;
	}
	
	/** ?????????????????????????????????CRM
	 * @param batchSize
	 * @return
	 */
	private boolean transICardData(int batchSize) {
		
		//??????????????????????????????????????????
				IcardUserAccount example=DTOUtils.newDTO(IcardUserAccount.class);
				example.setPage(1);
				example.setRows(batchSize);
				example.setSort("created_time");
				example.setOrder("ASC");
				Date latestTime=this.findSettlementLatestTime();
				if(latestTime!=null) {
					example.setCreatedTime(latestTime);
				}
				
				
				//?????????????????????????????????
				BasePage<IcardUserAccount>page=icardUserAccountService.listPageByExample(example);
				List<IcardUserAccount>data=page.getDatas();
				if(data!=null&&data.size()>0) {
					IcardUserCardDTO condtion=DTOUtils.newDTO(IcardUserCardDTO.class);
					//4:????????????
					condtion.setStatusNotIn(Arrays.asList(Byte.valueOf("4")));
					for(IcardUserAccount icardUserAccount:data) {
						//??????????????????????????????crm????????????
						Customer customer=this.transUserAccountAsCustomer(icardUserAccount);
						if(customer==null) {
							this.saveOrUpdateSettlementLatestTime(icardUserAccount.getCreatedTime());
							continue;
						}
						List<Address>address=this.transICardUserAccountAsAddress(icardUserAccount);
						condtion.setAccountId(icardUserAccount.getId());
						//????????????????????????????????????????????????
						List<IcardUserCard> icardUserCardList=this.icardUserCardService.list(condtion);
						//??????????????????
						List<CustomerExtensions> customerExtensionsList=this.transIcardUserCardAsCustomerExtensions(icardUserCardList);
						//???????????????crm
						this.saveOrUpdate(customer, customerExtensionsList,address);
					}
					return true;
				}
				
				return false;
	}
	
	
	private String getMarket() {
		return SystemConfigUtils.getProperty("customer.market", "hd");
	}
	
	/**
	 * @param icardUserAccount ?????????????????????????????????
	 * @return ??????crm??????????????????
	 */
	private Customer transUserAccountAsCustomer(IcardUserAccount icardUserAccount) {

		 //????????????", 10/????????????", 20/????????? 30
		Byte accountType=icardUserAccount.getType();
		
		if(new Byte((byte)30).equals(accountType)||StringUtils.trimToEmpty(icardUserAccount.getName()).equals("?????????")||StringUtils.trimToNull(icardUserAccount.getIdCode())==null) {
			return null;
		}

		Customer customer=DTOUtils.newDTO(Customer.class);
		if(new Byte((byte)10).equals(accountType)) {
			customer.setCertificateType("id");
			customer.setCertificateNumber(icardUserAccount.getIdCode());
			customer.setName(icardUserAccount.getName());
			//individuals:??????
			customer.setOrganizationType("individuals");
		}else if(new Byte((byte)20).equals(accountType)) {
			customer.setCertificateType("businessLicense");
			customer.setCertificateNumber(icardUserAccount.getIdCode());
			customer.setName(icardUserAccount.getName());
			//enterprise:??????
			customer.setOrganizationType("enterprise");
		}else {
			return null;
		}
		//HD ??????
		customer.setMarket(this.getMarket());
		customer.setCreated(icardUserAccount.getCreatedTime());
		customer.setModified(icardUserAccount.getCreatedTime());
		customer.setSyncTime(customer.getCreated());
		//1-??? 2-???
		if(Byte.valueOf("1").equals(icardUserAccount.getGender())) {
			customer.setSex("male"); 
		}else {
			customer.setSex("female"); 
		}

		if(StringUtils.isNotBlank(icardUserAccount.getMobile())) {
			customer.setPhone(icardUserAccount.getMobile());
		}else {
			customer.setPhone(icardUserAccount.getTelphone());
		}
		//settlement:????????????
		customer.setSourceSystem("settlement");
	
		customer.setYn(1);
		
		return customer;
	}
	
	
	/**
	 * @param tollCustomer ?????????????????????????????????
	 * @return ??????crm??????????????????
	 */
	private Customer transTollCustomerAsCustomer(TollCustomer tollCustomer) {
		
		
		Customer customer=DTOUtils.newDTO(Customer.class);
		customer.setCertificateType("id");
		customer.setCertificateNumber(tollCustomer.getIdCard());
		customer.setName(tollCustomer.getName());
		customer.setCreated(tollCustomer.getCreated());
		customer.setSyncTime(customer.getCreated());
		customer.setModified(tollCustomer.getModified());
//		//1-??? 2-???
		if("1".equals(String.valueOf(tollCustomer.getSex()))) {
			customer.setSex("male"); 
		}else {
			customer.setSex("female"); 
		}
//
		if(StringUtils.isNotBlank(tollCustomer.getCellphone())) {
			customer.setPhone(tollCustomer.getCellphone());
		}else {
			customer.setPhone(tollCustomer.getPhone());
		}
		//HD ??????
		customer.setMarket(this.getMarket());
//		//toll:??????
		customer.setSourceSystem("toll");
		//individuals:??????
		customer.setOrganizationType("individuals");
		customer.setNotes(tollCustomer.getRemarks());
		customer.setYn(1);
		
		return customer;
	}
	/**
	 * @param tollCustomer ?????????????????????????????????
	 * @return ??????crm??????????????????
	 */
	private List<Address> transTollCustomerAsAddress(TollCustomer tollCustomer) {
		String[] addressArray=new String[] {StringUtils.trimToNull(tollCustomer.getHomeAddr()),StringUtils.trimToNull(tollCustomer.getAddr())};
		List<Address>resultList=new ArrayList<>();
		
		for(String addr:addressArray) {
			if(addr!=null) {
				Address address=DTOUtils.newDTO(Address.class);
				address.setAddress(addr);
				resultList.add(this.setLocationToAddress(address));
			}
		}
		
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	private Address setLocationToAddress(Address address) {
		String addr=address.getAddress();
		try {
			//???????????????????????????
			BaseOutput<Map<String,Object>> baseOut = addressService.locationAddress(addr);
			if(baseOut.isSuccess()) {
				Map<String,Object> cityJson=baseOut.getData();
				String lat=cityJson.get("lat").toString();
				String lng=cityJson.get("lng").toString();
				//???????????????????????????????????????
				BaseOutput<City>cityBaseOut=addressService.locationReverse(lat, lng);
				if(cityBaseOut.isSuccess()){
					City addressCity=cityBaseOut.getData();
					City city=this.findParentCity(addressCity, 2);
					if(city!=null) {
						 //????????????????????????CityId [level_type:2]
						 String cityId=String.valueOf(city.getId());
						 //???????????????,??????
						 address.setCityId(cityId);
					}
					 address.setLat(lat);
	        		 address.setLng(lng);

				}else {
					logger.info("????????????????????????????????????????????????");
				}
			}else {
				logger.info("????????????????????????????????????");
			}
		} catch (Exception e) {
			logger.error("??????????????????,????????????????????????"+e.getMessage(), e);
		}
		
		return address;
	}
	private City findParentCity(City city,Integer levelType) {
		if(city==null||levelType==null) {
			return null;
		}
		int compareValue=levelType.compareTo(city.getLevelType());
		if(compareValue==0) {
			return city;
		}else if(compareValue<0){
			Long parentId=city.getParentId();
			if(parentId!=null) {
				City parentCity=this.cityService.get(parentId);
				if(parentCity!=null) {
					return this.findParentCity(parentCity, levelType);	
				}
			}
			return null;
		}else {
			return null;
		}
		
	}
	/**
	 * @param tollCustomer ??????????????????????????????
	 * @return ??????crm?????????????????????
	 */
	private List<CustomerExtensions> transTollCustomerAsCustomerExtensions(TollCustomer tollCustomer) {
		List<CustomerExtensions>resultList=new ArrayList<>();
			CustomerExtensions customerExtensions=DTOUtils.newDTO(CustomerExtensions.class);
			
			customerExtensions.setAcctId(String.valueOf(tollCustomer.getId()));
			//account:??????
			customerExtensions.setAcctType("account");
//			customerExtensions.setNotes("??????:"+icardUserCard.getCardNo());

//			customerExtensions.setAcctType("bankCard");
			
			//toll:??????
			customerExtensions.setSystem("toll");
			resultList.add(customerExtensions);

		return resultList;
	}
	/**
	 * @param icardUserAccount ?????????????????????????????????
	 * @return ??????crm??????????????????
	 */
	private List<Address> transICardUserAccountAsAddress(IcardUserAccount icardUserAccount) {
		
		List<Address>resultList=new ArrayList<>();
		String addr=StringUtils.trimToNull(icardUserAccount.getAddress());

		if(addr!=null) {
			Address address=DTOUtils.newDTO(Address.class);
			address.setAddress(addr);
			resultList.add(this.setLocationToAddress(address));
		}
		
		return resultList;
	}
	/**
	 * @param icardUserCardList ??????????????????????????????
	 * @return ??????crm?????????????????????
	 */
	private List<CustomerExtensions> transIcardUserCardAsCustomerExtensions(List<IcardUserCard> icardUserCardList) {
		List<CustomerExtensions>resultList=new ArrayList<>();
		for(IcardUserCard icardUserCard:icardUserCardList) {
			CustomerExtensions customerExtensions=DTOUtils.newDTO(CustomerExtensions.class);
			
			customerExtensions.setAcctId(String.valueOf(icardUserCard.getAccountId()));
			customerExtensions.setNotes("??????:"+icardUserCard.getCardNo());
			//10-?????? 20-?????? 30-????????? 40-???????????????????????????????????????  50???60???????????????
			if(Byte.valueOf("10").equals(icardUserCard.getCategory())) {
				customerExtensions.setAcctType("masterCard");
			}else if(Byte.valueOf("20").equals(icardUserCard.getCategory())) {
				customerExtensions.setAcctType("slaveCard");
			}else if(Byte.valueOf("30").equals(icardUserCard.getCategory())) {
				customerExtensions.setAcctType("tempCard");
			}else if(Byte.valueOf("40").equals(icardUserCard.getCategory())) {
				customerExtensions.setAcctType("unionCard");
			}else{
				customerExtensions.setAcctType("bankCard");
			}
			//settlement:????????????
			customerExtensions.setSystem("settlement");
			resultList.add(customerExtensions);
		}

		return resultList;
	}
}
