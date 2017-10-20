package com.dili.sysadmin.domain.dto;

import java.util.List;

public class EditRoleDataAuthDto {

	private Long roleId;
	private List<DataAuthEditDto> allDataAuths;
	private List<DataAuthEditDto> roleDataAuths;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<DataAuthEditDto> getAllDataAuths() {
		return allDataAuths;
	}

	public void setAllDataAuths(List<DataAuthEditDto> allDataAuths) {
		this.allDataAuths = allDataAuths;
	}

	public List<DataAuthEditDto> getRoleDataAuths() {
		return roleDataAuths;
	}

	public void setRoleDataAuths(List<DataAuthEditDto> roleDataAuths) {
		this.roleDataAuths = roleDataAuths;
	}

}
