package com.dili.sysadmin.domain.dto;

import java.io.Serializable;
import java.util.List;

public class DataAuthDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5723361473436406872L;

	private Long id;
	private Long parentId;
	private String name;
	private List<DataAuthDto> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataAuthDto> getChildren() {
		return children;
	}

	public void setChildren(List<DataAuthDto> children) {
		this.children = children;
	}

}
