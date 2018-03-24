package com.dili.points.service;

import java.util.List;

import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
public interface PointsDetailService extends BaseService<PointsDetail, Long> {
	public int insert(PointsDetailDTO pointsDetail) ;
	public int batchInsertPointsDetailDTO(List<PointsDetailDTO> pointsDetail) ;
}