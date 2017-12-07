package com.dili.crm.provider;

import com.dili.crm.cache.CrmCache;
import com.dili.crm.domain.City;
import com.dili.crm.service.CacheService;
import com.dili.crm.service.CityService;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.util.CharUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 城市提供者
 * @author asiamaster
 */
@Component
public class CityProvider implements ValueProvider {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CityService cityService;

    private static final int COUNT = 20;

    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        if(null == val || StringUtils.isBlank(val.toString())) {
            return null;
        }
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        City city = DTOUtils.newDTO(City.class);
        if(CharUtil.isChinese(val.toString())) {
            city.setMergerName(val.toString());
        }else {
            city.setShortPy(val.toString().toUpperCase());
        }
        List<City> cities = cityService.listByExample(city);
        //最多取COUNT条联想数据
        int count = cities.size() > COUNT ? COUNT : cities.size();
        for(int i=0; i<count; i++) {
            buffer.add(new ValuePairImpl( cities.get(i).getMergerName(), cities.get(i).getId().toString()));
        }
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || "".equals(obj)) {
            return null;
        }
        cacheService.refreshCity();
        if(!NumberUtils.isDigits(obj.toString())){
            return null;
        }
        City city = CrmCache.CITY_MAP.get(Long.parseLong(obj.toString()));
        if(city == null){
            return null;
        }
        return city.getMergerName();
    }
}