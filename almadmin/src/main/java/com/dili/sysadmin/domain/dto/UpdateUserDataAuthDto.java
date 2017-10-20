package com.dili.sysadmin.domain.dto;

import java.util.List;

public class UpdateUserDataAuthDto {

	private Long userId;
	private String type;
	private List<Long> dataAuthIds;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
