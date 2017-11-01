package com.dili.alm.service;

import com.dili.alm.domain.Milestones;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-20 11:02:17.
 */
public interface MilestonesService extends BaseService<Milestones, Long> {

	/**
	 * @param milestones
	 * @return
	 */
	BaseOutput insertSelectiveWithOutput(Milestones milestones);

	/**
	 * @param milestones
	 * @return
	 */
	BaseOutput updateSelectiveWithOutput(Milestones milestones);

	/**
	 *
	 * @param id
	 * @return
	 */
	BaseOutput deleteWithOutput(Long id);
}