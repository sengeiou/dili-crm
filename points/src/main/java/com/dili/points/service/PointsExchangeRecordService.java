package com.dili.points.service;

import com.dili.points.domain.ExchangeCommodities;
import com.dili.points.domain.PointsExchangeRecord;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-21 17:06:49.
 */
public interface PointsExchangeRecordService extends BaseService<PointsExchangeRecord, Long> {

    /**
     * 新增商品兑换记录
     *
     * @param pointsExchangeRecord  兑换记录信息
     * @return
     */
    BaseOutput insertSelectiveWithOutput(PointsExchangeRecord pointsExchangeRecord) throws Exception;
}