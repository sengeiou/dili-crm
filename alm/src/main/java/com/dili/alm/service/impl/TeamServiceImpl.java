package com.dili.alm.service.impl;

import com.dili.alm.constant.AlmConstants;
import com.dili.alm.constant.AlmConstants.MemberState;
import com.dili.alm.dao.TeamMapper;
import com.dili.alm.domain.Team;
import com.dili.alm.rpc.DataAuthRpc;
import com.dili.alm.service.TeamService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-10-24 14:31:10.
 */
@Service
public class TeamServiceImpl extends BaseServiceImpl<Team, Long> implements TeamService {

	@Autowired
	DataAuthRpc dataAuthRpc;

	public TeamMapper getActualDao() {
		return (TeamMapper) getDao();
	}

	@Override
	public int insert(Team team) {
		int i = super.insert(team);
		dataAuthRpc.addUserDataAuth(team.getMemberId(), team.getProjectId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT);
		return i;
	}

	@Override
	public int delete(Long id) {
		Team team = get(id);
		dataAuthRpc.deleteUserDataAuth(team.getMemberId(), team.getProjectId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT);
		return super.delete(id);
	}

	@Override
	public int delete(List<Long> ids) {
		ids.forEach(id -> {
			dataAuthRpc.deleteUserDataAuth(id, get(id).getProjectId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT);
		});
		return super.delete(ids);
	}

	@Override
	public int insertSelective(Team t) {
		Date now = new Date();
		t.setJoinTime(now);
		if (t.getMemberState().equals(MemberState.LEAVE.getCode())) {
			t.setLeaveTime(now);
		}
		dataAuthRpc.addUserDataAuth(t.getMemberId(), t.getProjectId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT);
		return super.insertSelective(t);
	}

	@Override
	public int updateSelective(Team team) {
		Team old = this.getActualDao().selectByPrimaryKey(team.getId());
		if (old.getMemberState().equals(team.getMemberState())) {
			return super.updateSelective(team);
		}
		Date now = new Date();
		if (team.getMemberState().equals(MemberState.LEAVE.getCode())) {
			team.setLeaveTime(now);
		} else if (team.getMemberState().equals(MemberState.JOIN.getCode())) {
			team.setJoinTime(now);
		}
		return super.updateSelective(team);
	}

	@Override
	public BaseOutput<Object> insertAfterCheck(Team team) {
		Team record = DTOUtils.newDTO(Team.class);
		record.setProjectId(team.getProjectId());
		record.setMemberId(team.getMemberId());
		int count = this.getActualDao().selectCount(record);
		if (count > 0) {
			return BaseOutput.failure("项目和团队已存在，不能重复添加");
		}
		int result = this.insertSelective(team);
		if (result > 0) {
			return BaseOutput.success().setData(team);
		}
		return BaseOutput.failure("新增失败");
	}

	@Override
	public BaseOutput<Object> updateAftreCheck(Team team) {
		int result;
		Team oldRecord = this.getActualDao().selectByPrimaryKey(team.getId());
		if (oldRecord.getProjectId().equals(team.getProjectId()) && oldRecord.getMemberId().equals(team.getMemberId())) {
			result = this.updateSelective(team);
		} else {
			Team record = DTOUtils.newDTO(Team.class);
			record.setProjectId(team.getProjectId());
			record.setMemberId(team.getMemberId());
			int count = this.getActualDao().selectCount(record);
			if (count > 0) {
				return BaseOutput.failure("项目和团队已存在，不能重复添加");
			}
			result = this.updateSelective(team);
		}
		if (result > 0) {
			return BaseOutput.success().setData(team);
		}
		return BaseOutput.failure("修改失败");
	}

}