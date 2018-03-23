package com.dili.points.service.impl;

import com.alibaba.fastjson.JSONObject;
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

import com.dili.ss.metadata.ValueProviderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-03-20 11:29:30.
 */
@Service
public class CustomerPointsServiceImpl extends BaseServiceImpl<CustomerPoints, Long> implements CustomerPointsService {
	@Autowired
	CustomerRpc customerRpc;

	public CustomerPointsMapper getActualDao() {
		return (CustomerPointsMapper) getDao();
	}

	@Override
    public DTO findCustomerPointsByCertificateNumber(String certificateNumber) {
		CustomerApiDTO dto = DTOUtils.newDTO(CustomerApiDTO.class);
		dto.setCertificateNumber(certificateNumber);
		EasyuiPageOutput output = this.listCustomerPointsByCustomer(dto);
		List<DTO> list = output.getRows();
		return list.stream().findFirst().orElseGet(() -> {
			CustomerPoints cp = DTOUtils.newDTO(CustomerPoints.class);
			cp.setId(0L);
			cp.setCertificateNumber(certificateNumber);
			cp.setAvailable(0);
			cp.setFrozen(0);
			cp.setTotal(0);
			return DTOUtils.go(cp);

		});
	}

	@Override
    public EasyuiPageOutput listCustomerPointsByCustomer(CustomerApiDTO customer) {
		BaseOutput<EasyuiPageOutput> baseOut = customerRpc.listPage(customer);
		if (baseOut.isSuccess()) {
			List<JSONObject> jsonList = baseOut.getData().getRows();
			List<Customer> customerList = jsonList.stream().map(json -> {
				return (Customer) DTOUtils.as(new DTO(json), Customer.class);
			}).collect(Collectors.toList());
			
			List<String> certificateNumbers = customerList.stream()
					.map(Customer::getCertificateNumber)
					.collect(Collectors.toList());

			CustomerPoints example = DTOUtils.newDTO(CustomerPoints.class);
			example.setCertificateNumbers(certificateNumbers);
			example.setPage(customer.getPage());
			example.setRows(customer.getRows());
			BasePage<CustomerPoints> basePage = this.listPageByExample(example);
			
			Map<String, CustomerPoints> map = basePage.getDatas().stream()
					.collect(Collectors.toMap(CustomerPoints::getCertificateNumber, cp -> cp));

			List<CustomerPoints> resultList = customerList.stream().map(c -> {
				CustomerPoints cp = map.get(c.getCertificateNumber());
				// 如果客户没有对应的积分信息,则创建一个新的默认积分信息显示到页面
				if (cp == null) {
					cp = DTOUtils.newDTO(CustomerPoints.class);
					cp.setId(c.getId());
					cp.setCertificateNumber(c.getCertificateNumber());
					cp.setAvailable(0);
					cp.setFrozen(0);
					cp.setTotal(0);
				}
				// 将客户的其他信息(名字,组织类型等信息附加到积分信息)
                cp.aset("name", c.getName());
				cp.aset("organizationType", c.getOrganizationType());
				cp.aset("profession", c.getProfession());
				cp.aset("type", c.getType());
				cp.aset("certificateType", c.getCertificateType());
				return cp;
			}).collect(Collectors.toList());
            //提供者转换
            List<Map> datas = null;
            try {
                datas = ValueProviderUtils.buildDataByProvider(customer, resultList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new EasyuiPageOutput(baseOut.getData().getTotal(), datas);
		}
		return new EasyuiPageOutput(0, Collections.emptyList());

	}
}