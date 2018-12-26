package com.dili.crm.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 证件类型提供者
 * 根据组织类型获取下拉数据
 */
@Component
@Scope("prototype")
public class CertificateTypeProvider extends DataDictionaryValueProvider {

    /**
     * 取列表的值
     * @param value 当前字段的值
     * @param paramMap 参数
     * @param fieldMeta
     * @return
     */
    @Override
    public List<ValuePair<?>> getLookupList(Object value, Map paramMap, FieldMeta fieldMeta){
        if (null == paramMap){
            paramMap = Maps.newHashMap();
        }
        if (!paramMap.containsKey(QUERY_PARAMS_KEY)){
            JSONObject json = new JSONObject();
            json.put(DD_CODE_KEY,getDdCode(null));
            paramMap.put(QUERY_PARAMS_KEY,json.toJSONString());
        }
        List<ValuePair<?>> valuePairs = super.getLookupList(value, paramMap, fieldMeta);
        Object organizationType = paramMap.get("organizationType");
        if(organizationType == null || "".equals(organizationType)){
            return valuePairs;
        }
        Iterator<ValuePair<?>> iterator = valuePairs.iterator();
        //个人
        if("individuals".equals(organizationType)){
            while(iterator.hasNext()){
                ValuePair<?> valuePair = iterator.next();
                //个人没有营业执照和统一社会信用代码
                if(valuePair.getValue() != null && ("businessLicense".equals(valuePair.getValue()) || "uscc".equals(valuePair.getValue()))) {
                    iterator.remove();
                }
            }
            return valuePairs;
        }
        //企业
        else if("enterprise".equals(organizationType)){//企业
            //企业只有营业执照和统一社会信用代码
            List<ValuePair<?>> enterpriseValuePairs = Lists.newArrayList();
            while(iterator.hasNext()){
                ValuePair<?> valuePair = iterator.next();
                if(valuePair.getValue().equals("businessLicense") || valuePair.getValue().equals("uscc")){
                    enterpriseValuePairs.add(new ValuePairImpl(valuePair.getText(), valuePair.getValue()));
                }
            }
            return enterpriseValuePairs;
        }
        //没有组织类型就取所有的证件类型
        return valuePairs;
    }

    @Override
    public String getDdCode(String queryParam){
        return "certificate_type";
    }
}