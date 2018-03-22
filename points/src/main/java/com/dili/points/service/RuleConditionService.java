package com.dili.points.service;

import com.dili.points.domain.RuleCondition;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
public interface RuleConditionService extends BaseService<RuleCondition, Long> {

    void deleteByRuleId(Long ruleId);
}