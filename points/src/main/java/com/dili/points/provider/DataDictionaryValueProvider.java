package com.dili.points.provider;

import com.alibaba.fastjson.JSONObject;
import com.dili.points.rpc.DataDictionaryRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 远程数据字典批量提供者
 */
@Component
@Scope("prototype")
public class DataDictionaryValueProvider extends BatchDisplayTextProviderAdaptor {

    //前台需要传入的参数
    protected static final String DD_CODE_KEY = "dd_code";
    //根据编码指定获取下拉列表项
    protected static final String INCLUDE_CODES_KEY = "include_codes";
    //根据编码排除获取下拉列表项
    protected static final String EXCLUDE_CODES_KEY = "exclude_codes";

    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
        if(queryParams == null) {
            return Lists.newArrayList();
        }
        String ddCode = getDdCode(queryParams.toString());
        DataDictionaryValue dataDictionaryValue = DTOUtils.newDTO(DataDictionaryValue.class);
        dataDictionaryValue.setDdCode(ddCode);
        //远程到UAP查询数据字典值
        BaseOutput<List<DataDictionaryValue>> output = dataDictionaryRpc.list(dataDictionaryValue);
        if(!output.isSuccess()){
            return null;
        }
        List<ValuePair<?>> valuePairs = Lists.newArrayList();
        List<DataDictionaryValue> dataDictionaryValues = output.getData();
        String includeCodes = JSONObject.parseObject(queryParams.toString()).getString(INCLUDE_CODES_KEY);
        String excludeCodes = JSONObject.parseObject(queryParams.toString()).getString(EXCLUDE_CODES_KEY);
        if(StringUtils.isNotBlank(includeCodes)){
            List<String> includeCodeList = null;
            includeCodeList = Arrays.asList(includeCodes.trim().split(","));
            //去掉每个项两边的空格
            includeCodeList = includeCodeList.stream().map(t -> t.trim()).collect(Collectors.toList());
            for(int i=0; i<dataDictionaryValues.size(); i++) {
                DataDictionaryValue dataDictionaryValue1 = dataDictionaryValues.get(i);
                //添加指定编码项
                if(includeCodeList.contains(dataDictionaryValue1.getCode())){
                    valuePairs.add(i, new ValuePairImpl(dataDictionaryValue1.getName(), dataDictionaryValue1.getCode()));
                }
            }
        }else {
            List<String> excludeCodeList = null;
            if(StringUtils.isNotBlank(excludeCodes)){
                excludeCodeList = Arrays.asList(excludeCodes.trim().split(","));
                //去掉每个项两边的空格
                excludeCodeList = excludeCodeList.stream().map(t -> t.trim()).collect(Collectors.toList());
            }
            for(int i=0; i<dataDictionaryValues.size(); i++) {
                DataDictionaryValue dataDictionaryValue1 = dataDictionaryValues.get(i);
                //排除指定编码项
                if(excludeCodeList == null || (excludeCodeList != null && !excludeCodeList.contains(dataDictionaryValue1.getCode()))){
                    valuePairs.add(i, new ValuePairImpl(dataDictionaryValue1.getName(), dataDictionaryValue1.getCode()));
                }
            }
        }
        return valuePairs;
    }

    @Override
    protected List getFkList(List<String> ddvIds, Map metaMap) {
        Object queryParams = metaMap.get(QUERY_PARAMS_KEY);
        if(queryParams == null) {
            return Lists.newArrayList();
        }
        String ddCode = getDdCode(queryParams.toString());
        DataDictionaryValue dataDictionaryValue = DTOUtils.newDTO(DataDictionaryValue.class);
        dataDictionaryValue.setDdCode(ddCode);
        BaseOutput<List<DataDictionaryValue>> output = dataDictionaryRpc.list(dataDictionaryValue);
        return output.isSuccess() ? output.getData() : null;
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
            return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "name");
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
        return "code";
    }

    /**
     * 获取数据字典编码
     * @return
     */
    private String getDdCode(String queryParams){
        String ddCode = JSONObject.parseObject(queryParams).getString(DD_CODE_KEY);
        if(ddCode == null){
            throw new RuntimeException("dd_code属性为空");
        }
        return ddCode;
    }

    @Override
    protected boolean ignoreCaseToRef(){
        return true;
    }
}