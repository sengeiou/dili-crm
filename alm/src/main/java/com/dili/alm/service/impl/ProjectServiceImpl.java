package com.dili.alm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.alm.cache.AlmCache;
import com.dili.alm.dao.ProjectMapper;
import com.dili.alm.domain.Project;
import com.dili.alm.domain.dto.DataDictionaryDto;
import com.dili.alm.domain.dto.DataDictionaryValueDto;
import com.dili.alm.rpc.DataDictionaryRPC;
import com.dili.alm.service.ProjectService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-10-18 17:22:54.
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project, Long> implements ProjectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
	private static final String PROJECT_TYPE_CODE = "project_type";

	@Autowired
	private DataDictionaryRPC dataDictionaryRPC;

	public ProjectMapper getActualDao() {
		return (ProjectMapper) getDao();
	}

	@Override
	public int update(Project condtion) {
		// 同步更新缓存
		if (StringUtils.isNotBlank(condtion.getName())) {
			AlmCache.projectMap.get(condtion.getId()).setName(condtion.getName());
		}
		return super.update(condtion);
	}

	@Override
	public int insert(Project project) {
		int i = super.insert(project);
		// 同步更新缓存
		AlmCache.projectMap.put(project.getId(), project);
		return i;
	}

	@Override
	public int delete(Long aLong) {
		AlmCache.projectMap.remove(aLong);
		return super.delete(aLong);
	}

	@Override
	public int delete(List<Long> ids) {
		ids.forEach(id -> {
			AlmCache.projectMap.remove(id);
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
}