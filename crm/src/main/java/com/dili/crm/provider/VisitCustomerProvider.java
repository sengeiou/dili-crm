package com.dili.crm.provider;

import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户提供者
 */
@Component
public class VisitCustomerProvider extends BatchDisplayTextProviderAdaptor {

    /**
     * 返回主DTO和关联DTO需要转义的字段名
     * Map中key为主DTO在页面(datagrid)渲染时需要的字段名， value为关联DTO中对应的字段名
     * @return
     */
    @Override
    protected Map<String, String> getEscapeFileds(){
        Map<String, String> excapeFields = new HashMap<>();
        excapeFields.put("customerName", "name");
        excapeFields.put("customerPhone", "phone");
        return excapeFields;
    }

    /**
     * 主DTO与关联DTO的关联属性
     * @return
     */
    @Override
    protected String getRelationField(){
        return "customerId";
    }

    /**
     * 关联表名
     * @return
     */
    @Override
    protected String getRelationTable(){
        return "customer";
    }

    /**
     * 关联表的主键名
     * @return
     */
    @Override
    protected String getRelationTablePKField(){
        return "id";
    }
}