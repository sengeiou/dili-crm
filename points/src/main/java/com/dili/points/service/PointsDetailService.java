package com.dili.points.service;

import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.dto.CustomerCategoryPointsDTO;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
public interface PointsDetailService extends BaseService<PointsDetail, Long> {
	public Optional<PointsDetailDTO> insert(PointsDetailDTO pointsDetail) ;
	int clear(String notes) ;
	int batchInsertPointsDetailDTO(List<PointsDetailDTO> pointsDetail) ;
	public int batchInsertPointsDetailDTO(Map<PointsDetailDTO,List<CustomerCategoryPointsDTO>> pointsDetailMap);
}