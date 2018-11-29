package com.dili.points.provider;

import com.dili.points.domain.ExchangeCommodities;
import com.dili.points.domain.dto.ExchangeCommoditiesDTO;
import com.dili.points.service.ExchangeCommoditiesService;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <B>商品信息的提供者</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/3/21 17:59
 */
@Component
public class CommodityProvider extends BatchDisplayTextProviderAdaptor {

    @Autowired
    private ExchangeCommoditiesService exchangeCommoditiesService;

    /**
     * 取下拉列表的选项
     *
     * @param val       值对象
     * @param metaMap   meta信息
     * @param fieldMeta
     * @return
     */
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        ExchangeCommodities ec = DTOUtils.newDTO(ExchangeCommodities.class);
        List<ExchangeCommodities> list = exchangeCommoditiesService.listByExample(ec);
        list.forEach(n -> buffer.add(new ValuePairImpl(n.getName(),n.getId().toString())));
        return buffer;
    }

    @Override
    protected List getFkList(List<String> ids, Map metaMap) {
        ExchangeCommoditiesDTO exchangeCommoditiesDTO = DTOUtils.newDTO(ExchangeCommoditiesDTO.class);
        exchangeCommoditiesDTO.setIds(ids);
        return exchangeCommoditiesService.listByExample(exchangeCommoditiesDTO);
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

//    /**
//     * 取显示文本的值
//     *
//     * @param val       值对象
//     * @param metaMap   meta信息，包括:当前行数据:_rowData,当前字段名:_field及其它DTO中的meta信息
//     * @param fieldMeta 当前字段的注解封装对象
//     * @return
//     */
//    @Override
//    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
//        if(val == null || "".equals(val)) {
//            return null;
//        }
//        if(!NumberUtils.isDigits(val.toString())){
//            return null;
//        }
//        ExchangeCommodities commodities = exchangeCommoditiesService.get(Long.valueOf(val.toString()));
//        if(null == commodities){
//            return null;
//        }
//        return commodities.getName();
//    }
}
