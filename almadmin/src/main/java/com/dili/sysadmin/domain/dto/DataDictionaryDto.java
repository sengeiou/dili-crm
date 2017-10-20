package com.dili.sysadmin.domain.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class DataDictionaryDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2577822422167614099L;

	private Long id;

	private String code;

	private String name;

	private String remark;

	private List<DataDictionaryValueDto> values;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<DataDictionaryValueDto> getValues() {
		return values;
	}

	public void setValues(List<DataDictionaryValueDto> values) {
		this.values = values;
	}

	public DataDictionaryValueDto getValueByCode(String code) {
		if (CollectionUtils.isEmpty(this.getValues())) {
			return null;
		}
		for (DataDictionaryValueDto value : this.getValues()) {
			if (value.getCode().equals(code)) {
				return value;
			}
		}
		return null;
	}

}