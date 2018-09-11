package com.dili.dp.service.impl;

import com.dili.dp.dao.OrderMapper;
import com.dili.dp.domain.Order;
import com.dili.dp.service.OrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-21 16:01:59.
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

    public OrderMapper getActualDao() {
        return (OrderMapper)getDao();
    }
}