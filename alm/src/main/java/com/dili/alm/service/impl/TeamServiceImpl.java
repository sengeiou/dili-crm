package com.dili.alm.service.impl;

import com.dili.alm.constant.AlmConstants;
import com.dili.alm.dao.TeamMapper;
import com.dili.alm.domain.Team;
import com.dili.alm.rpc.DataAuthRpc;
import com.dili.alm.service.TeamService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-24 14:31:10.
 */
@Service
public class TeamServiceImpl extends BaseServiceImpl<Team, Long> implements TeamService {

    @Autowired
    DataAuthRpc dataAuthRpc;

    public TeamMapper getActualDao() {
        return (TeamMapper)getDao();
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
        dataAuthRpc.deleteUserDataAuth(id, team.getProjectId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT);
        return super.delete(id);
    }

    @Override
    public int delete(List<Long> ids) {
        ids.forEach(id -> {
            dataAuthRpc.deleteUserDataAuth(id, get(id).getProjectId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT);
        });
        return super.delete(ids);
    }
}