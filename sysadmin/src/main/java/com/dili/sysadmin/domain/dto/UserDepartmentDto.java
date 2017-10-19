package com.dili.sysadmin.domain.dto;

import java.util.List;

import org.springframework.cglib.beans.BeanCopier;

import com.dili.sysadmin.domain.Department;
import com.dili.sysadmin.domain.User;

public class UserDepartmentDto extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 781633557170038317L;

	private static final BeanCopier COPIER = BeanCopier.create(User.class, UserDepartmentDto.class, false);

	private List<Department> departments;

	public UserDepartmentDto(User user) {
		COPIER.copy(user, this, null);
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

}
