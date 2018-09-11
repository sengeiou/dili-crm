package com.dili.dp.dao;

import com.dili.dp.domain.PointsExchangeRecord;
import com.dili.ss.base.MyMapper;

import java.math.BigDecimal;
import java.util.Map;

public interface PointsExchangeRecordMapper extends MyMapper<PointsExchangeRecord> {

    /**
     * 统计总积分和总兑换数
     * @param pointsExchangeRecord
     * @return
     */
    Map<Object,BigDecimal> statistics(PointsExchangeRecord pointsExchangeRecord);
}