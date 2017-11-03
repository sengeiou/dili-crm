package com.dili.alm.domain;

public enum TeamType {

	PRODUCT("产品", 1), DEVELOP("开发", 2), TEST("测试", 3), OPS("运维",4);

	private String name;
	private int code;

	private TeamType(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public int getCode() {
		return code;
	}

}
