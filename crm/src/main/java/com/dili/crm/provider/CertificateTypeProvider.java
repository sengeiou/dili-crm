package com.dili.crm.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
        List<ValuePair<?>> valuePairs = super.getLookupList(value, paramMap, fieldMeta);
        Object organizationType = paramMap.get("organizationType");
        Iterator<ValuePair<?>> iterator = valuePairs.iterator();
        while(iterator.hasNext()){
            ValuePair<?> valuePair = iterator.next();
            //个人
            if(organizationType.equals("individuals")){
                //个人没有营业执照
                if(valuePair.getValue() != null && valuePair.getValue().equals("businessLicense")) {
                    iterator.remove();
                }
            }else if(organizationType.equals("enterprise")){//企业
                //企业只有营业执照
                if(valuePair.getValue() != null && !valuePair.getValue().equals("businessLicense")) {
                    iterator.remove();
                }
            }
            //没有组织类型就取所有的证件类型
        }
        return valuePairs;
    }

    @Override
    public String getDdId(){
        return "3";
    }
}