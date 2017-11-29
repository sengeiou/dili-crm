package com.dili.crm.provider;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.service.CustomerService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 客户提供者
 */
@Component
public class VisitCustomerProvider implements ValueProvider {

    @Autowired
    private CustomerService customerService;

    //不提供下拉数据
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (CollectionUtils.isEmpty(metaMap)) {
            return null;
        }
        if (metaMap.containsKey("field")){
            CustomerVisit cv = (CustomerVisit)metaMap.get("_rowData");
            Customer customer = customerService.get(cv.getCustomerId());
            if(customer == null) return null;
            String field = (String)metaMap.get("field");
            if ("customerName".equalsIgnoreCase(field)){
                 return customer.getName();
            }else if ("customerPhone".equalsIgnoreCase(field)){
                return customer.getPhone();
            }
        }

        return null;
    }
}