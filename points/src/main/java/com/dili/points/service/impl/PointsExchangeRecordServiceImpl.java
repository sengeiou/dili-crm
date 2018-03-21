package com.dili.points.service.impl;

import com.dili.points.dao.PointsExchangeRecordMapper;
import com.dili.points.domain.PointsExchangeRecord;
import com.dili.points.service.PointsExchangeRecordService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-21 17:06:49.
 */
@Service
public class PointsExchangeRecordServiceImpl extends BaseServiceImpl<PointsExchangeRecord, Long> implements PointsExchangeRecordService {

    public PointsExchangeRecordMapper getActualDao() {
        return (PointsExchangeRecordMapper)getDao();
    }
}