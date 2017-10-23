package com.dili.alm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.alm.dao.DataDictionaryMapper;
import com.dili.alm.dao.DataDictionaryValueMapper;
import com.dili.alm.domain.DataDictionary;
import com.dili.alm.domain.DataDictionaryValue;
import com.dili.alm.domain.dto.DataDictionaryDto;
import com.dili.alm.domain.dto.DataDictionaryValueDto;
import com.dili.alm.service.DataDictionaryService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;

/**
 *
 * 10:41:18.
 */
@Service
public class DataDictionaryServiceImpl extends BaseServiceImpl<DataDictionary, Long> implements DataDictionaryService {

	@Autowired
	private DataDictionaryValueMapper valueMapper;

	public DataDictionaryMapper getActualDao() {
		return (DataDictionaryMapper) getDao();
	}

	@Override
	public DataDictionaryDto findByCode(String code) {
		DataDictionary record = DTOUtils.newDTO(DataDictionary.class);
		record.setCode(code);
		DataDictionary model = this.getActualDao().selectOne(record);
		if (model == null) {
			return null;
		}
		DataDictionaryValue valueRecord = DTOUtils.newDTO(DataDictionaryValue.class);
		valueRecord.setDdId(model.getId());
		List<DataDictionaryValue> values = this.valueMapper.select(valueRecord);
		DataDictionaryDto dto = DTOUtils.cast(model, DataDictionaryDto.class);
		if (CollectionUtils.isNotEmpty(values)) {
			List<DataDictionaryValueDto> dtos = new ArrayList<>(values.size());
			for (DataDictionaryValue value : values) {
				DataDictionaryValueDto valueDto = DTOUtils.cast(value, DataDictionaryValueDto.class);
				dtos.add(valueDto);
			}
			dto.setValues(dtos);
		}
		return dto;
	}

}