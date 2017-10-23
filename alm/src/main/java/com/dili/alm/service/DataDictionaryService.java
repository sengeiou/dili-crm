package com.dili.alm.service;

import com.dili.alm.domain.DataDictionary;
import com.dili.alm.domain.dto.DataDictionaryDto;
import com.dili.ss.base.BaseService;

/**
 * ��MyBatis Generator�����Զ�����
 * This file was generated on 2017-07-12 10:41:18.
 */
public interface DataDictionaryService extends BaseService<DataDictionary, Long> {

	DataDictionaryDto findByCode(String code);
}