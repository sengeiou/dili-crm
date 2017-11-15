package com.dili.crm.provider;

import com.dili.crm.domain.Customer;
import com.dili.crm.service.CustomerService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 客户提供者
 */
@Component
public class CustomerProvider implements ValueProvider {

    @Autowired
    private CustomerService customerService;

    //不提供下拉数据
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if(obj == null || obj.equals("")) return null;
        Customer customer = customerService.get(Long.parseLong(obj.toString()));
        if(customer == null) return null;
        return customer.getName();
    }
}