package com.dili.dp.service;

import com.dili.dp.domain.PointsDetail;
import com.dili.dp.domain.dto.PointsDetailDTO;
import com.dili.ss.base.BaseService;

import java.util.Optional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
public interface PointsDetailService extends BaseService<PointsDetail, Long> {
	Optional<PointsDetailDTO> insert(PointsDetailDTO pointsDetail) ;

	

}