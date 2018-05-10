package com.dili.points.service;

import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
public interface PointsDetailService extends BaseService<PointsDetail, Long> {
	int insert(PointsDetailDTO pointsDetail) ;
	int clear(String notes) ;
	int batchInsertPointsDetailDTO(List<PointsDetailDTO> pointsDetail) ;
}