package com.dili.crm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.crm.dao.CustomerExtensionsMapper;
import com.dili.crm.dao.CustomerMapper;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.CustomerExtensions;
import com.dili.crm.domain.IcardUserAccount;
import com.dili.crm.domain.IcardUserCard;
import com.dili.crm.domain.dto.IcardUserCardDTO;
import com.dili.crm.provider.YnProvider;
import com.dili.crm.service.CustomerExtensionsService;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.ICardETLService;
import com.dili.crm.service.IcardUserAccountService;
import com.dili.crm.service.IcardUserCardService;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.mysql.fabric.xmlrpc.base.Array;

@Component
public class ICardETLServiceImpl implements ICardETLService{
	@Autowired private IcardUserAccountService icardUserAccountService;
	@Autowired private IcardUserCardService icardUserCardService;
	@Autowired private CustomerService customerService;
	@Autowired private CustomerExtensionsService customerExtensionsService;
	

	@Transactional
	private boolean saveOrUpdate(Customer customer,List<CustomerExtensions> customerExtensionsList) {
		Customer condtion=DTOUtils.newDTO(Customer.class);
		condtion.setCertificateNumber(customer.getCertificateNumber());
		List<Customer>list=this.customerService.list(condtion);
		if(list!=null&&list.size()==1) {
			Customer customerItem=list.get(0);
			if(customer.getCreated()!=null) {
				if(customer.getCreated().after(customerItem.getCreated())) {
					customerItem.setCreated(customer.getCreated());
				}
			}
			customer=customerItem;
		}
		this.customerService.saveOrUpdate(customer);
		
		List<CustomerExtensions>extensionList=new ArrayList<>();
		for(CustomerExtensions customerExtensions:customerExtensionsList) {
			CustomerExtensions customerExtensionsCondtion=DTOUtils.newDTO(CustomerExtensions.class);
			
			customerExtensionsCondtion.setAcctId(customerExtensions.getAcctId());
			customerExtensions.setSystem("settlement");
			
			List<CustomerExtensions>extensions=this.customerExtensionsService.list(customerExtensionsCondtion);
			if(extensions==null||extensions.size()==0) {
				customerExtensions.setCustomerId(customer.getId());
				extensionList.add(customerExtensions);
			}
		}
		if(extensionList.size()>0) {
			this.customerExtensionsService.batchInsert(customerExtensionsList);
		}
		return true;
	}
	private Customer findLatestCustomer() {
		Customer example=DTOUtils.newDTO(Customer.class);
		//settlement:电子结算
		example.setSourceSystem("settlement");
		example.setPage(1);
		example.setRows(1);
		example.setSort("created");
		example.setOrder("DESC");
		BasePage<Customer>page=this.customerService.listPageByExample(example);
		List<Customer>data=page.getDatas();
		if(data!=null&&data.size()==1) {
			return data.get(0);
		}
		return null;
	}
	
	public boolean transIncrementData(Customer latestCustomer,int batchSize) {
		if(latestCustomer==null) {
			latestCustomer=this.findLatestCustomer();
		}
		IcardUserAccount example=DTOUtils.newDTO(IcardUserAccount.class);
		example.setPage(1);
		example.setRows(batchSize);
		example.setSort("created_time");
		example.setOrder("ASC");
		if(latestCustomer!=null) {
			example.setCreatedTime(latestCustomer.getCreated());
		}
		
		
		
		BasePage<IcardUserAccount>page=icardUserAccountService.listPageByExample(example);
		List<IcardUserAccount>data=page.getDatas();
		if(data!=null&&data.size()>0) {
			IcardUserCardDTO condtion=DTOUtils.newDTO(IcardUserCardDTO.class);
			//4:已经退卡
			condtion.setStatusNotIn(Arrays.asList(Byte.valueOf("4")));
			for(IcardUserAccount icardUserAccount:data) {
				Customer customer=this.transUserAccountAsCustomer(icardUserAccount);
				if(customer==null) {
					continue;
				}
				
				condtion.setAccountId(icardUserAccount.getId());
				List<IcardUserCard> icardUserCardList=this.icardUserCardService.list(condtion);
				
				List<CustomerExtensions> customerExtensionsList=this.transIcardUserCardAsCustomerExtensions(icardUserCardList);
				this.saveOrUpdate(customer, customerExtensionsList);
			}
			return true;
		}
		
		return false;
	}
	private Customer transUserAccountAsCustomer(IcardUserAccount icardUserAccount) {
		if(StringUtils.trimToEmpty(icardUserAccount.getName()).equals("不记名")||StringUtils.trimToNull(icardUserAccount.getIdCode())==null) {
			return null;
		}
		
		Customer customer=DTOUtils.newDTO(Customer.class);
		customer.setCertificateType("id");
		customer.setCertificateNumber(icardUserAccount.getIdCode());
		customer.setName(icardUserAccount.getName());
		customer.setCreated(icardUserAccount.getCreatedTime());
		customer.setModified(icardUserAccount.getCreatedTime());
		//1-男 2-女
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
		//settlement:电子结算
		customer.setSourceSystem("settlement");
		//individuals:个人
		customer.setOrganizationType("individuals");
		customer.setYn(1);
		
		return customer;
	}
	private List<CustomerExtensions> transIcardUserCardAsCustomerExtensions(List<IcardUserCard> icardUserCardList) {
		List<CustomerExtensions>resultList=new ArrayList<>();
		for(IcardUserCard icardUserCard:icardUserCardList) {
			CustomerExtensions customerExtensions=DTOUtils.newDTO(CustomerExtensions.class);
			
			customerExtensions.setAcctId(String.valueOf(icardUserCard.getId()));
			customerExtensions.setNotes("卡号:"+icardUserCard.getCardNo());
			//10-主卡 20-副卡 30-临时卡 40-联营卡（目前应该没这种卡）  50和60都是银行卡
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
			//settlement:电子结算
			customerExtensions.setSystem("settlement");
			resultList.add(customerExtensions);
		}

		return resultList;
	}
}
