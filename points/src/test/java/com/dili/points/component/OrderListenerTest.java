package com.dili.points.component;

import com.dili.PointsApplication;
import com.dili.points.domain.Order;
import com.dili.points.domain.OrderItem;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.RuleCondition;
import com.dili.ss.dto.DTOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
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
		order.setCode("12345");
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
		order.setSettlementCode("77777");
		return order;
		
	}
	private List<OrderItem> buildOrderItems(){
		List<OrderItem>orderItemList=new ArrayList<>();
		OrderItem orderItem=DTOUtils.newDTO(OrderItem.class); 
		orderItem.setCategoryId(1L);
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
//	@Test
//	public void sumOrdersForPurchase() {
//		Map<Order,List<OrderItem>>orderMap=new HashMap<>();
//		orderMap.put(this.buildOrder(), this.buildOrderItems());
//		List<Order>list=this.orderListener.sumOrdersForPurchase(orderMap.keySet());
//		Assert.assertEquals(list.size(), 1);
//		
//		
//	}
	@Test
	public void findNotes() {
		Order order=this.buildOrder();
		List<OrderItem>orderItems=this.buildOrderItems();
		
		Map<Order,List<OrderItem>>orderMap=new HashMap<>();
		orderMap.put(order, orderItems);
		String notes=this.orderListener.findNotes(order, orderItems);
		System.out.println(notes);
	}
	@Test
	public void calculateWeight() {
		// 等于 10,  大于等于 20 大于  30, 小于等于 40, 小于 50,区间: 60,
		RuleCondition ruleCondition=DTOUtils.newDTO(RuleCondition.class);
		
		ruleCondition.setConditionType(10);
		ruleCondition.setStartValue(null);
		ruleCondition.setEndValue(null);
		ruleCondition.setValue("10");
		ruleCondition.setWeight("1.1");
		List<RuleCondition>ruleConditionList=Lists.newArrayList(ruleCondition);
		BigDecimal weight=this.orderListener.calculateWeight(new BigDecimal("10"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		
		weight=this.orderListener.calculateWeight(new BigDecimal("9"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
		
		
		ruleCondition.setConditionType(20);
		ruleCondition.setValue("20");
		ruleCondition.setWeight("1.2");
		
		weight=this.orderListener.calculateWeight(new BigDecimal("20"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		weight=this.orderListener.calculateWeight(new BigDecimal("21"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		
		weight=this.orderListener.calculateWeight(new BigDecimal("19"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
		
		ruleCondition.setConditionType(30);
		ruleCondition.setValue("30");
		ruleCondition.setWeight("1.3");
		weight=this.orderListener.calculateWeight(new BigDecimal("31"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		
		weight=this.orderListener.calculateWeight(new BigDecimal("30"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
		weight=this.orderListener.calculateWeight(new BigDecimal("29"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
		
		
		ruleCondition.setConditionType(40);
		ruleCondition.setValue("40");
		ruleCondition.setWeight("1.4");
		weight=this.orderListener.calculateWeight(new BigDecimal("40"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		weight=this.orderListener.calculateWeight(new BigDecimal("39"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		
		weight=this.orderListener.calculateWeight(new BigDecimal("41"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
		
		
		ruleCondition.setConditionType(50);
		ruleCondition.setValue("50");
		ruleCondition.setWeight("1.5");
		weight=this.orderListener.calculateWeight(new BigDecimal("49"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		
		weight=this.orderListener.calculateWeight(new BigDecimal("50"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
		weight=this.orderListener.calculateWeight(new BigDecimal("51"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
		
		ruleCondition.setConditionType(60);
		ruleCondition.setStartValue("55");
		ruleCondition.setValue(null);
		ruleCondition.setEndValue("65");
		ruleCondition.setWeight("1.6");
		weight=this.orderListener.calculateWeight(new BigDecimal("55"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		weight=this.orderListener.calculateWeight(new BigDecimal("65"), ruleConditionList);
		Assert.assertEquals(weight.subtract(new BigDecimal(ruleCondition.getWeight())).setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
		
		weight=this.orderListener.calculateWeight(new BigDecimal("54"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
		weight=this.orderListener.calculateWeight(new BigDecimal("66"), ruleConditionList);
		Assert.assertEquals(weight, BigDecimal.ONE);
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
		pr.setComputingParameter("0.1");
		
	
		doReturn(Optional.of(pr)).when(orderListener).findPointsRule("sale");
		
		
		
		//when(pointsRuleService.listByExample(pointsRuleEx)).thenReturn(Stream.of(pr).collect(Collectors.toList()));
		pointsRuleEx.setCustomerType("purchase");
		PointsRule pr2=DTOUtils.newDTO(PointsRule.class);  
		pr2.setId(2L);
		pr2.setComputingStandard(173);//172 交易量,173 交易额,174 固定值
		pr2.setComputingParameter("0.3");
		
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

		Order order=this.buildOrder();
		List<OrderItem>orderItems=this.buildOrderItems();
		
		Map<Order,List<OrderItem>>orderMap=new HashMap<>();
		orderMap.put(order, orderItems);
		this.orderListener.calAndSaveData(orderMap);
		
	}
	@Test
	public void convertOrder() throws JsonProcessingException {

		Order order=this.buildOrder();
		List<OrderItem>orderItems=this.buildOrderItems();
		
		
		Map<String,Object>data=DTOUtils.go(order);
		data.put("orderItems", orderItems);
		
		
		List<Map<String,Object>>list=new ArrayList<>();
		list.add(data);
		
		ObjectMapper objMapper=new ObjectMapper();
		Map<String,Object> jsonObj=new HashMap<>();
		jsonObj.put("type", "json");
jsonObj.put("data", list);
System.out.println(objMapper.writeValueAsString(jsonObj));
		this.orderListener.convertOrder(objMapper.writeValueAsString(jsonObj));
		
	}
}
