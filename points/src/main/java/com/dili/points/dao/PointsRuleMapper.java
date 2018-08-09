package com.dili.points.dao;

import com.dili.points.domain.PointsRule;
import com.dili.ss.base.MyMapper;

public interface PointsRuleMapper extends MyMapper<PointsRule> {

    void disable(PointsRule pointsRule);
}