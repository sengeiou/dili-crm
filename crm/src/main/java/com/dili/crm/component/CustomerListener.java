package com.dili.crm.component;

import com.dili.crm.boot.RabbitConfiguration;
import com.dili.crm.converter.DtoMessageConverter;
import com.dili.crm.domain.Address;
import com.dili.crm.domain.City;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.CustomerExtensions;
import com.dili.crm.domain.User;
import com.dili.crm.provider.YnProvider;
import com.dili.crm.service.AddressService;
import com.dili.crm.service.CacheService;
import com.dili.crm.service.CityService;
import com.dili.crm.service.CustomerExtensionsService;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.IcardUserAccountService;
import com.dili.crm.service.IcardUserCardService;
import com.dili.crm.service.SystemConfigService;
import com.dili.crm.service.toll.TollCustomerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.SystemConfigUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户消费组件
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
public class CustomerListener {
	private static final Logger logger=LoggerFactory.getLogger(CustomerListener.class);
	@Autowired private CustomerService customerService;
	@Autowired private CustomerExtensionsService customerExtensionsService;
	@Autowired private AddressService addressService;
	@Autowired private CityService cityService;
	
	/*@Autowired
    private AmqpTemplate amqpTemplate;
	public void testCustomerMessage() {
		Customer customer = DTOUtils.newDTO(Customer.class);
        customer.setId(1L);
        customer.setName("客户1");
        ObjectMapper m=new ObjectMapper();
        try {
			customer.setCertificateType("id");
			customer.setCertificateNumber("268598541236014587");
			customer.setOrganizationType("individuals");
			customer.setSex("male");
			customer.setPhone("13888889999");
			customer.setSourceSystem("toll");
			customer.setCreated(new Date());
			customer.setSyncTime(new Date());
			customer.setNotes("test customer");
		Map<String,Object>map=	BeanUtilsBean2.getInstance().getPropertyUtils().describe(customer);
		map.put("address", new String[] {"四川省成都市"});
			m.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        	String s=m.writeValueAsString(map);
			amqpTemplate.convertAndSend(RabbitConfiguration.DEFAULT_TOPIC_EXCHANGE, RabbitConfiguration.TOPIC_ROUTE_KEY, s);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	@RabbitListener(queues = "#{rabbitConfiguration.TOPIC_QUEUE}")
	public void processBootTask(String customerJson) {
		
		try {
			//将json转换为map结构
			Optional<Map<String,Object>> mapOpt=	DtoMessageConverter.convertAsMap(customerJson);
			mapOpt.ifPresent((jsonMap)->{
				
				String type = StringUtils.trimToNull(String.valueOf(jsonMap.get("type")));
				if (type == null || type.equalsIgnoreCase("json")) {
					Map<String,Object>dataMap=(Map<String, Object>) jsonMap.get("data");
							
					//获得address
					List<String> addressObjList=(List<String>) dataMap.remove("address");
					//获得CustomerExtension
					List<Map<String,Object>> extensionsObjList=(List<Map<String,Object>>) dataMap.remove("extensions");
					//map中其余信息为Customer信息
					Map<String,Object> customerObj=dataMap;
					
					this.processCustomer(customerJson,customerObj, addressObjList, extensionsObjList);	
				}else {
					
				}
				
			});
		}catch(Exception e) {
			logger.error("转换Customer对象: {} 出错 {}",customerJson,e);
		}
	}
	/** 进行对象转换和验证并将数据插入(更新到数据库)
	 * @param customerJson 原始customer json
	 * @param customerObj  转换为map的customer数据
	 * @param addressObjList转换为List<String>的address数据
	 * @param extensionsObjList转换为List<Map>的extension数据
	 */
	private void processCustomer(String customerJson,Map<String,Object> customerObj,List<String> addressObjList,List<Map<String,Object>> extensionsObjList) {
		Optional<Customer>customerOpt=this.convertAndValidateCustomer(customerJson,customerObj);
		List<CustomerExtensions>extensionList=this.convertAndValidateExtension(customerOpt,extensionsObjList);
		List<Address>addressList=this.convertAndValidateAddress(addressObjList);
		
		customerOpt.ifPresent(customer->{
			logger.info("process customer");
			this.saveOrUpdate(customer, extensionList, addressList);
		});
	}
	/**
	 * @param addressObjList 转换为List<String>的address数据
	 * @return 返回地址对象集合
	 */
	private List<Address>convertAndValidateAddress(List<String> addressObjList){
		List<Address>resultList=new ArrayList<>();
		if(addressObjList!=null) {
			for(String addr:addressObjList) {
				Address address=DTOUtils.newDTO(Address.class);
				address.setAddress(addr);
				//设置经纬度
				this.setLocationToAddress(address);
				resultList.add(address);
			}
		}
		return resultList;
	}
	/**
	 * @param customerJson 原始customer json
	 * @param customerObj转换为map的customer数据
	 * @return Customer对象
	 */
	private Optional<Customer> convertAndValidateCustomer(String customerJson,Map<String,Object> customerObj){
		Customer customer=DTOUtils.proxy(new DTO(customerObj), Customer.class);
		if(this.validateCustomer(customer)) {
			customer.setMarket(this.getMarket());
			customer.setYn(1);
			return Optional.ofNullable(customer);
		}else {
			logger.error("验证客户信息出错:"+customerJson);
		}
		return Optional.empty();
	}
	/**
	 * @param extensionsObjList 转换为List<Map>的extension数据
	 * @return 转换后的List<CustomerExtensions>
	 */
	private List<CustomerExtensions>convertAndValidateExtension(Optional<Customer>customerOpt,List<Map<String,Object>> extensionsObjList){
		List<CustomerExtensions>resultList=new ArrayList<>();
		if(extensionsObjList!=null) {
			for(Map<String,Object>map:extensionsObjList) {
				CustomerExtensions customerExtensions=DTOUtils.proxy(new DTO(map), CustomerExtensions.class);
				customerOpt.ifPresent(c->{
					customerExtensions.setSystem(c.getSourceSystem());	
				});
				
				resultList.add(customerExtensions);
			}
		}
		return resultList;
	}
	private String getMarket() {
		return SystemConfigUtils.getProperty("customer.market", "HD");
	}
	
