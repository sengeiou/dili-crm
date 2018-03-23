package com.dili.points.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.points.constant.Constants;
import com.dili.points.dao.PointsRuleMapper;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.PointsRuleLog;
import com.dili.points.domain.RuleCondition;
import com.dili.points.service.PointsRuleLogService;
import com.dili.points.service.PointsRuleService;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class PointsRuleServiceImpl extends BaseServiceImpl<PointsRule, Long> implements PointsRuleService {

    @Autowired
    private RuleConditionService ruleConditionService;

    @Autowired
    private PointsRuleLogService pointsRuleLogService;


    public PointsRuleMapper getActualDao() {
        return (PointsRuleMapper) getDao();
    }

    @Override
    public int insertPointRule(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        pointsRule.setCreatedId(SessionContext.getSessionContext().getUserTicket().getId());
        pointsRule.setYn(0);
        getActualDao().insertSelective(pointsRule);
        savelog(pointsRule.getId());
        makeRuleCondition(pointsRule, numberJson, moneyJson, payMethodJson);
        return 0;
    }

    /**
     * 生成积分规则
     * @param pointsRule
     * @param numberJson
     * @param moneyJson
     * @param payMethodJson
     */
    private void makeRuleCondition(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        List<RuleCondition> numberRuleConditions = JSON.parseArray(numberJson, RuleCondition.class);
        makeRuleCondition(pointsRule, numberRuleConditions, Constants.WeightType.NUMBER.getCode());

        List<RuleCondition> moneyRuleConditions = JSON.parseArray(moneyJson, RuleCondition.class);

        makeRuleCondition(pointsRule, moneyRuleConditions, Constants.WeightType.MONEY.getCode());

        List<RuleCondition> payRuleConditions = JSON.parseArray(payMethodJson, RuleCondition.class);

        makeRuleCondition(pointsRule, payRuleConditions, Constants.WeightType.PAYMETHOD.getCode());
    }

    private void makeRuleCondition(PointsRule pointsRule, List<RuleCondition> numberRuleConditions, int weightTypeNumber) {
        for (RuleCondition numberRuleCondition : numberRuleConditions) {
            numberRuleCondition.setCreatedId(pointsRule.getCreatedId());
            numberRuleCondition.setPointRuleId(pointsRule.getId());
            numberRuleCondition.setWeightType(weightTypeNumber);
            ruleConditionService.insertSelective(numberRuleCondition);
        }
    }

    @Override
    public int updatePointRule(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        pointsRule.setModified(new Date());
        pointsRule.setModifiedId(SessionContext.getSessionContext().getUserTicket().getId());
        getActualDao().updateByPrimaryKeySelective(pointsRule);
        savelog(pointsRule.getId());
        ruleConditionService.deleteByRuleId(pointsRule.getId());
        makeRuleCondition(pointsRule, numberJson, moneyJson, payMethodJson);
        return 0;
    }

    @Override
    public void startPointRule(PointsRule pointsRule,int yn) {
        if(yn == 1) {
            getActualDao().updateByYn(pointsRule);
        }
        pointsRule.setYn(yn);
        pointsRule.setModified(new Date());
        pointsRule.setModifiedId(SessionContext.getSessionContext().getUserTicket().getId());
        this.updateSelective(pointsRule);
        savelog(pointsRule.getId());
    }

    private void savelog(Long ruleId){
        PointsRuleLog log = DTOUtils.newDTO(PointsRuleLog.class);
        log.setCreated(new Date());
        log.setCreatedId(SessionContext.getSessionContext().getUserTicket().getId());
        log.setPointsRuleId(ruleId);
        pointsRuleLogService.insertSelective(log);
    }
}