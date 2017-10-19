package com.dili.alm.service.impl;

import com.dili.alm.dao.ProjectMapper;
import com.dili.alm.domain.Project;
import com.dili.alm.service.ProjectService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-18 17:22:54.
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project, Long> implements ProjectService {

    public ProjectMapper getActualDao() {
        return (ProjectMapper)getDao();
    }
}