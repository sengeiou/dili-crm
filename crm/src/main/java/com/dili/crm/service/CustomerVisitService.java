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

	/**基于回访信息类型分组查询统计数据
	 * @return 信息数量,类型数据列表
	 */
    BaseOutput<List<CustomerVisitChartDTO>> selectCustomerVisitGroupByMode(String firmCode);


	/**基于回访信息状态分组查询统计数据
	 * @return 信息数量,状态数据列表
	 */
    BaseOutput<List<CustomerVisitChartDTO>> selectCustomerVisitGroupByState(String firmCode);

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