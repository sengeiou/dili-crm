package com.dili.points.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.PointsApplication;
import com.dili.points.domain.Order;
import com.dili.points.domain.OrderItem;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.RuleCondition;
import com.dili.points.service.OrderService;
import com.dili.points.service.PointsRuleService;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import okhttp3.internal.ws.RealWebSocket.Streams;

import static org.mockito.Mockito.*;

public class ProxyTest {
	
	@Test
	public void calculateWeight() {
Map<Order,List<OrderItem>>map=new HashMap<>(100);
List<OrderItem>items=new ArrayList<>();
OrderItem item=DTOUtils.newDTO(OrderItem.class);
item.setTotalMoney(100L);
items.add(item);
Order o=DTOUtils.newDTO(Order.class);
o.setBusinessType(12345);
o.setBuyerCardNo(111L);
o.setBuyerCertificateNumber("2222");
o.setCode("3333");
o.setCreated(new Date());
o.setPayDate(new Date());
o.setPayment(1);
o.setSellerCardNo(257L);
o.setSellerCertificateNumber("5555555555");
o.setSettlementCode("66666");
o.setSourceSystem("toll");
o.setTotalMoney(23L);
o.setWeight(new BigDecimal("144"));
map.put(o, items);
int count=0;
System.out.println(o.hashCode());
o.setBusinessType(123);
System.out.println(o.hashCode());
o.setBusinessType(456);
System.out.println(o.hashCode());


	for(Order order:map.keySet()) {
		
		List<OrderItem>list=map.get(order);

		BigDecimal orderWeight = order.getWeight();// 交易量

		BigDecimal totalMoney = new BigDecimal(order.getTotalMoney());// 交易额
		System.out.println(list);
	}
BigDecimal a=new BigDecimal("123");
BigDecimal b=new BigDecimal("456");
BigDecimal c=a.add(b);

System.out.println(a.toPlainString());
System.out.println(b.toPlainString());
System.out.println(c.toPlainString());
//for(Order order:map.keySet()) {
//	//order.getBusinessType();
//	List<OrderItem>list=map.get(order);
//	System.out.println(list);
//}


	}
}
