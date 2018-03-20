package com.dili.points.service.impl;

import com.dili.points.dao.PointsRuleMapper;
import com.dili.points.domain.PointsRule;
import com.dili.points.service.PointsRuleService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class PointsRuleServiceImpl extends BaseServiceImpl<PointsRule, Long> implements PointsRuleService {

    public PointsRuleMapper getActualDao() {
        return (PointsRuleMapper)getDao();
    }
}