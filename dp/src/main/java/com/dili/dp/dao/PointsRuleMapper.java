package com.dili.dp.dao;

import com.dili.dp.domain.PointsRule;
import com.dili.ss.base.MyMapper;

public interface PointsRuleMapper extends MyMapper<PointsRule> {

    void disable(PointsRule pointsRule);
}