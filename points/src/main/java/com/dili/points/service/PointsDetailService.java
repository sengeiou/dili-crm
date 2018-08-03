package com.dili.points.service;

import java.util.List;
import java.util.Optional;

import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
public interface PointsDetailService extends BaseService<PointsDetail, Long> {
	Optional<PointsDetailDTO> insert(PointsDetailDTO pointsDetail) ;

	/**
	 * 按照市场编码，清除所有客户在该市场中的积分
	 * @param firmCode 市场编码
	 * @param notes 清除原因
	 * @return
	 */
	int clear(String firmCode,String notes);

	/**
	 * 
	 * @param pointsDetail 查询example
	 * @param useProvider 是否使用provider
	 * @param firmCodes 数据权限firmcode列表
	 * @return
	 * @throws Exception
	 */
	public EasyuiPageOutput listEasyuiPageByExample(PointsDetailDTO pointsDetail,boolean useProvider,List<String>firmCodes) throws Exception;
	

}