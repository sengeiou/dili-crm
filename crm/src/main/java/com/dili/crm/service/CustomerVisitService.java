package com.dili.crm.service;

import java.util.List;

import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.domain.dto.CustomerVisitChartDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-21 17:27:41.
 */
public interface CustomerVisitService extends BaseService<CustomerVisit, Long> {

    /**
     * 新增回访信息
     * @param customerVisit
     * @return
     */
    BaseOutput insertSelectiveWithOutput(CustomerVisit customerVisit);
    
    BaseOutput<List<CustomerVisitChartDTO>> selectCustomerVisitGroupByMode();
    
    BaseOutput<List<CustomerVisitChartDTO>> selectCustomerVisitGroupByState();
}