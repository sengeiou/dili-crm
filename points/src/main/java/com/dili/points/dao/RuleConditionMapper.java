package com.dili.points.dao;

import com.dili.points.domain.RuleCondition;
import com.dili.ss.base.MyMapper;

public interface RuleConditionMapper extends MyMapper<RuleCondition> {

    void deleteByRuleId(Long ruleId);
}