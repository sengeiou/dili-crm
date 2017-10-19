package com.dili.sysadmin.domain.dto;

import java.io.Serializable;
import java.util.List;

public class EditRoleMenuAndResouceDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5296968964782849224L;

	private List<MenuResourceDto> allMenus;
	private List<MenuResourceDto> roleMenus;

	public List<MenuResourceDto> getAllMenus() {
		return allMenus;
	}

	public void setAllMenus(List<MenuResourceDto> allMenus) {
		this.allMenus = allMenus;
	}

	public List<MenuResourceDto> getRoleMenus() {
		return roleMenus;
	}

	public void setRoleMenus(List<MenuResourceDto> roleMenus) {
		this.roleMenus = roleMenus;
	}

}
