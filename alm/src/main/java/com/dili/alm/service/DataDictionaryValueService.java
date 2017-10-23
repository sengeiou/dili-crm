package com.dili.alm.service;

import java.util.List;

import com.dili.alm.domain.DataDictionaryValue;
import com.dili.alm.domain.dto.DataDictionaryValueTreeView;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * ��MyBatis Generator�����Զ�����
 * This file was generated on 2017-07-12 10:41:19.
 */
public interface DataDictionaryValueService extends BaseService<DataDictionaryValue, Long> {

	BaseOutput<Object> insertAndGet(DataDictionaryValue dataDictionaryValue);

	List<DataDictionaryValueTreeView> listTree(Long ddId);
}