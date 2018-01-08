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
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/1/8 15:39
 */
@Component
public class IsDefaultProvider implements ValueProvider {
    private static final List<ValuePair<?>> BUFFER;

    static {
        BUFFER = new ArrayList<ValuePair<?>>();
        BUFFER.add(new ValuePairImpl("是", "1"));
        BUFFER.add(new ValuePairImpl("否", "0"));
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
