package com.dili.sysadmin.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.sysadmin.domain.Department;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-09-07 09:48:51.
 */
public interface DepartmentService extends BaseService<Department, Long> {

	BaseOutput<Object> checkBeforeDelete(Long id);

	BaseOutput<Object> insertAfterCheck(Department department);

	BaseOutput<Object> updateAfterCheck(Department department);
}