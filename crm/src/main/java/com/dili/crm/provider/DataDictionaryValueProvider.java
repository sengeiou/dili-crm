package com.dili.crm.provider;

import com.dili.ss.metadata.provider.SimpleValueProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典提供者
 */
@Component
@Scope("prototype")
public class DataDictionaryValueProvider extends SimpleValueProvider {

    //前台需要传入的参数
    protected static final String DD_ID_KEY = "dd_id";

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
    protected void buildParam(Map paramMap){
        super.buildParam(paramMap);
        Map<String, Object> params = new HashMap<>();
        params.put("yn", 1);
        params.put("dd_id", getDdId());
        setQueryParams(params);
    }

    /**
     * 获取数据字典id
     * @return
     */
    public String getDdId(){
        //清空缓存
        Object ddId = getQueryParams().get(DD_ID_KEY);
        if(ddId == null){
            throw new RuntimeException("dd_id属性为空");
        }
        return ddId.toString();
    }
}