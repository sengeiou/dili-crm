package com.dili.points.component;

import java.math.BigDecimal;
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

import okhttp3.internal.ws.RealWebSocket.Streams;

import static org.mockito.Mockito.*;
@RunWith(SpringJUnit4ClassRunner.class)
// @SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@SpringBootTest(classes = PointsApplication.class)
public class OrderListenerTest {
	 @InjectMocks @Autowired OrderListener orderListener;
	
	// @MockBean PointsRuleService pointsRuleService;
	// @Mock RuleConditionService ruleConditionService;
	   
	@Before
    public void setUp() throws Exception {
        // 初始化测试用例类中由Mockito的注解标注的所有模拟对象
        MockitoAnnotations.initMocks(this);
    }
	private Order buildOrder() {

		Order order=DTOUtils.newDTO(Order.class); 
		order.setCode("123");
		order.setSellerCardNo(1111L);
		order.setSellerCertificateNumber("230106197601090813");
		order.setBuyerCardNo(2222L);
		order.setBuyerCertificateNumber("230119198210102318");
		order.setMarket("HD");
		order.setTotalMoney(100L);
		order.setWeight(new BigDecimal("10"));
		order.setPayDate(new Date());
		order.setPayment(20);
		order.setBusinessType(10);
		order.setSourceSystem("settlement");
		order.setSettlementCode("9999999");
		return order;
		
	}
	private List<OrderItem> buildOrderItems(){
		List<OrderItem>orderItemList=new ArrayList<>();
		OrderItem orderItem=DTOUtils.newDTO(OrderItem.class); 
		orderItem.setCategoryId(100L);
		orderItem.setTotalMoney(100L);
		orderItem.setWeight(new BigDecimal("500"));
		orderItemList.add(orderItem);
		
		return orderItemList;
	}
//	private Map<Order,List<OrderItem>> buildTestData() {
//		
//		Order order=DTOUtils.newDTO(Order.class); 
//		order.setCode("123");
//		order.setSellerCardNo(1111L);
//		order.setSellerCertificateNumber("1111");
//		order.setBuyerCardNo(2222L);
//		order.setBuyerCertificateNumber("2222");
//		order.setMarket("HD");
//		order.setTotalMoney(100L);
//		order.setWeight(new BigDecimal("500"));
//		order.setPayDate(new Date());
//		order.setPayment(20);
//		order.setBusinessType(10);
//		order.setSourceSystem("settlement");
//		
//		
//		List<OrderItem>orderItemList=new ArrayList<>();
//		OrderItem orderItem=DTOUtils.newDTO(OrderItem.class); 
//		orderItem.setOrderId(100L);
//		orderItem.setCategoryId(100L);
//		orderItem.setTotalMoney(100L);
//		orderItem.setWeight(new BigDecimal("500"));
//		orderItemList.add(orderItem);
//		
//		Map<Order,List<OrderItem>>orderMap=new HashMap<>();
//		orderMap.put(order, orderItemList);
//		return orderMap;
//	}
	@Test
	public void sumOrdersForPurchase() {
		Map<Order,List<OrderItem>>orderMap=new HashMap<>();
		orderMap.put(this.buildOrder(), this.buildOrderItems());
		List<Order>list=this.orderListener.sumOrdersForPurchase(orderMap.keySet());
		Assert.assertEquals(list.size(), 1);
		
		
	}
	@Test
	public void calculateBasePoints() {
		Order order=this.buildOrder();
		List<OrderItem>orderItems=this.buildOrderItems();
		
		Map<Order,List<OrderItem>>orderMap=new HashMap<>();
		orderMap.put(order, orderItems);
		
		orderListener=spy(orderListener);
		
		
		PointsRule pointsRuleEx=DTOUtils.newDTO(PointsRule.class);
		pointsRuleEx.setYn(1);
		pointsRuleEx.setCustomerType("sale");
		pointsRuleEx.setBusinessType(169);//169交易,170充值,171开卡
		
		
		
		
		
		PointsRule pr=DTOUtils.newDTO(PointsRule.class);  
		pr.setId(1L);
		pr.setComputingStandard(172);//172 交易量,173 交易额,174 固定值
		pr.setComputingParameter(0.1F);
		
	
		doReturn(Optional.of(pr)).when(orderListener).findPointsRule("sale");
		
		
		
		//when(pointsRuleService.listByExample(pointsRuleEx)).thenReturn(Stream.of(pr).collect(Collectors.toList()));
		pointsRuleEx.setCustomerType("purchase");
		PointsRule pr2=DTOUtils.newDTO(PointsRule.class);  
		pr2.setId(2L);
		pr2.setComputingStandard(173);//172 交易量,173 交易额,174 固定值
		pr2.setComputingParameter(0.3F);
		
		//when(pointsRuleService.listByExample(pointsRuleEx)).thenReturn(Stream.of(pr).collect(Collectors.toList()));
		doReturn(Optional.of(pr2)).when(orderListener).findPointsRule("purchase");
		BigDecimal basePoints=	this.orderListener.calculateBasePoints(pr,order);
		System.out.println(basePoints);
		Assert.assertEquals(basePoints.intValue(), 50);
		
		
		
		
//		
//		
//		List<RuleCondition>sales175Condition=new ArrayList<>();
//		RuleCondition rc1=DTOUtils.newDTO(RuleCondition.class);   
//		rc1.setConditionType(180);//区间: 179,  大于等于 180 大于  181, 小于等于 182, 小于 183, 等于 184
//		rc1.setStartValue("100");
//		sales175Condition.add(rc1);
//		doReturn(sales175Condition).when(orderListener).findRuleCondition(1L,175);// 交易量 175 交易额 176  商品 177 支付方式:178
//		
//		List<RuleCondition>sales176Condition=new ArrayList<>();
//		doReturn(sales176Condition).when(orderListener).findRuleCondition(1L,176);// 交易量 175 交易额 176  商品 177 支付方式:178
//		
//		List<RuleCondition>sales178Condition=new ArrayList<>();
//		doReturn(sales178Condition).when(orderListener).findRuleCondition(1L,178);// 交易量 175 交易额 176  商品 177 支付方式:178
//		
//		
//		List<RuleCondition>purchase175Condition=new ArrayList<>();
//		doReturn(purchase175Condition).when(orderListener).findRuleCondition(2L,175);// 交易量 175 交易额 176  商品 177 支付方式:178
//		
//		List<RuleCondition>purchase176Condition=new ArrayList<>();
//		doReturn(purchase176Condition).when(orderListener).findRuleCondition(2L,176);// 交易量 175 交易额 176  商品 177 支付方式:178
//		
//		List<RuleCondition>purchase178Condition=new ArrayList<>();
//		doReturn(purchase178Condition).when(orderListener).findRuleCondition(2L,178);// 交易量 175 交易额 176  商品 177 支付方式:178
//		
//		
//		this.orderListener.calPointsAndSaveData(orderMap);
//		
		
	}
//	@Autowired OrderService orderService;
//	@Test
//	public void insertOrder() {
//		Order order=this.buildOrder();
//		this.orderService.insertSelective(order);
//	}
	@Test
	public void calPointsAndSaveData() {
dd();
	}
	private void dd() {
		Order order=this.buildOrder();
		List<OrderItem>orderItems=this.buildOrderItems();
		
		Map<Order,List<OrderItem>>orderMap=new HashMap<>();
		orderMap.put(order, orderItems);
		this.orderListener.calPointsAndSaveData(orderMap);
		
	}
}
