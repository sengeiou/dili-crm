package com.dili.sysadmin.domain;

public enum UserStatus {

	ENABLE("启用", 1), DISABLE("禁用", 0);

	private String name;
	private int value;

	private UserStatus(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

}
