package com.dili.dp.service;

import com.dili.uap.sdk.domain.DataDictionaryValue;

import java.util.List;

/**
 * Created by asiam on 2018/7/10 0010.
 */
public interface DataDictionaryValueRpcService {

    /**
     * 根据字典编码查询字典值
     * @param ddCode
     * @return
     */
    List<DataDictionaryValue> listByDdCode(String ddCode);
}
