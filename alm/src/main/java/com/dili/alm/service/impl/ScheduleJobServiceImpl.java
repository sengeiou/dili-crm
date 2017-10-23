package com.dili.alm.service.impl;

import com.dili.alm.dao.ScheduleJobMapper;
import com.dili.alm.domain.ScheduleJob;
import com.dili.alm.service.ScheduleJobService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-06-06 10:27:25.
 */
@Service
public class ScheduleJobServiceImpl extends BaseServiceImpl<ScheduleJob, Long> implements ScheduleJobService {

    public ScheduleJobMapper getActualDao() {
        return (ScheduleJobMapper)getDao();
    }
}