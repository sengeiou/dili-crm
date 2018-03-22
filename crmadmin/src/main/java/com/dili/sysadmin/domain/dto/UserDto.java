package com.dili.sysadmin.domain.dto;

import java.util.List;

import com.dili.ss.domain.annotation.Operator;
import org.springframework.cglib.beans.BeanCopier;

import com.dili.sysadmin.domain.DataAuth;
import com.dili.sysadmin.domain.Role;
import com.dili.sysadmin.domain.User;

import javax.persistence.Column;
import javax.persistence.Transient;

public class UserDto extends User {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -8150815057297031411L;
	@Operator(Operator.IN)
	@Column(name = "`id`")
	private List<Long> ids;
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

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
