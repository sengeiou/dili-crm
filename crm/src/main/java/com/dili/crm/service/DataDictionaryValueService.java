package com.dili.crm.service;

import java.util.List;

import com.dili.crm.domain.DataDictionaryValue;
import com.dili.crm.domain.dto.DataDictionaryValueTreeView;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

public interface DataDictionaryValueService extends BaseService<DataDictionaryValue, Long> {

	BaseOutput<Object> insertAndGet(DataDictionaryValue dataDictionaryValue);

	List<DataDictionaryValueTreeView> listTree(Long ddId);
	
	String findChartServer();
}