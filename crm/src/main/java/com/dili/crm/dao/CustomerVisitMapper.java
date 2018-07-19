package com.dili.crm.dao;

import java.util.List;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.domain.dto.CustomerVisitChartDTO;
import com.dili.crm.domain.dto.CustomerVisitDto;
import com.dili.ss.base.MyMapper;

public interface CustomerVisitMapper extends MyMapper<CustomerVisit> {

	/**
	 * 根据回访方式分组查询回访统计报表
	 * @param firmCodes 市场权限编码集
	 * @return
	 */
	List<CustomerVisitChartDTO> selectCustomerVisitGroupByMode(List<String> firmCodes);

	/**
	 * 根据回访状态分组查询回访统计报表
	 * @param firmCodes 市场权限编码集
	 * @return
	 */
	List<CustomerVisitChartDTO> selectCustomerVisitGroupByState(List<String> firmCodes);

	/**
	 * 根据条件联合客户信息查询数据
	 * @param customerVisit
	 * @return
	 */
	List<CustomerVisitDto> selectForPage(CustomerVisit customerVisit);
}