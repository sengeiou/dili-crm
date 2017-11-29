package com.dili.crm.service;

import com.dili.crm.domain.VisitEvent;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-21 16:39:42.
 */
public interface VisitEventService extends BaseService<VisitEvent, Long> {

    /**
     * 根据回访ID删除回访事件
     * @param visitId 回访ID
     * @return
     */
    Integer deleteByVisit(Long visitId);

}