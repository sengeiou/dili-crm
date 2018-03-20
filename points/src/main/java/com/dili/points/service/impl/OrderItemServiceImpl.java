package com.dili.points.service.impl;

import com.dili.points.dao.OrderItemMapper;
import com.dili.points.domain.OrderItem;
import com.dili.points.service.OrderItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Long> implements OrderItemService {

    public OrderItemMapper getActualDao() {
        return (OrderItemMapper)getDao();
    }
}