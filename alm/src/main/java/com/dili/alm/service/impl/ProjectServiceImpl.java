package com.dili.alm.service.impl;

import com.dili.alm.cache.AlmCache;
import com.dili.alm.constant.AlmConstants;
import com.dili.alm.dao.MilestonesMapper;
import com.dili.alm.dao.ProjectMapper;
import com.dili.alm.dao.TeamMapper;
import com.dili.alm.domain.Milestones;
import com.dili.alm.domain.Project;
import com.dili.alm.domain.Team;
import com.dili.alm.domain.dto.DataDictionaryDto;
import com.dili.alm.domain.dto.DataDictionaryValueDto;
import com.dili.alm.domain.dto.ProjectDto;
import com.dili.alm.rpc.DataAuthRpc;
import com.dili.alm.rpc.DataDictionaryRPC;
import com.dili.alm.service.ProjectService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-10-18 17:22:54.
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project, Long> implements ProjectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
	private static final String PROJECT_TYPE_CODE = "project_type";

	@Autowired
	private DataDictionaryRPC dataDictionaryRPC;
	@Autowired
	private MilestonesMapper milestonesMapper;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	DataAuthRpc dataAuthRpc;

	public ProjectMapper getActualDao() {
		return (ProjectMapper) getDao();
	}

	@Override
	public int update(Project condtion) {
		// 同步更新缓存
		if (StringUtils.isNotBlank(condtion.getName())) {
			AlmCache.projectMap.get(condtion.getId()).setName(condtion.getName());
		}
		dataAuthRpc.updateDataAuth(condtion.getId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT, condtion.getName());
		return super.update(condtion);
	}

	@Override
	public int insert(Project project) {
		int i = super.insert(project);
		// 同步更新缓存
		AlmCache.projectMap.put(project.getId(), project);
		//向权限系统中添加项目数据权限
		dataAuthRpc.addDataAuth(project.getId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT, project.getName());
		return i;
	}

	@Override
	public int insertSelective(Project project) {
		int i = super.insertSelective(project);
		// 同步更新缓存
		AlmCache.projectMap.put(project.getId(), project);
		//向权限系统中添加项目数据权限
		dataAuthRpc.addDataAuth(project.getId().toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT, project.getName());
		return i;
	}

	@Override
	public int delete(Long id) {
		AlmCache.projectMap.remove(id);
		dataAuthRpc.deleteDataAuth(id.toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT);
		return super.delete(id);
	}

	@Override
	public int delete(List<Long> ids) {
		ids.forEach(id -> {
			AlmCache.projectMap.remove(id);
			dataAuthRpc.deleteDataAuth(id.toString(), AlmConstants.DATA_AUTH_TYPE_PROJECT);
		});
		return super.delete(ids);
	}

	@Override
	public List<DataDictionaryValueDto> getPojectTypes() {
		BaseOutput<DataDictionaryDto> output = this.dataDictionaryRPC.findDataDictionaryByCode(PROJECT_TYPE_CODE);
		if (!output.isSuccess()) {
			LOGGER.error(output.getResult());
			return null;
		}
		DataDictionaryDto dto = output.getData();
		if (dto == null) {
			return null;
		}
		return dto.getValues();
	}

	@Override
	public BaseOutput<Object> deleteBeforeCheck(Long projectId) {
		Milestones record = DTOUtils.newDTO(Milestones.class);
		record.setProjectId(projectId);
		int count = this.milestonesMapper.selectCount(record);
		if (count > 0) {
			return BaseOutput.failure("项目关联了里程碑，不能删除");
		}
		Team teamQuery = DTOUtils.newDTO(Team.class);
		teamQuery.setProjectId(projectId);
		count = this.teamMapper.selectCount(teamQuery);
		if (count > 0) {
			return BaseOutput.failure("项目关联了团队，不能删除");
		}
		Project projectQuery = DTOUtils.newDTO(Project.class);
		projectQuery.setParentId(projectId);
		count = this.getActualDao().selectCount(projectQuery);
		if (count > 0) {
			return BaseOutput.failure("项目包含子项目，不能删除");
		}
		count = this.getActualDao().deleteByPrimaryKey(projectId);
		if (count > 0) {
			return BaseOutput.success("删除成功");
		}
		return BaseOutput.failure("删除失败");
	}

	@Override
	public List<Project> getChildProjects(Long id) {
		return getActualDao().getChildProjects(id);
	}

	@Override
	public EasyuiPageOutput listEasyuiPageByExample(Project domain, boolean useProvider) throws Exception {
		SessionContext sessionContext = SessionContext.getSessionContext();
		if(sessionContext == null) {
			throw new RuntimeException("未登录");
		}
		List<Map> dataauth = sessionContext.dataAuth(AlmConstants.DATA_AUTH_TYPE_PROJECT);
		List<Long> projectIds = new ArrayList<>(dataauth.size());
		dataauth.forEach( t -> {
			List<Project> projects = getChildProjects(Long.parseLong(t.get("dataId").toString()));
			projects.forEach( p -> {
				projectIds.add(p.getId());
			});
//			projectIds.add(Long.parseLong(t.get("dataId").toString()));
		});
		ProjectDto projectDto = DTOUtils.as(domain, ProjectDto.class);
		if(projectIds.isEmpty()){
			return new EasyuiPageOutput(0, null);
		}
		projectDto.setIds(projectIds);
		return super.listEasyuiPageByExample(projectDto, useProvider);
	}
}