package com.dili.sysadmin.domain.dto;

import java.util.List;

import com.dili.sysadmin.domain.Menu;
import com.dili.sysadmin.domain.Resource;

public class MenuDto extends Menu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8254821897645709926L;

	private List<Resource> resources;

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
}
