package com.dili.crm.domain.dto;

import com.dili.ss.dto.IBaseDomain;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-03-20 11:29:30.
 */
public interface CustomerPointsApiDTO extends IBaseDomain {

    List<Long> getCustomerIds();

    void setCustomerIds(List<Long> customerIds);

}