	/**
	 * @param customer 要进行验证的customer对象
	 * @return 验证无误返回true
	 */
	private boolean validateCustomer(Customer customer) {
		if(StringUtils.isBlank(customer.getCertificateType())) {
			return false;
		}
		if(StringUtils.isBlank(customer.getCertificateNumber())) {
			return false;
		}
		if(StringUtils.isBlank(customer.getOrganizationType())) {
			return false;
		}
		if(StringUtils.isBlank(customer.getName())) {
			return false;
		}
		if(StringUtils.isBlank(customer.getSex())) {
			return false;
		}
		if(StringUtils.isBlank(customer.getPhone())) {
			return false;
		}
		if(StringUtils.isBlank(customer.getSourceSystem())) {
			return false;
		}
		if(customer.getCreated()==null) {
			return false;
		}
		return true;
	}
	/**
	 * @param customer 要插入或者更新到crm的客户信息
	 * @param customerExtensionsList 要插入到或者更新到cmr的帐户信息
	 * @return true保存成功,false 保存失败
	 */
	@Transactional
	private boolean saveOrUpdate(Customer customer,List<CustomerExtensions> customerExtensionsList,List<Address>address) {
		
		
		Customer customerQueryCondition=DTOUtils.newDTO(Customer.class);
		customerQueryCondition.setCertificateNumber(customer.getCertificateNumber());
		customerQueryCondition.setCertificateType(customer.getCertificateType());
		customerQueryCondition.setOrganizationType(customer.getOrganizationType());
		//用身份证号在crm系统查询用户信息
		List<Customer>list=this.customerService.list(customerQueryCondition);
		if(list!=null&&list.size()==1) {
			Customer customerItem=list.get(0);
			//更新用户信息
			if(customerItem.getSyncTime()==null) {
				customerItem.setSyncTime(customerItem.getCreated());
			}
			if(customer.getSyncTime()==null) {
				customer.setSyncTime(new Date());
			}
			if(customerItem.getSyncTime().before(customer.getSyncTime())) {
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
		
		//对账号信息做插入或者更新的判断
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
		
		//对地址信息做插入或者更新的判断
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
				//如果当前客户没有默认地址,将第一个设置为默认地址
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
	@SuppressWarnings("unchecked")
	private Address setLocationToAddress(Address address) {
		String addr=address.getAddress();
		try {
			//根据地址查询经纬度
			BaseOutput<Map<String,Object>> baseOut = addressService.locationAddress(addr);
			if(baseOut.isSuccess()) {
				Map<String,Object> cityJson=baseOut.getData();
				String lat=cityJson.get("lat").toString();
				String lng=cityJson.get("lng").toString();
				//根据经纬度查询行政区划代码
				BaseOutput<City>cityBaseOut=addressService.locationReverse(lat, lng);
				if(cityBaseOut.isSuccess()){
					City addressCity=cityBaseOut.getData();
					City city=this.findParentCity(addressCity, 2);
					if(city!=null) {
						 //行政区划代码即为CityId [level_type:2]
						 String cityId=String.valueOf(city.getId());
						 //设置经纬度,城市
						 address.setCityId(cityId);
					}
					 address.setLat(lat);
	        		 address.setLng(lng);

				}else {
					logger.info("根据经纬度未能查询到行政区划代码");
				}
			}else {
				logger.info("根据地址未能查询到经纬度");
			}
		} catch (Exception e) {
			logger.error("同步地址信息,查询地址信息异常"+e.getMessage(), e);
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
}
