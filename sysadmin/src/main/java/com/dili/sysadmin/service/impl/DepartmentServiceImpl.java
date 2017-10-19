package com.dili.sysadmin.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.sysadmin.dao.DepartmentMapper;
import com.dili.sysadmin.dao.UserMapper;
import com.dili.sysadmin.domain.Department;
import com.dili.sysadmin.service.DepartmentService;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-09-07 09:48:51.
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Long> implements DepartmentService {

	@Autowired
	private UserMapper userMapper;

	public DepartmentMapper getActualDao() {
		return (DepartmentMapper) getDao();
	}

	@Transactional
	@Override
	public BaseOutput<Object> checkBeforeDelete(Long deptId) {
		Department record = new Department();
		record.setParentId(deptId);
		int count = this.getActualDao().selectCount(record);
		if (count > 0) {
			return BaseOutput.failure("该部门下包含子部门不能删除");
		}
		count = this.userMapper.countByDepartmentId(deptId);
		if (count > 0) {
			return BaseOutput.failure("部门下包含用户不能删除");
		}
		count = this.getActualDao().deleteByPrimaryKey(deptId);
		return count > 0 ? BaseOutput.success() : BaseOutput.failure();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public BaseOutput<Object> insertAfterCheck(Department department) {
		Department record = new Department();
		record.setName(department.getName());
		int count = this.getActualDao().selectCount(record);
		if (count > 0) {
			return BaseOutput.failure("存在相同名称的部门");
		}
		int result = this.getActualDao().insertSelective(department);
		if (result > 0) {
			return BaseOutput.success().setData(department);
		}
		return BaseOutput.failure("插入失败");
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public BaseOutput<Object> updateAfterCheck(Department department) {
		Department record = new Department();
		record.setName(department.getName());
		record = this.getActualDao().selectOne(record);
		if (record != null && !record.getId().equals(department.getId())) {
			return BaseOutput.failure("存在相同名称的部门");
		}
		record.setNotes(department.getNotes());
		record.setModified(new Date());
		int result = this.getActualDao().updateByPrimaryKey(record);
		if (result > 0) {
			return BaseOutput.success().setData(record);
		}
		return BaseOutput.failure("更新失败");
	}
}