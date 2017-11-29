package com.dili.crm.service;

import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.domain.dto.CustomerVisitChartDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.List;

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

    /**
     * 更新回访信息
     * @param customerVisit
     * @return
     */
    BaseOutput updateSelectiveWithOutput(CustomerVisit customerVisit);

    /**
     * 删除回访以及对应的事件
     * @param id 回访ID
     * @return
     */
    BaseOutput deleteAndEvent(Long id);
}