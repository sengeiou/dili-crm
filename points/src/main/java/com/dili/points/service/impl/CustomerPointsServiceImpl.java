package com.dili.points.service.impl;

import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.service.CustomerPointsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:30.
 */
@Service
public class CustomerPointsServiceImpl extends BaseServiceImpl<CustomerPoints, Long> implements CustomerPointsService {
	@Autowired CustomerRpc customerRpc;
    public CustomerPointsMapper getActualDao() {
        return (CustomerPointsMapper)getDao();
    }
    public DTO findCustomerPointsByCertificateNumber(String certificateNumber) {
    	
    	
    	return null;
    }
    public EasyuiPageOutput listCustomerPointsByCustomer(CustomerApiDTO customer) {

    	BaseOutput<List<Customer>>baseOut=	customerRpc.list(customer);
    	if(baseOut.isSuccess()) {
        	List<Customer>customerList=baseOut.getData().subList(0, 10);
        	List<String>certificateNumbers=customerList.stream().map(Customer::getCertificateNumber).collect(Collectors.toList());
        	
        	
        	CustomerPoints example=DTOUtils.newDTO(CustomerPoints.class);
        	example.setCertificateNumbers(certificateNumbers);
        	example.setPage(customer.getPage());
        	example.setRows(customer.getRows());
        	BasePage<CustomerPoints> basePage=	this.listPageByExample(example);
        	Map<String,CustomerPoints>map=basePage.getDatas().stream().collect(Collectors.toMap(CustomerPoints::getCertificateNumber, cp->cp));
        	
        	
        	List<DTO>resultList=customerList.stream().map(c->{
        		CustomerPoints cp=map.get(c.getCertificateNumber());
        		//如果客户没有对应的积分信息,则创建一个新的默认积分信息显示到页面
        		if(cp==null) {
        			 cp=DTOUtils.newDTO(CustomerPoints.class);
        			 cp.setId(c.getId());
        			 cp.setCertificateNumber(c.getCertificateNumber());
        			 cp.setAvailable(0);
        			 cp.setFrozen(0);
        			 cp.setTotal(0);
        		}
        		//将客户的其他信息(名字,组织类型等信息附加到积分信息)
    			DTO dto=DTOUtils.go(cp);
    			dto.put("name", c.getName());
    			dto.put("organizationType", c.getOrganizationType());
    			dto.put("profession", c.getProfession());
    			dto.put("type", c.getType());
    			dto.put("certificateType", c.getCertificateType());
        		return dto;
        	}).collect(Collectors.toList());
        	

    		return new EasyuiPageOutput(10, resultList);
    	}
    	
		return new EasyuiPageOutput(0, Collections.emptyList());
    	
    }
}