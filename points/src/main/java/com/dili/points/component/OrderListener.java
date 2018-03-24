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
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dili.points.domain.Category;
import com.dili.points.domain.Customer;
import com.dili.points.domain.Order;
import com.dili.points.domain.OrderItem;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.RuleCondition;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.points.provider.DataDictionaryValueProvider;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.service.OrderItemService;
import com.dili.points.service.OrderService;
import com.dili.points.service.PointsDetailService;
import com.dili.points.service.PointsRuleService;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.domain.BaseOutput;
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
	@Autowired CustomerRpc customerRpc;
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
	@Transactional(timeout=90,propagation=Propagation.REQUIRED)
	public void calPointsAndSaveData(Map<Order,List<OrderItem>>orderMap) {
		
		Collection<Order>purchaseOrderList=this.sumOrdersForPurchase(orderMap.keySet());
		Collection<Order>saleOrderList=orderMap.keySet();
		
		List<PointsDetailDTO>purchasePointsDetails=this.calPoints(purchaseOrderList, "purchase");
		List<PointsDetailDTO>salePointsDetails=this.calPoints(saleOrderList, "sale");
		
		for(Order order:orderMap.keySet()) {
			List<OrderItem>items=orderMap.get(order);
			this.orderService.insertSelective(order);
			for(OrderItem item:items) {
				item.setOrderCode(order.getCode());
			}
			this.orderItemService.batchInsert(items);
		}
			this.pointsDetailService.batchInsertPointsDetailDTO(purchasePointsDetails);
			this.pointsDetailService.batchInsertPointsDetailDTO(salePointsDetails);
		
	}

	protected Long findIdByCertificateNumber(String certificateNumber) {
		CustomerApiDTO dto=DTOUtils.newDTO(CustomerApiDTO.class);
		dto.setCertificateNumber(certificateNumber);
		BaseOutput<List<Customer>>baseOut=this.customerRpc.list(dto);
		if(baseOut.isSuccess()&&!baseOut.getData().isEmpty()) {
			return baseOut.getData().get(0).getId();
		}
		return null;
		
	}
	//卖家sale,买家purchase
	protected Optional<PointsRule> findPointsRule(String customerType) {
		PointsRule pointsRuleEx=DTOUtils.newDTO(PointsRule.class);
		pointsRuleEx.setYn(1);
		pointsRuleEx.setCustomerType(customerType);
		pointsRuleEx.setBusinessType(10);//10交易,20充值,30开卡
		return this.pointsRuleService.listByExample(pointsRuleEx).stream().findFirst();
	}
	protected List<RuleCondition> findRuleCondition(Long pointsRuleId,Integer conditionType) {
		RuleCondition condition=DTOUtils.newDTO(RuleCondition.class);
		condition.setConditionType(conditionType);// 交易量 175 交易额 176  商品 177 支付方式:178
		condition.setPointRuleId(pointsRuleId);
		List<RuleCondition>list=ruleConditionService.listByExample(condition);
		return list;
	}
	protected List<Order> sumOrdersForPurchase(Collection<Order> orderList){
		if(orderList.size()==1) {
			return orderList.stream().collect(Collectors.toList());
		}
		
		
		Order order=DTOUtils.newDTO(Order.class);
		order.setTotalMoney(0L);
		order.setWeight(BigDecimal.ZERO);
		
		for(Order orderObj:orderList) {
			order.setTotalMoney(orderObj.getTotalMoney()+order.getTotalMoney());
			order.setWeight(orderObj.getWeight().add(order.getWeight()));
			order.setPayment(orderObj.getPayment());
			
			order.setBusinessType(orderObj.getBusinessType());
			order.setBuyerCardNo(orderObj.getBuyerCardNo());
			order.setBuyerCertificateNumber(orderObj.getBuyerCertificateNumber());
			order.setCode(orderObj.getCode());
			order.setPayment(orderObj.getPayment());
			order.setSellerCardNo(orderObj.getSellerCardNo());
			order.setSellerCertificateNumber(orderObj.getSellerCertificateNumber());
		}
		return Arrays.asList(order);
	}
	protected BigDecimal calculateBasePoints(PointsRule pointsRule,Order order) {
		BigDecimal orderWeight=order.getWeight();//交易量
		
		BigDecimal totalMoney=new BigDecimal(order.getTotalMoney());//交易额
		//10 交易量,20 交易额,30 固定值
		Integer computingStandard=pointsRule.getComputingStandard();
		Float computingParameter=pointsRule.getComputingParameter();
		//根据基准设置,计算基础积分值
		BigDecimal basePoint=BigDecimal.ZERO;
		if(computingStandard==10) {
			basePoint=(orderWeight).multiply(new BigDecimal(computingParameter));
		}else if(computingStandard==20) {
			basePoint=(totalMoney).multiply(new BigDecimal(computingParameter));
		}else if(computingStandard==30) {
			basePoint=(new BigDecimal(computingParameter));
		}else {
			return BigDecimal.ZERO;
		}
		return basePoint;
	}
	
	//卖家sale,买家purchase
	private List<PointsDetailDTO> calPoints(Collection<Order> orderList,String customerType) {
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
//		Integer computingStandard=pointsRule.getComputingStandard();
//		Float computingParameter=pointsRule.getComputingParameter();
		//根据基准设置,计算基础积分值
		
		
		//根据 交易量,交易额 ,支付方式 的权重计算总积分
		
		// 交易量 10 交易额 20  商品 30 支付方式:40
		List<RuleCondition>tradeWeightConditionList=this.findRuleCondition(pointsRule.getId(), 10);
		List<RuleCondition>tradeTotalMoneyConditionList=this.findRuleCondition(pointsRule.getId(), 20);
		List<RuleCondition>tradeTypeConditionList=this.findRuleCondition(pointsRule.getId(), 40);
		
		List<PointsDetailDTO>pointsDetailList=new ArrayList<>();
		for(Order order:orderList) {
			
			BigDecimal orderWeight=order.getWeight();//交易量
			
			BigDecimal totalMoney=new BigDecimal(order.getTotalMoney());//交易额
			
			BigDecimal basePoint=this.calculateBasePoints(pointsRule, order);
//			if(computingStandard==172) {
//				basePoint=basePoint.multiply(orderWeight).multiply(new BigDecimal(computingParameter));
//			}else if(computingStandard==173) {
//				basePoint=basePoint.multiply(totalMoney).multiply(new BigDecimal(computingParameter));
//			}else if(computingStandard==174) {
//				basePoint=basePoint.multiply(new BigDecimal(computingParameter));
//			}else {
//				//pointsDetail.setPoints(0);
//				return Collections.emptyList();
//			}
//			
			PointsDetailDTO pointsDetail=DTOUtils.newDTO(PointsDetailDTO.class); 
			pointsDetail.setPoints(basePoint.intValue());
			pointsDetail.setSourceSystem(order.getSourceSystem());
			//只有一个订单信息
			if(StringUtils.isNoneBlank(order.getSettlementCode())) {
				pointsDetail.setOrderCode(order.getSettlementCode());
				pointsDetail.setOrderType("settlementOrder");//settlementOrder结算单号,order主单
			}else{
				pointsDetail.setOrderCode(order.getCode());
				pointsDetail.setOrderType("order");//settlementOrder结算单号,order主单	
			}
			String certificateNumber=null;
			//buyer
			if(isPurchase) {
				certificateNumber=order.getBuyerCertificateNumber();
			}else {
				certificateNumber=order.getSellerCertificateNumber();
			}
			//交易10 充值20 兑换礼品30  扣减:40 手工调整50
			pointsDetail.setGenerateWay(10);
			pointsDetail.setCertificateNumber(certificateNumber);
			pointsDetail.setSourceSystem(order.getSourceSystem());
			Long customerId=this.findIdByCertificateNumber(certificateNumber);
			pointsDetail.setCustomerId(customerId);
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
				
				Integer conditonType=ruleCondition.getConditionType();//区间: 60,  大于等于 20 大于  30, 小于等于 40, 小于 50, 等于 10
				boolean hitCondition=false;
				if(conditonType.equals(60)&&(orderWeight.compareTo(new BigDecimal(startValue))>=0&&orderWeight.compareTo(new BigDecimal(endValue))<0)) {//区间
					hitCondition=true;
				}else if(conditonType.equals(20)&&(orderWeight.compareTo(new BigDecimal(value))>=0)) {//大于等于
					hitCondition=true;
				}else if(conditonType.equals(30)&&(orderWeight.compareTo(new BigDecimal(value))>0)) {//大于
					hitCondition=true;
				}else if(conditonType.equals(40)&&(orderWeight.compareTo(new BigDecimal(value))<=0)) {//小于等于
					hitCondition=true;
				}else if(conditonType.equals(50)&&(orderWeight.compareTo(new BigDecimal(value))<0)) {// 小于
					hitCondition=true;
				}else if(conditonType.equals(10)&&(orderWeight.compareTo(new BigDecimal(value))==0)) {//等于 
					hitCondition=true;
				}else {
					hitCondition=false;
				}
				//对当前条件进行积分
				if(hitCondition) {
					//如果有对上一个条件进行了积分,则当前积分:原来积分*权重*条件值,否则:权重*条件值
					basePoint=new BigDecimal(conditionWeight).multiply(basePoint);
					break;
				}
			}
			
			
			//根据交易额计算积分
		
			// 交易量 10 交易额 20  商品 30 支付方式:40
			for(RuleCondition ruleCondition:tradeTotalMoneyConditionList) {
				
				
				Float conditionWeight=ruleCondition.getWeight();//权重
				String startValue=ruleCondition.getStartValue();//开始值
				String endValue=ruleCondition.getEndValue();//结束值
				String value=ruleCondition.getValue();//条件值
				
				Integer conditonType=ruleCondition.getConditionType();//区间: 60,  大于等于 20 大于  30, 小于等于 40, 小于 50, 等于 10
				boolean hitCondition=false;
				if(conditonType.equals(60)&&(totalMoney.compareTo(new BigDecimal(startValue))>=0&&totalMoney.compareTo(new BigDecimal(endValue))<0)) {
					hitCondition=true;
				}else if(conditonType.equals(20)&&(totalMoney.compareTo(new BigDecimal(value))>=0)) {
					hitCondition=true;
				}else if(conditonType.equals(30)&&(totalMoney.compareTo(new BigDecimal(value))>0)) {
					hitCondition=true;
				}else if(conditonType.equals(40)&&(totalMoney.compareTo(new BigDecimal(value))<=0)) {
					hitCondition=true;
				}else if(conditonType.equals(50)&&(totalMoney.compareTo(new BigDecimal(value))<0)) {
					hitCondition=true;
				}else if(conditonType.equals(10)&&(totalMoney.compareTo(new BigDecimal(value))==0)) {
					hitCondition=true;
				}else {
					hitCondition=false;
				}

				if(hitCondition) {
					basePoint=new BigDecimal(conditionWeight).multiply(basePoint);
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
				
				Integer conditonType=ruleCondition.getConditionType();//区间: 60,  大于等于 20 大于  30, 小于等于 40, 小于 50, 等于 10
				boolean hitCondition=false;
				if(conditonType.equals(60)&&(payment.compareTo(new BigDecimal(startValue))>=0&&payment.compareTo(new BigDecimal(endValue))<0)) {
					hitCondition=true;
				}else if(conditonType.equals(20)&&(payment.compareTo(new BigDecimal(value))>=0)) {
					hitCondition=true;
				}else if(conditonType.equals(30)&&(payment.compareTo(new BigDecimal(value))>0)) {
					hitCondition=true;
				}else if(conditonType.equals(40)&&(payment.compareTo(new BigDecimal(value))<=0)) {
					hitCondition=true;
				}else if(conditonType.equals(50)&&(payment.compareTo(new BigDecimal(value))<0)) {
					hitCondition=true;
				}else if(conditonType.equals(10)&&(payment.compareTo(new BigDecimal(value))==0)) {
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
