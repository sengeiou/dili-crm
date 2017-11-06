package com.dili.sysadmin.domain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateMenuDto {

	@NotNull(message = "菜单id不能为空")
	private Long id;
	@NotBlank(message = "菜单名称不能为空")
	@Size(min = 1, max = 50, message = "菜单长度必须1-50个字符")
	private String name;
	@NotNull(message = "菜单类型不能为空")
	private Integer type;
	@NotBlank(message = "菜单链接地址不能为空")
	@Pattern(regexp = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\\\\\/])+$", message = "链接地址格式不正确")
	private String menuUrl;
	private String description;
	@NotNull(message = "排序不能为空")
	private Integer orderNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

}
