package com.dili.crm.service.impl;

import com.dili.crm.dao.VisitEventMapper;
import com.dili.crm.domain.VisitEvent;
import com.dili.crm.service.VisitEventService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-21 16:39:42.
 */
@Service
public class VisitEventServiceImpl extends BaseServiceImpl<VisitEvent, Long> implements VisitEventService {

    public VisitEventMapper getActualDao() {
        return (VisitEventMapper)getDao();
    }
}