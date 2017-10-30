package com.dili.alm.service;

import com.dili.alm.domain.Team;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-10-24 14:31:10.
 */
public interface TeamService extends BaseService<Team, Long> {

	BaseOutput<Object> insertAfterCheck(Team team);

	BaseOutput<Object> updateAftreCheck(Team team);
}