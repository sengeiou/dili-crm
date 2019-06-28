package com.dili.points.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.points.constant.PointsConstants;
import com.dili.points.dao.PointsRuleMapper;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.PointsRuleLog;
import com.dili.points.domain.RuleCondition;
import com.dili.points.domain.dto.PointsRuleDTO;
import com.dili.points.service.DataDictionaryValueRpcService;
import com.dili.points.service.PointsRuleLogService;
import com.dili.points.service.PointsRuleService;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private DataDictionaryValueRpcService dataDictionaryValueRpcService;

    public PointsRuleMapper getActualDao() {
        return (PointsRuleMapper) getDao();
    }

    @Override
    public void insertPointRule(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        pointsRule.setCreatedId(SessionContext.getSessionContext().getUserTicket().getId());
        pointsRule.setYn(0);
        pointsRule.setModifiedId(pointsRule.getCreatedId());
        pointsRule.setModified(new Date());
        getActualDao().insertSelective(pointsRule);
        savelog(pointsRule.getId());
        makeRuleCondition(pointsRule, numberJson, moneyJson, payMethodJson);
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
        makeRuleCondition(pointsRule, numberRuleConditions, PointsConstants.WeightType.NUMBER.getCode());

        List<RuleCondition> moneyRuleConditions = JSON.parseArray(moneyJson, RuleCondition.class);

        makeRuleCondition(pointsRule, moneyRuleConditions, PointsConstants.WeightType.MONEY.getCode());

        List<RuleCondition> payRuleConditions = JSON.parseArray(payMethodJson, RuleCondition.class);

        makeRuleCondition(pointsRule, payRuleConditions, PointsConstants.WeightType.PAYMETHOD.getCode());
    }

    private void makeRuleCondition(PointsRule pointsRule, List<RuleCondition> numberRuleConditions, int weightTypeNumber) {
        for (RuleCondition numberRuleCondition : numberRuleConditions) {
            numberRuleCondition.setCreatedId(SessionContext.getSessionContext().getUserTicket().getId());
            numberRuleCondition.setModifiedId(SessionContext.getSessionContext().getUserTicket().getId());
            numberRuleCondition.setPointRuleId(pointsRule.getId());
            numberRuleCondition.setWeightType(weightTypeNumber);

            // fix id
            if (numberRuleCondition.getId() != null) {
                numberRuleCondition.setId(null);
            }

            ruleConditionService.insertSelective(numberRuleCondition);
        }
    }

    @Override
    public void updatePointRule(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        pointsRule.setModified(new Date());
        pointsRule.setModifiedId(SessionContext.getSessionContext().getUserTicket().getId());
        getActualDao().updateByPrimaryKeySelective(pointsRule);
        savelog(pointsRule.getId());
        ruleConditionService.deleteByRuleId(pointsRule.getId());
        makeRuleCondition(pointsRule, numberJson, moneyJson, payMethodJson);
    }

    @Override
    public void startPointRule(PointsRule pointsRule,int yn) {
        if(yn == 1) {
            getActualDao().disable(pointsRule);
        }
        pointsRule.setYn(yn);
        pointsRule.setModified(new Date());
        pointsRule.setModifiedId(SessionContext.getSessionContext().getUserTicket().getId());
        this.updateSelective(pointsRule);
        savelog(pointsRule.getId());
    }

    @Override
    public void buildConditionParameter(Map map) {
        List<DataDictionaryValue> list = dataDictionaryValueRpcService.listByDdCode("condition_type");
        map.put("conditionType", JSON.toJSONString(list));
        map.put("payMethod", JSON.toJSONString(dataDictionaryValueRpcService.listByDdCode("payment")));
    }
    @Override
    public EasyuiPageOutput listEasyuiPageByExample(PointsRuleDTO pointsRule,boolean useProvider,List<String>firmCodes) throws Exception {
    	
    	//如果用户数据权限集全为空，则返回空结果集(不再进行数据库查询)
    	if(firmCodes.isEmpty()) {
    		return new EasyuiPageOutput(0,Collections.emptyList()); 
    	}
    	pointsRule.setFirmCodes(firmCodes);
        return super.listEasyuiPageByExample(pointsRule, useProvider);
    }
    /**
     * 生成操作日志
     * @param ruleId
     */
    private void savelog(Long ruleId){
        PointsRuleLog log = DTOUtils.newDTO(PointsRuleLog.class);
        log.setCreated(new Date());
        log.setCreatedId(SessionContext.getSessionContext().getUserTicket().getId());
        log.setPointsRuleId(ruleId);
        pointsRuleLogService.insertSelective(log);
    }
}