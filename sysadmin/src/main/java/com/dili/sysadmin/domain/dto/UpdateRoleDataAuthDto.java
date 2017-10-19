package com.dili.sysadmin.domain.dto;

import java.util.List;

public class UpdateRoleDataAuthDto {

	private Long roleId;
	private String type;
	private List<Long> dataAuthIds;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Long> getDataAuthIds() {
		return dataAuthIds;
	}

	public void setDataAuthIds(List<Long> dataAuthIds) {
		this.dataAuthIds = dataAuthIds;
	}

}
