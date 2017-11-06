package com.dili.crm.domain.dto;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.dili.crm.domain.DataDictionary;

public interface DataDictionaryDto extends DataDictionary {

	public List<DataDictionaryValueDto> getValues();

	public void setValues(List<DataDictionaryValueDto> values);

	public default DataDictionaryValueDto getValueByCode(String code) {
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