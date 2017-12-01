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
import com.dili.crm.service.CustomerExtensionsService;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.ICardETLService;
import com.dili.crm.service.IcardUserAccountService;
import com.dili.crm.service.IcardUserCardService;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;

@Component
public class ICardETLServiceImpl implements ICardETLService{
	@Autowired private IcardUserAccountService icardUserAccountService;
	@Autowired private IcardUserCardService icardUserCardService;
	@Autowired private CustomerService customerService;
	@Autowired private CustomerExtensionsService customerExtensionsService;
	


	private boolean saveOrUpdate(Customer customer,List<CustomerExtensions> customerExtensionsList) {
		this.customerService.saveOrUpdate(customer);
		for(CustomerExtensions customerExtensions:customerExtensionsList) {
			customerExtensions.setCustomerId(customer.getId());
		}
		this.customerExtensionsService.batchInsert(customerExtensionsList);
		return true;
	}
	private Customer findLatestCustomer() {
		Customer example=DTOUtils.newDTO(Customer.class);
		example.setSourceSystem("电子结算");
		example.setPage(1);
		example.setRows(1);
		example.setSort("created");
		example.setOrder("ASC");
		BasePage<Customer>page=this.customerService.listPageByExample(example);
		List<Customer>data=page.getDatas();
		if(data!=null&&data.size()==1) {
			return data.get(0);
		}
		return null;
	}
	@Transactional
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
		Customer customer=DTOUtils.newDTO(Customer.class);
		customer.setCertificateType("id");
		customer.setCertificateNumber(icardUserAccount.getIdCode());
		customer.setName(icardUserAccount.getName());
		customer.setCreated(icardUserAccount.getCreatedTime());
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
		customer.setSourceSystem("电子结算");
		
		
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
			customerExtensions.setSystem("电子结算");
			resultList.add(customerExtensions);
		}

		return resultList;
	}
}
