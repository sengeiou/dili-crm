package com.dili.crm.service;

import com.dili.crm.domain.BizNumber;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-12-08 09:00:40.
 */
public interface BizNumberService extends BaseService<BizNumber, Long> {

	/**
	 * 获取客户回访编号
	 * @return
	 */
	String getCustomerVisitCode();
}