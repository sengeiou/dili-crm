package com.dili.sysadmin.domain.dto;

import java.util.List;

public class EditUserDataAuthDto {

	private Long userId;
	private List<DataAuthEditDto> allDataAuths;
	private List<DataAuthEditDto> userDataAuths;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<DataAuthEditDto> getAllDataAuths() {
		return allDataAuths;
	}

	public void setAllDataAuths(List<DataAuthEditDto> allDataAuths) {
		this.allDataAuths = allDataAuths;
	}

	public List<DataAuthEditDto> getUserDataAuths() {
		return userDataAuths;
	}

	public void setUserDataAuths(List<DataAuthEditDto> userDataAuths) {
		this.userDataAuths = userDataAuths;
	}

}
