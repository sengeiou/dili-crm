package com.dili.points.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dili.points.domain.Category;
import com.dili.points.domain.Order;
import com.dili.points.domain.OrderItem;
import com.dili.points.service.OrderItemService;
import com.dili.points.service.OrderService;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;

/**
 * 积分监听组件
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
public class OrderListener {
	private static final Logger logger=LoggerFactory.getLogger(OrderListener.class);
	@Autowired OrderService orderService;
	@Autowired OrderItemService orderItemService;
//	@RabbitListener(queues = "#{rabbitConfiguration.TOPIC_QUEUE}")
	public void processBootTask(String orderJson) {
		System.out.println("OrderListener:"+orderJson);
		Map<Order,List<OrderItem>>orderMap=this.convertOrder(orderJson);
		if(orderMap.isEmpty()){
			return;
		}
		this.insertOrderAndItem(orderMap);
		
	}
	private Map<Order,List<OrderItem>>convertOrder(String json){
		return new HashMap<>();
	}
	@Transactional
	public void insertOrderAndItem(Map<Order,List<OrderItem>>orderMap) {
		for(Order order:orderMap.keySet()) {
			this.orderService.insert(order);
			List<OrderItem>items=orderMap.get(order);
			for(OrderItem item:items) {
				item.setOrderId(order.getId());
			}
			this.orderItemService.batchInsert(items);
		}
		
		
	}
	
}
