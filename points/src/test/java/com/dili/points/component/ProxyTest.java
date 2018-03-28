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
Map<Order,List<OrderItem>>map=new HashMap<>();
map.put(DTOUtils.newDTO(Order.class), new ArrayList<>());
map.put(DTOUtils.newDTO(Order.class), new ArrayList<>());
map.put(DTOUtils.newDTO(Order.class), new ArrayList<>());
map.put(DTOUtils.newDTO(Order.class), new ArrayList<>());
map.put(DTOUtils.newDTO(Order.class), new ArrayList<>());
map.put(DTOUtils.newDTO(Order.class), new ArrayList<>());
for(Order order:map.keySet()) {
	List<OrderItem>list=map.get(order);
	System.out.println(list);
}



	}
}
