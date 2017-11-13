package com.dili.crm.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 11:11:59.
 */
@Component
public class OrganizationTypeProvider implements ValueProvider {
    private static final List<ValuePair<?>> buffer;

    static {
        buffer = new ArrayList<ValuePair<?>>();
        buffer.add(new ValuePairImpl("个人", "individuals"));
        buffer.add(new ValuePairImpl("企业", "enterprise"));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || obj.equals("")) return null;
        for(ValuePair<?> valuePair : buffer){
            if(obj.toString().equals(valuePair.getValue())){
                return valuePair.getText();
            }
        }
        return null;
    }
}