package com.dili.points.service.impl;

import com.dili.points.dao.PointsDetailMapper;
import com.dili.points.domain.PointsDetail;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class PointsDetailServiceImpl extends BaseServiceImpl<PointsDetail, Long> implements PointsDetailService {

    public PointsDetailMapper getActualDao() {
        return (PointsDetailMapper)getDao();
    }
}