package com.dili.alm.service;

import com.dili.alm.domain.Milestones;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-20 11:02:17.
 */
public interface MilestonesService extends BaseService<Milestones, Long> {

	/**
	 * 插入，返回异常信息，为空则成功
	 * @param milestones
	 * @return
	 */
	String insertSelectiveWithMsg(Milestones milestones);

	/**
	 * 修改，返回异常信息，为空则成功
	 * @param milestones
	 * @return
	 */
	String updateSelectiveWithMsg(Milestones milestones);
}