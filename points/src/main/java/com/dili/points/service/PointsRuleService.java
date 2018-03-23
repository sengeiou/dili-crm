package com.dili.points.service;

import com.dili.points.domain.PointsRule;
import com.dili.ss.base.BaseService;

import java.awt.*;
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
}