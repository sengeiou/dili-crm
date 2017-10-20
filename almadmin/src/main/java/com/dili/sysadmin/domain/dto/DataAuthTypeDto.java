package com.dili.sysadmin.domain.dto;

import java.io.Serializable;

public class DataAuthTypeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2550259512439171549L;

	private String type;
	private String name;

	public DataAuthTypeDto() {
		super();
	}

	public DataAuthTypeDto(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
