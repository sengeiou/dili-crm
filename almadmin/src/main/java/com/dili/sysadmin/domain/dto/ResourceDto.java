package com.dili.sysadmin.domain.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class ResourceDto {

	private Long id;
	@NotBlank(message = "资源名称不能为空")
	@Size(min = 1, max = 50, message = "资源名称长度必须是1-50个字符")
	private String name;
	@NotBlank(message = "资源代码不能为空")
	@Size(min = 1, max = 20, message = "资源代码长度必须是1-50个字符")
	private String code;
	private String description;
	private Long menuId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
