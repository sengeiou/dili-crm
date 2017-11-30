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
 * This file was generated on 2017-11-21 17:27:41.
 */
@Component
public class VisitStateProvider implements ValueProvider {
    private static final List<ValuePair<?>> buffer;

    static {
        buffer = new ArrayList<ValuePair<?>>();
        buffer.add(new ValuePairImpl(EMPTY_ITEM_TEXT, null));
        buffer.add(new ValuePairImpl("新建", "1"));
        buffer.add(new ValuePairImpl("进行中", "2"));
        buffer.add(new ValuePairImpl("完成", "3"));
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