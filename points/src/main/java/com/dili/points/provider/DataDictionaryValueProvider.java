package com.dili.points.provider;

import com.alibaba.fastjson.JSONObject;
import com.dili.points.domain.DataDictionaryValue;
import com.dili.points.rpc.DataDictionaryValueRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 远程数据字典批量提供者
 */
@Component
@Scope("prototype")
public class DataDictionaryValueProvider extends BatchDisplayTextProviderAdaptor {

    //前台需要传入的参数
    protected static final String DD_ID_KEY = "dd_id";
    @Autowired
    private DataDictionaryValueRpc dataDictionaryValueRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
        if(queryParams == null) {
            return Lists.newArrayList();
        }
        Long ddId = JSONObject.parseObject(queryParams.toString()).getLong(DD_ID_KEY);
        BaseOutput<List<DataDictionaryValue>> output = dataDictionaryValueRpc.listByDdId(ddId);
        if(!output.isSuccess()){
            return null;
        }
        List<ValuePair<?>> valuePairs = Lists.newArrayList();
        valuePairs.add(0, new ValuePairImpl(EMPTY_ITEM_TEXT, null));
        List<DataDictionaryValue> dataDictionaryValues = output.getData();
        for(int i=0; i<dataDictionaryValues.size(); i++) {
            DataDictionaryValue dataDictionaryValue = dataDictionaryValues.get(i);
            valuePairs.add(i+1, new ValuePairImpl(dataDictionaryValue.getCode(), dataDictionaryValue.getValue()));
        }
        return valuePairs;
    }

    @Override
    protected List getFkList(List<String> ddvIds, Map metaMap) {
        Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
        if(queryParams == null) {
            return Lists.newArrayList();
        }
        Long ddId = JSONObject.parseObject(queryParams.toString()).getLong(DD_ID_KEY);
        BaseOutput<List<DataDictionaryValue>> userOutput = dataDictionaryValueRpc.listByDdId(ddId);
        return userOutput.isSuccess() ? userOutput.getData() : null;
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
            return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "code");
            return map;
        }
    }

    /**
     * 关联(数据库)表的主键的字段名
     * 默认取id，子类可自行实现
     * @return
     */
    @Override
    protected String getRelationTablePkField(Map metaMap) {
        return "value";
    }
}