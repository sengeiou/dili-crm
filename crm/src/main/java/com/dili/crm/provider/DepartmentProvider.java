package com.dili.crm.provider;

import com.dili.crm.cache.CrmCache;
import com.dili.crm.service.CacheService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 客户提供者
 */
@Component
public class DepartmentProvider implements ValueProvider {

    @Autowired
    private CacheService cacheService;
    //不提供下拉数据
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        List<ValuePair<?>> buffer = new ArrayList<ValuePair<?>>();
        if(CrmCache.DEPARTMENT_MAP == null || CrmCache.DEPARTMENT_MAP.isEmpty()) {
            cacheService.refreshDepartment();
        }
        if(CrmCache.DEPARTMENT_MAP == null) return null;
        for(Long id : CrmCache.DEPARTMENT_MAP.keySet()){
            buffer.add(new ValuePairImpl<>(CrmCache.DEPARTMENT_MAP.get(id).getName(), id));
        }
        return buffer;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || "".equals(obj)){
            return null;
        }
        if(CrmCache.DEPARTMENT_MAP == null || CrmCache.DEPARTMENT_MAP.isEmpty()) {
            cacheService.refreshDepartment();
        }
        if(CrmCache.DEPARTMENT_MAP == null) return null;
        for(Long id : CrmCache.DEPARTMENT_MAP.keySet()){
            if(id.equals(Long.parseLong(obj.toString()))){
                return CrmCache.DEPARTMENT_MAP.get(id).getName();
            }
        }
        return null;
    }
}