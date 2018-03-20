package com.dili.points.service.impl;

import com.dili.points.dao.PointsRuleLogMapper;
import com.dili.points.domain.PointsRuleLog;
import com.dili.points.service.PointsRuleLogService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class PointsRuleLogServiceImpl extends BaseServiceImpl<PointsRuleLog, Long> implements PointsRuleLogService {

    public PointsRuleLogMapper getActualDao() {
        return (PointsRuleLogMapper)getDao();
    }
}