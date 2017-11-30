package com.dili.crm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.crm.dao.CustomerExtensionsMapper;
import com.dili.crm.dao.CustomerMapper;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.CustomerExtensions;
import com.dili.crm.domain.IcardUserAccount;
import com.dili.crm.domain.IcardUserCard;
import com.dili.crm.service.CustomerExtensionsService;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.ICardETLService;
import com.dili.crm.service.IcardUserAccountService;
import com.dili.crm.service.IcardUserCardService;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;

@Service
public class ICardETLServiceImpl implements ICardETLService{
	@Autowired private IcardUserAccountService icardUserAccountService;
	@Autowired private IcardUserCardService icardUserCardService;
	@Autowired private CustomerService customerService;
	@Autowired private CustomerExtensionsService customerExtensionsService;
	
	public Customer transAllData(int batchSize) {
		IcardUserAccount example=DTOUtils.newDTO(IcardUserAccount.class);
		example.mset("page", 1);
		example.mset("rows", batchSize);
		BasePage<IcardUserAccount>page=icardUserAccountService.listPageByExample(example);
		List<IcardUserAccount>data=page.getDatas();
		if(data!=null&&data.size()>0) {
			IcardUserCard condtion=DTOUtils.newDTO(IcardUserCard.class);
			for(IcardUserAccount icardUserAccount:data) {
				Customer customer=this.transUserAccountAsCustomer(icardUserAccount);
				
//				condtion.setStatus();FIXME
				condtion.setAccountId(icardUserAccount.getId());
				List<IcardUserCard> icardUserCardList=this.icardUserCardService.list(condtion);
				
				List<CustomerExtensions> customerExtensionsList=this.transIcardUserCardAsCustomerExtensions(icardUserCardList);
				this.saveOrUpdate(customer, customerExtensionsList);
			}
		}
		
		return null;
	}
	@Transactional
	public boolean saveOrUpdate(Customer customer,List<CustomerExtensions> customerExtensionsList) {
		this.customerService.saveOrUpdate(customer);
		for(CustomerExtensions customerExtensions:customerExtensionsList) {
			customerExtensions.setCustomerId(customer.getId());
		}
		this.customerExtensionsService.batchInsert(customerExtensionsList);
		return true;
	}
	public Customer transIncrementData(Customer latestCustomer,int batchSize) {
		return latestCustomer;
	}
	private Customer transUserAccountAsCustomer(IcardUserAccount icardUserAccount) {
		Customer customer=DTOUtils.newDTO(Customer.class);
		customer.setCertificateType("id");
		customer.setCertificateNumber(icardUserAccount.getIdCode());
		customer.setName(icardUserAccount.getName());
//		customer.setSex(icardUserAccount.getGender()); //TODO
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
			
			customerExtensions.setAcctId(icardUserCard.getCardNo());
//			customerExtensions.setAcctType(icardUserCard.getCategory());TODO
			customerExtensions.setSystem("电子结算");
			resultList.add(customerExtensions);
		}

		return resultList;
	}
}
