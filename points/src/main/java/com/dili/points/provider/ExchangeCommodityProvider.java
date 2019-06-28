package com.dili.points.provider;

import com.dili.points.domain.ExchangeCommodities;
import com.dili.ss.dto.DTO;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/3/30 14:18
 */
@Component
public class ExchangeCommodityProvider implements ValueProvider {
    @Override
    public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object o, Map map, FieldMeta fieldMeta) {
        DTO ec = (DTO) map.get(ValueProvider.ROW_DATA_KEY);
        return String.valueOf(Integer.parseInt(ec.get("total").toString())-Integer.parseInt(ec.get("available").toString()));
    }
}
