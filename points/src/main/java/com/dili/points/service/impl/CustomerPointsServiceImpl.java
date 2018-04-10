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
import com.dili.ss.dao.CommonMapper;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.dili.ss.exception.AppException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.google.common.collect.Lists;

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
	@Autowired CommonMapper commonMapper;

	public CustomerPointsMapper getActualDao() {
		return (CustomerPointsMapper) getDao();
	}
	// 计算总可用积分
	private Long calculateTotalPoints() {
		BigDecimal totalAvailablePoints=commonMapper.selectMap("select sum(available) as available from customer_points where yn=1").stream()
				.filter(m->m!=null&&m.containsKey("available"))
				.map(m->{return (BigDecimal)m.get("available");})
				.findFirst()
				.orElse(BigDecimal.ZERO);
		return totalAvailablePoints.longValue();
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
		List<CustomerPointsDTO> resultList = Lists.newArrayList();
		int total;
		//如果按可用积分排序，需要以客户积分表为主表，否则以客户表为主表
		if ("available".equalsIgnoreCase(customer.getSort())) {
			total = sortByAvailable(customer, resultList);
		} else {
			total = sortByNoneAvailable(customer, resultList);
		}
		//提供者转换
//		List<Map> datas = new ArrayList<>();
//		try {
//			datas = ValueProviderUtils.buildDataByProvider(customer, resultList);
//		} catch (Exception e) {
//		   LOG.error("查询客户积分出错",e);
//		}
		EasyuiPageOutput easyuiPageOutput = new EasyuiPageOutput(total, resultList);
		//页脚汇总
		List<Map<String,Object>> footers = Lists.newArrayList();
		Map<String,Object>footer = new HashMap<>(2);
		footer.put("name", "总可用积分:");
		footer.put("organizationType", this.calculateTotalPoints());
		footers.add(footer);
		easyuiPageOutput.setFooter(footers);
		return easyuiPageOutput;
	}

	/**
	 * 根据非可用积分排序的查询
	 * @param customer
	 * @param resultList 客户积分列表的最终结果
	 * @return	返回总记录数
	 */
	private int sortByNoneAvailable(CustomerApiDTO customer, List<CustomerPointsDTO> resultList){
		//如果是可用积分排序，需要去掉，因为RPC调用CRM的客户时，会因为客户表没有available字段而报错
		//这里先记录下来，后面会用于积分表的排序
		String sort = customer.getSort();
		String order = customer.getOrder();
		customer.setYn(1);
		BaseOutput<EasyuiPageOutput> baseOut = customerRpc.listPage(customer);
		if (!baseOut.isSuccess()) {
			throw new AppException("远程调用失败:"+baseOut.getResult());
//			return new EasyuiPageOutput(0, Collections.emptyList());
		}
		//设置总数，计算完resultList后返回
		int total = baseOut.getData().getTotal();
		List<JSONObject> jsonList = baseOut.getData().getRows();
		//构建客户列表
		List<Customer> customerList = jsonList.stream().map(json -> {
			return (Customer) DTOUtils.as(new DTO(json), Customer.class);
		}).collect(Collectors.toList());
		//客户列表的证件号码
		List<String> certificateNumbers = customerList.stream()
				.map(Customer::getCertificateNumber)
				.collect(Collectors.toList());
		CustomerPointsDTO example = DTOUtils.newDTO(CustomerPointsDTO.class);
		example.setCertificateNumbers(certificateNumbers);
		example.setPage(customer.getPage());
		example.setRows(customer.getRows());
		List<CustomerPoints> customerPointss = this.listByExample(example);
		Map<String, CustomerPoints> certificateNumber2CustomerPointsMap = customerPointss.stream()
				.collect(Collectors.toMap(CustomerPoints::getCertificateNumber, cp -> cp));
		resultList.addAll(customerList.stream().map(c -> {
			CustomerPoints cp = certificateNumber2CustomerPointsMap.get(c.getCertificateNumber());
			// 如果客户没有对应的积分信息,则创建一个新的默认积分信息显示到页面
			CustomerPointsDTO cpdto = DTOUtils.newDTO(CustomerPointsDTO.class);
			if (cp == null) {
				cpdto.setId(c.getId());
				cpdto.setCertificateNumber(c.getCertificateNumber());
				cpdto.setAvailable(0);
				cpdto.setFrozen(0);
				cpdto.setTotal(0);
			} else {
				cpdto = DTOUtils.link(cpdto, cp, CustomerPointsDTO.class);
			}
			// 将客户的其他信息(名字,组织类型等信息附加到积分信息)
			cpdto.setName(c.getName());
			cpdto.setOrganizationType(c.getOrganizationType());
			cpdto.setProfession(c.getProfession());
			cpdto.setType(c.getType());
			cpdto.setCertificateType(c.getCertificateType());
			cpdto.setPhone(c.getPhone());
			return cpdto;
		})
		.collect(Collectors.toList()));
		return total;
	}

	/**
	 * 根据可用积分排序的查询
	 * @param customer
	 * @param resultList 客户积分列表的最终结果
	 * @return	返回总记录数
	 */
	private int sortByAvailable(CustomerApiDTO customer, List<CustomerPointsDTO> resultList){
		//如果是可用积分排序，需要去掉，因为RPC调用CRM的客户时，会因为客户表没有available字段而报错
		//这里先记录下来，后面会用于积分表的排序
		String sort = customer.getSort();
		String order = customer.getOrder();
		//记录每页条数和页数
		Integer rows = customer.getRows();
		Integer page = customer.getPage();
		//记录列表总数
		int total = 0;
		//客户表不能按可用积分列表，所以要去掉
		customer.setSort(null);
		customer.setOrder(null);
		customer.setYn(1);
		//关联积分表只能全查客户
		customer.setRows(null);
		customer.setPage(null);
		BaseOutput<EasyuiPageOutput> baseOut = customerRpc.listPage(customer);
		if (!baseOut.isSuccess()) {
			throw new AppException("远程调用失败:"+baseOut.getResult());
//			return new EasyuiPageOutput(0, Collections.emptyList());
		}
		List<JSONObject> jsonList = baseOut.getData().getRows();
		//构建客户列表
		List<Customer> customerList = jsonList.stream().map(json -> {
			return (Customer) DTOUtils.as(new DTO(json), Customer.class);
		}).collect(Collectors.toList());

		//构建客户积分查询条件
		CustomerPointsDTO example = DTOUtils.newDTO(CustomerPointsDTO.class);
		example.setPage(customer.getPage());
		example.setRows(customer.getRows());
		example.setOrder(order);
		example.setSort("available");
		List<CustomerPoints> customerPointsList = this.listByExample(example);
		//构建客户证件号为key，客户积分为value的Map
		Map<String, CustomerPoints> certificateNumber2CustomerPointsMap = customerPointsList.stream()
				.collect(Collectors.toMap(CustomerPoints::getCertificateNumber, c -> c));
		//先根据客户积分表的数据构建客户积分结果列表
		resultList.addAll(customerList.stream().map(c -> {
			CustomerPointsDTO cpdto = DTOUtils.newDTO(CustomerPointsDTO.class);
			CustomerPoints customerPoints = certificateNumber2CustomerPointsMap.get(c.getCertificateNumber());
			if (customerPoints == null) {
				cpdto.setId(c.getId());
				cpdto.setCertificateNumber(c.getCertificateNumber());
				cpdto.setAvailable(0);
				cpdto.setFrozen(0);
				cpdto.setTotal(0);
			} else {
				cpdto = DTOUtils.as(customerPoints, CustomerPointsDTO.class);
			}
			// 将客户的其他信息(名字,组织类型等信息附加到积分信息)
			cpdto.setName(c.getName());
			cpdto.setOrganizationType(c.getOrganizationType());
			cpdto.setProfession(c.getProfession());
			cpdto.setType(c.getType());
			cpdto.setCertificateType(c.getCertificateType());
			cpdto.setPhone(c.getPhone());
			return cpdto;
		}).collect(Collectors.toList()));
		//排序
		if("desc".equalsIgnoreCase(order)){
			resultList.sort(Comparator.comparingInt(CustomerPointsDTO::getAvailable).reversed());
		}else{
			resultList.sort(Comparator.comparingInt(CustomerPointsDTO::getAvailable));
		}
		total = resultList.size();
		if(rows != null && page != null) {
			//根据分页信息进行本地分页
			int startIndex = (page - 1) * rows;
			resultList.subList(startIndex, startIndex + rows);
		}
		return total;
	}
}