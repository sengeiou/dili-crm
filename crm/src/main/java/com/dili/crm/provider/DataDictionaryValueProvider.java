package com.dili.crm.provider;

import com.dili.ss.metadata.provider.SimpleValueProvider;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 11:11:59.
 */
public abstract class DataDictionaryValueProvider extends SimpleValueProvider {

    @Override
    public String getTable() {
        return "data_dictionary_value";
    }

    @Override
    public String getTextField() {
        return "code";
    }

    @Override
    public String getValueField() {
        return "value";
    }

    @Override
    public String getOrderByClause() {
        return "order_number asc";
    }

    @Override
    public Map<String, Object> getQueryParams() {
        Map<String, Object> params = new HashedMap();
        params.put("yn", 1);
        params.put("dd_id", getDdid());
        return params;
    }

    /**
     * 由子类实现数据字典id
     * @return
     */
    public abstract Long getDdid();
}