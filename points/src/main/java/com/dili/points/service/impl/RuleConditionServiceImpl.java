package com.dili.points.service.impl;

import com.dili.points.dao.RuleConditionMapper;
import com.dili.points.domain.RuleCondition;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class RuleConditionServiceImpl extends BaseServiceImpl<RuleCondition, Long> implements RuleConditionService {

    public RuleConditionMapper getActualDao() {
        return (RuleConditionMapper)getDao();
    }
}