package com.dili.points.service;

import com.dili.points.domain.PointsRule;
import com.dili.points.domain.dto.PointsRuleDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
public interface PointsRuleService extends BaseService<PointsRule, Long> {

    void insertPointRule(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson);

    void updatePointRule(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson);

    void startPointRule(PointsRule pointsRule,int yn);
    void buildConditionParameter(Map map);
    
    /**
     * 
     * @param pointsRule 条件domain
     * @param useProvider 是否应用provider
     * @param firmCodes  用户数据权限firm
     * @return
     */
    public EasyuiPageOutput listEasyuiPageByExample(PointsRuleDTO pointsRule,boolean useProvider,List<String>firmCodes) throws Exception;
}