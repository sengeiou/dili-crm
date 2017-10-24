package com.dili.alm.service;

import java.util.List;

import com.dili.alm.domain.Project;
import com.dili.alm.domain.dto.DataDictionaryValueDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-18 17:22:54.
 */
public interface ProjectService extends BaseService<Project, Long> {

	List<DataDictionaryValueDto> getPojectTypes();

	BaseOutput<Object> deleteBeforeCheck(Long id);
}