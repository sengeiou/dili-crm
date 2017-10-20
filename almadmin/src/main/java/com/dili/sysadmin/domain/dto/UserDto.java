package com.dili.sysadmin.domain.dto;

import java.util.List;

import org.springframework.cglib.beans.BeanCopier;

import com.dili.sysadmin.domain.DataAuth;
import com.dili.sysadmin.domain.Role;
import com.dili.sysadmin.domain.User;

public class UserDto extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8150815057297031411L;
	private List<Role> roles;
	private List<DataAuth> dataAuths;

	public UserDto() {
	}

	public UserDto(User user) {
		BeanCopier copier = BeanCopier.create(user.getClass(), this.getClass(), false);
		copier.copy(user, this, null);
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<DataAuth> getDataAuths() {
		return dataAuths;
	}

	public void setDataAuths(List<DataAuth> dataAuths) {
		this.dataAuths = dataAuths;
	}

}
