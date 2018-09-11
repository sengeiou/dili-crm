package com.dili.dp.dao;

import com.dili.dp.domain.RuleCondition;
import com.dili.ss.base.MyMapper;

public interface RuleConditionMapper extends MyMapper<RuleCondition> {

    void deleteByRuleId(Long ruleId);
}