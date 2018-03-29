package com.dili.points.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.CustomerPointsDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.service.CustomerPointsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.dili.ss.metadata.ValueProviderUtils;
import com.google.common.collect.Lists;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-03-20 11:29:30.
 */
@Service
public class CustomerPointsServiceImpl extends BaseServiceImpl<CustomerPoints, Long> implements CustomerPointsService {
	private static final Logger LOG=LoggerFactory.getLogger(CustomerPointsServiceImpl.class);
	@Autowired
	CustomerRpc customerRpc;

	public CustomerPointsMapper getActualDao() {
		return (CustomerPointsMapper) getDao();
	}

	@Override
    public CustomerPointsDTO findCustomerPointsByCertificateNumber(String certificateNumber) {
		CustomerApiDTO dto = DTOUtils.newDTO(CustomerApiDTO.class);
		dto.setCertificateNumber(certificateNumber);
		EasyuiPageOutput output = this.listCustomerPointsByCustomer(dto);
		List<CustomerPointsDTO> list = output.getRows();
		return list.stream().findFirst().orElseGet(() -> {
			CustomerPointsDTO cp = DTOUtils.newDTO(CustomerPointsDTO.class);
			cp.setId(0L);
			cp.setCertificateNumber(certificateNumber);
			cp.setAvailable(0);
			cp.setFrozen(0);
			cp.setTotal(0);
			return cp;

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

			CustomerPointsDTO example = DTOUtils.newDTO(CustomerPointsDTO.class);
			example.setCertificateNumbers(certificateNumbers);
			example.setPage(customer.getPage());
			example.setRows(customer.getRows());
			BasePage<CustomerPoints> basePage = this.listPageByExample(example);
			
			Map<String, CustomerPoints> map = basePage.getDatas().stream()
					.collect(Collectors.toMap(CustomerPoints::getCertificateNumber, cp -> cp));
			AtomicInteger totalAvailablePoints=new AtomicInteger(0);
			List<CustomerPointsDTO> resultList = customerList.stream().map(c -> {
				CustomerPoints cp = map.get(c.getCertificateNumber());
				// 如果客户没有对应的积分信息,则创建一个新的默认积分信息显示到页面
				CustomerPointsDTO cpdto =DTOUtils.newDTO(CustomerPointsDTO.class);
				if (cp == null) {
					cpdto.setId(c.getId());
					cpdto.setCertificateNumber(c.getCertificateNumber());
					cpdto.setAvailable(0);
					cpdto.setFrozen(0);
					cpdto.setTotal(0);
				}else {
					try {
						BeanUtils.copyProperties(cpdto, cp);
					} catch (Exception e) {
						  LOG.error("查询客户积分出错",e);
					}
				}
				
				totalAvailablePoints.addAndGet(cpdto.getAvailable());
				// 将客户的其他信息(名字,组织类型等信息附加到积分信息)
				cpdto.setName(c.getName());
				cpdto.setOrganizationType(c.getOrganizationType());
				cpdto.setProfession(c.getProfession());
				cpdto.setType(c.getType());
				cpdto.setCertificateType(c.getCertificateType());
				cpdto.setPhone(c.getPhone());
				return cpdto;
			}).collect(Collectors.toList());
            //提供者转换
            List<Map> datas = new ArrayList<>();
            try {
                datas = ValueProviderUtils.buildDataByProvider(customer, resultList);
            } catch (Exception e) {
               LOG.error("查询客户积分出错",e);
            }
            EasyuiPageOutput easyuiPageOutput = new EasyuiPageOutput(baseOut.getData().getTotal(), datas);

            List<Map> footers = Lists.newArrayList();
            Map footer = new HashMap(1);
            footer.put("id", "总可用积分");
            footer.put("organizationType", totalAvailablePoints.get());
            footers.add(footer);
            easyuiPageOutput.setFooter(footers);
            return easyuiPageOutput;
		}
		return new EasyuiPageOutput(0, Collections.emptyList());

	}
}