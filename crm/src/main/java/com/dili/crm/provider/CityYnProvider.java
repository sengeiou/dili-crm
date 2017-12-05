package com.dili.crm.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 城市的yn提供者，城市的yn是tinyint
 * Created by asiamaster on 2017/5/31 0031.
 */
@Component
public class CityYnProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER;

    static {
        BUFFER = new ArrayList<ValuePair<?>>();
        BUFFER.add(new ValuePairImpl("可用", "true"));
        BUFFER.add(new ValuePairImpl("不可用", "false"));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || "".equals(obj)){
            return null;
        }
        for(ValuePair<?> valuePair : BUFFER){
            if(obj.toString().equals(valuePair.getValue())){
                return valuePair.getText();
            }
        }
        return null;
    }
}
