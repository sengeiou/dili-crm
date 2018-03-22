package com.dili.points.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.points.dao.PointsRuleMapper;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.RuleCondition;
import com.dili.points.service.PointsRuleService;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class PointsRuleServiceImpl extends BaseServiceImpl<PointsRule, Long> implements PointsRuleService {

    @Autowired
    private RuleConditionService ruleConditionService;

    public PointsRuleMapper getActualDao() {
        return (PointsRuleMapper) getDao();
    }

    @Override
    public int insertPointRule(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        pointsRule.setCreatedId(SessionContext.getSessionContext().getUserTicket().getId());
        pointsRule.setYn(0);
        getActualDao().insertSelective(pointsRule);

        return 0;
    }

    private void makeRuleCondition(PointsRule pointsRule , String numberJson, String moneyJson, String payMethodJson){
        List<RuleCondition> numberRuleConditions = JSON.parseArray(numberJson, RuleCondition.class);
        for (RuleCondition numberRuleCondition : numberRuleConditions) {
            numberRuleCondition.setCreatedId(pointsRule.getCreatedId());
            numberRuleCondition.setPointRuleId(pointsRule.getId());
            ruleConditionService.insertSelective(numberRuleCondition);
        }

        List<RuleCondition> moneyRuleConditions = JSON.parseArray(moneyJson, RuleCondition.class);

        for (RuleCondition moneyRuleCondition : moneyRuleConditions) {
            moneyRuleCondition.setCreatedId(pointsRule.getCreatedId());
            moneyRuleCondition.setPointRuleId(pointsRule.getId());
            ruleConditionService.insertSelective(moneyRuleCondition);
        }


        List<RuleCondition> payRuleConditions = JSON.parseArray(payMethodJson, RuleCondition.class);

        for (RuleCondition payRuleCondition : payRuleConditions) {
            payRuleCondition.setCreatedId(pointsRule.getCreatedId());
            payRuleCondition.setPointRuleId(pointsRule.getId());
            ruleConditionService.insertSelective(payRuleCondition);
        }
    }
    @Override
    public int updatePointRule(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        getActualDao().updateByPrimaryKeySelective(pointsRule);
        ruleConditionService.deleteByRuleId(pointsRule.getId());

        List<RuleCondition> numberRuleConditions = JSON.parseArray(numberJson, RuleCondition.class);
        for (RuleCondition numberRuleCondition : numberRuleConditions) {
            numberRuleCondition.setCreatedId(pointsRule.getCreatedId());
            numberRuleCondition.setPointRuleId(pointsRule.getId());
            ruleConditionService.insertSelective(numberRuleCondition);
        }

        List<RuleCondition> moneyRuleConditions = JSON.parseArray(moneyJson, RuleCondition.class);

        for (RuleCondition moneyRuleCondition : moneyRuleConditions) {
            moneyRuleCondition.setCreatedId(pointsRule.getCreatedId());
            moneyRuleCondition.setPointRuleId(pointsRule.getId());
            ruleConditionService.insertSelective(moneyRuleCondition);
        }


        List<RuleCondition> payRuleConditions = JSON.parseArray(payMethodJson, RuleCondition.class);

        for (RuleCondition payRuleCondition : payRuleConditions) {
            payRuleCondition.setCreatedId(pointsRule.getCreatedId());
            payRuleCondition.setPointRuleId(pointsRule.getId());
            ruleConditionService.insertSelective(payRuleCondition);
        }
        return 0;
    }

    @Override
    public void startPointRule(PointsRule pointsRule) {
        getActualDao().updateByYn(pointsRule);
        pointsRule.setYn(1);
        this.updateSelective(pointsRule);
    }
}