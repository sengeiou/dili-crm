package com.dili.points.service;

import com.dili.points.domain.CustomerCategoryPoints;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-09 17:35:45.
 */
public interface CustomerCategoryPointsService extends BaseService<CustomerCategoryPoints, Long> {

    int updateCustomerName(String name,Long id);
}