package com.dili.points.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dili.points.domain.Category;
import com.dili.points.domain.Order;
import com.dili.points.domain.OrderItem;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.RuleCondition;
import com.dili.points.provider.DataDictionaryValueProvider;
import com.dili.points.service.OrderItemService;
import com.dili.points.service.OrderService;
import com.dili.points.service.PointsDetailService;
import com.dili.points.service.PointsRuleService;
import com.dili.points.service.RuleConditionService;
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
	@Autowired PointsRuleService pointsRuleService;
	@Autowired DataDictionaryValueProvider dataDictionaryValueProvider;
	@Autowired RuleConditionService ruleConditionService;
	@Autowired PointsDetailService pointsDetailService;
//	@RabbitListener(queues = "#{rabbitConfiguration.TOPIC_QUEUE}")
	public void processBootTask(String orderJson) {
		try {
			Map<Order,List<OrderItem>>orderMap=this.convertOrder(orderJson);
			if(orderMap.isEmpty()){
				logger.error("收到的交易订单信息错误: "+orderJson);
				return;
			}
			this.calPointsAndSaveData(orderMap);
		}catch(Exception e) {
			logger.error("计算积分并保存数据出错:"+e.getMessage(),e);
		}
	}
	private Map<Order,List<OrderItem>>convertOrder(String json){
		return new HashMap<>();
	}
	@Transactional
	public void calPointsAndSaveData(Map<Order,List<OrderItem>>orderMap) {
		List<Order>purchaseOrderList=this.sumOrdersForPurchase(orderMap.keySet());
		Set<Order>saleOrderList=orderMap.keySet();
		
		List<PointsDetail>purchasePointsDetails=this.calPoints(purchaseOrderList, "purchase");
		List<PointsDetail>salePointsDetails=this.calPoints(saleOrderList, "sale");
		
		
		for(Order order:orderMap.keySet()) {
			this.orderService.insert(order);
			List<OrderItem>items=orderMap.get(order);
			for(OrderItem item:items) {
				item.setOrderId(order.getId());
			}
			this.orderItemService.batchInsert(items);
		}
		
		this.pointsDetailService.batchInsert(purchasePointsDetails);
		this.pointsDetailService.batchInsert(salePointsDetails);
		
	}
	//卖家sale,买家purchase
	private Optional<PointsRule> findPointsRule(String customerType) {
		PointsRule pointsRuleEx=DTOUtils.newDTO(PointsRule.class);
		pointsRuleEx.setYn(1);
		pointsRuleEx.setCustomerType(customerType);
		pointsRuleEx.setBusinessType(169);//169交易,170充值,171开卡
		return this.pointsRuleService.listByExample(pointsRuleEx).stream().findFirst();
	}
	private List<RuleCondition> findRuleCondition(PointsRule pointsRule,Integer conditionType) {
		RuleCondition condition=DTOUtils.newDTO(RuleCondition.class);
		condition.setConditionType(conditionType);// 交易量 175 交易额 176  商品 177 支付方式:178
		List<RuleCondition>list=ruleConditionService.listByExample(condition);
		return list;
	}
	private List<Order> sumOrdersForPurchase(Collection<Order> orderList){
		Order order=DTOUtils.newDTO(Order.class);
		order.setTotalMoney(0L);
		order.setWeight(BigDecimal.ZERO);
		
		for(Order orderObj:orderList) {
			order.setBusinessType(orderObj.getBusinessType());
			order.setTotalMoney(orderObj.getTotalMoney()+order.getTotalMoney());
			order.setWeight(orderObj.getWeight().add(order.getWeight()));
			order.setPayment(orderObj.getPayment());
		}
		return Arrays.asList(order);
	}
	//卖家sale,买家purchase
	private List<PointsDetail> calPoints(Collection<Order> orderList,String customerType) {
		boolean isPurchase=false;
		if("sale".equals(customerType)) {
			isPurchase=false;
		}else if("purchase".equals(customerType)) {
			isPurchase=true;
		}else {
			return Collections.emptyList();
		}
		PointsRule pointsRule=this.findPointsRule(customerType).orElse(null);
		if(pointsRule==null) {
			return Collections.emptyList();
		}
		//172 交易量,173 交易额,174 固定值
		Integer computingStandard=pointsRule.getComputingStandard();
		Float computingParameter=pointsRule.getComputingParameter();
		//根据基准设置,计算基础积分值
		
		
		//根据 交易量,交易额 ,支付方式 的权重计算总积分
		
		// 交易量 175 交易额 176  商品 177 支付方式:178
		List<RuleCondition>tradeWeightConditionList=this.findRuleCondition(pointsRule, 175);
		List<RuleCondition>tradeTotalMoneyConditionList=this.findRuleCondition(pointsRule, 176);
		List<RuleCondition>tradeTypeConditionList=this.findRuleCondition(pointsRule, 178);
		
		List<PointsDetail>pointsDetailList=new ArrayList<>();
		for(Order order:orderList) {
			
			BigDecimal orderWeight=order.getWeight();//交易量
			
			BigDecimal totalMoney=new BigDecimal(order.getTotalMoney());//交易额
			//172 交易量,173 交易额,174 固定值
			BigDecimal basePoint=BigDecimal.ONE;
			if(computingStandard==172) {
				basePoint=basePoint.multiply(orderWeight).multiply(new BigDecimal(computingParameter));
			}else if(computingStandard==173) {
				basePoint=basePoint.multiply(totalMoney).multiply(new BigDecimal(computingParameter));
			}else if(computingStandard==174) {
				basePoint=basePoint.multiply(new BigDecimal(computingParameter));
			}else {
				//pointsDetail.setPoints(0);
				return Collections.emptyList();
			}
			
			PointsDetail pointsDetail=DTOUtils.newDTO(PointsDetail.class); 
			pointsDetail.setPoints(0);
			pointsDetail.setSourceSystem(order.getSourceSystem());
			pointsDetail.setOrder(order.getOrder());
			if(isPurchase) {
				pointsDetail.setCertificateNumber(order.getBuyerCertificateNumber());
			}else {
				pointsDetail.setCertificateNumber(order.getSellerCertificateNumber());
			}
			//如果买家或者卖家的证件号为空,则不进行积分
			if(StringUtils.isBlank(pointsDetail.getCertificateNumber())) {
				break;
			}
			
			//根据交易量计算积分
		
			for(RuleCondition ruleCondition:tradeWeightConditionList) {
				
				Float conditionWeight=ruleCondition.getWeight();//权重
				String startValue=ruleCondition.getStartValue();//开始值
				String endValue=ruleCondition.getEndValue();//结束值
				String value=ruleCondition.getValue();//条件值
				
				Integer conditonType=ruleCondition.getConditionType();//区间: 179,  大于等于 180 大于  181, 小于等于 182, 小于 183, 等于 184
				boolean hitCondition=false;
				if(conditonType.equals(179)&&(orderWeight.compareTo(new BigDecimal(startValue))>=0&&orderWeight.compareTo(new BigDecimal(endValue))<0)) {//区间
					hitCondition=true;
				}else if(conditonType.equals(180)&&(orderWeight.compareTo(new BigDecimal(startValue))>=0)) {//大于等于
					hitCondition=true;
				}else if(conditonType.equals(181)&&(orderWeight.compareTo(new BigDecimal(startValue))>0)) {//大于
					hitCondition=true;
				}else if(conditonType.equals(182)&&(orderWeight.compareTo(new BigDecimal(endValue))<=0)) {//小于等于
					hitCondition=true;
				}else if(conditonType.equals(183)&&(orderWeight.compareTo(new BigDecimal(endValue))<0)) {// 小于
					hitCondition=true;
				}else if(conditonType.equals(184)&&(orderWeight.compareTo(new BigDecimal(value))==0)) {//等于 
					hitCondition=true;
				}else {
					hitCondition=false;
				}
				//对当前条件进行积分
				if(hitCondition) {
					//如果有对上一个条件进行了积分,则当前积分:原来积分*权重*条件值,否则:权重*条件值
					basePoint=orderWeight.multiply(new BigDecimal(conditionWeight)).multiply(basePoint);
					break;
				}
			}
			
			
			//根据交易额计算积分
		
			// 交易量 175 交易额 176  商品 177 支付方式:178
			for(RuleCondition ruleCondition:tradeTotalMoneyConditionList) {
				
				
				Float conditionWeight=ruleCondition.getWeight();//权重
				String startValue=ruleCondition.getStartValue();//开始值
				String endValue=ruleCondition.getEndValue();//结束值
				String value=ruleCondition.getValue();//条件值
				
				Integer conditonType=ruleCondition.getConditionType();//区间: 179,  大于等于 180 大于  181, 小于等于 182, 小于 183, 等于 184
				boolean hitCondition=false;
				if(conditonType.equals(179)&&(totalMoney.compareTo(new BigDecimal(startValue))>=0&&totalMoney.compareTo(new BigDecimal(endValue))<0)) {
					hitCondition=true;
				}else if(conditonType.equals(180)&&(totalMoney.compareTo(new BigDecimal(startValue))>=0)) {
					hitCondition=true;
				}else if(conditonType.equals(181)&&(totalMoney.compareTo(new BigDecimal(startValue))>0)) {
					hitCondition=true;
				}else if(conditonType.equals(182)&&(totalMoney.compareTo(new BigDecimal(endValue))<=0)) {
					hitCondition=true;
				}else if(conditonType.equals(183)&&(totalMoney.compareTo(new BigDecimal(endValue))<0)) {
					hitCondition=true;
				}else if(conditonType.equals(184)&&(totalMoney.compareTo(new BigDecimal(value))==0)) {
					hitCondition=true;
				}else {
					hitCondition=false;
				}

				if(hitCondition) {
					basePoint=totalMoney.multiply(new BigDecimal(conditionWeight)).multiply(basePoint);
					break;
				}
				
			}


			//根据支付方式计算积分
			BigDecimal payment=new BigDecimal(order.getPayment());//支付方式
			for(RuleCondition ruleCondition:tradeTypeConditionList) {
				
				Float conditionWeight=ruleCondition.getWeight();//权重
				String startValue=ruleCondition.getStartValue();//开始值
				String endValue=ruleCondition.getEndValue();//结束值
				String value=ruleCondition.getValue();//条件值
				
				Integer conditonType=ruleCondition.getConditionType();//区间: 179,  大于等于 180 大于  181, 小于等于 182, 小于 183, 等于 184
				boolean hitCondition=false;
				if(conditonType.equals(179)&&(payment.compareTo(new BigDecimal(startValue))>=0&&payment.compareTo(new BigDecimal(endValue))<0)) {
					hitCondition=true;
				}else if(conditonType.equals(180)&&(payment.compareTo(new BigDecimal(startValue))>=0)) {
					hitCondition=true;
				}else if(conditonType.equals(181)&&(payment.compareTo(new BigDecimal(startValue))>0)) {
					hitCondition=true;
				}else if(conditonType.equals(182)&&(payment.compareTo(new BigDecimal(endValue))<=0)) {
					hitCondition=true;
				}else if(conditonType.equals(183)&&(payment.compareTo(new BigDecimal(endValue))<0)) {
					hitCondition=true;
				}else if(conditonType.equals(184)&&(payment.compareTo(new BigDecimal(value))==0)) {
					hitCondition=true;
				}else {
					hitCondition=false;
				}
				
				if(hitCondition) {
					basePoint=new BigDecimal(conditionWeight).multiply(basePoint);
					break;
				}
			}
			pointsDetail.setPoints(basePoint.intValue());
			pointsDetailList.add(pointsDetail);
		}
		
		return pointsDetailList;
	}
	
	
}
