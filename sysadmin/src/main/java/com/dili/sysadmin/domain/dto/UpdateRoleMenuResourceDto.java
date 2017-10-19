package com.dili.sysadmin.domain.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public class UpdateRoleMenuResourceDto {

	@NotNull(message = "角色id不能为空")
	private Long roleId;
	private List<MenuResourceDto> menuResources;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<MenuResourceDto> getMenuResources() {
		return menuResources;
	}

	public void setMenuResources(List<MenuResourceDto> menuResources) {
		this.menuResources = menuResources;
	}

}
