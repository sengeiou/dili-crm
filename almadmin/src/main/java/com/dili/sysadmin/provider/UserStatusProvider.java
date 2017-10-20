package com.dili.sysadmin.provider;

import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserStatusProvider implements ValueProvider {
    private static final List<ValuePair<?>> buffer;

    static {
        buffer = new ArrayList<ValuePair<?>>();
        buffer.add(new ValuePairImpl("已启用", 1));
        buffer.add(new ValuePairImpl("已停用", 0));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map metaMap, FieldMeta fieldMeta) {
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || obj.equals("")) return "";

        obj = "" + obj;
        String text = obj.toString();;
        if ("0".equals(obj)) {
            text = "已停用";
        } else if ("1".equals(obj)) {
            text = "已启用";
        }

        return text;
    }
}
