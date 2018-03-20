package com.dili.points.service.impl;

import com.dili.points.dao.OrderMapper;
import com.dili.points.domain.Order;
import com.dili.points.service.OrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

    public OrderMapper getActualDao() {
        return (OrderMapper)getDao();
    }
}