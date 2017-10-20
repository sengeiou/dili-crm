package com.dili.sysadmin.domain.dto;

import java.util.List;

import org.springframework.cglib.beans.BeanCopier;

import com.dili.sysadmin.domain.Resource;
import com.dili.sysadmin.domain.Role;
import com.dili.sysadmin.domain.User;

public class RoleDto extends Role {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1097383745946084129L;

	public RoleDto() {
		super();
	}

	public RoleDto(Role entity) {
		BeanCopier copier = BeanCopier.create(entity.getClass(), this.getClass(), false);
		copier.copy(entity, this, null);
	}

	private List<User> users;
	private List<Resource> resources;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

}
