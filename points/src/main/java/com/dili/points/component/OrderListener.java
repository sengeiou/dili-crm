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

import com.dili.points.converter.DtoMessageConverter;
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
import com.dili.ss.exception.AppException;

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
	@RabbitListener(queues = "#{rabbitConfiguration.ORDER_TOPIC_QUEUE}")
	public void processBootTask(String orderJson) {
		try {
			Map<Order,List<OrderItem>>orderMap=this.convertOrder(orderJson);
			if(orderMap.isEmpty()){
				logger.error("收到的交易订单信息格式错误: "+orderJson);
				return;
			}
			this.calPointsAndSaveData(orderMap);
		}catch(Exception e) {
			logger.error("根据订单信息"+orderJson+",计算积分出错",e);
		}
	}
	
	/** 将Json转换为dto数据结构
	 * @param json 原始json字符串
	 * @return 订单列表Map
	 */
	protected Map<Order,List<OrderItem>>convertOrder(String json){
		Map<Order,List<OrderItem>>resultMap=new HashMap<>();
		List<Map<String,Object>>list=DtoMessageConverter.convertAsList(json);
		for(Map<String,Object> map:list) {
			
			List<Object> orderItemsObjs=(List<Object>) map.remove("orderItems");
			Order order=DTOUtils.as(map, Order.class);
			List<OrderItem>orderItemsList=DTOUtils.as(orderItemsObjs, OrderItem.class);
			resultMap.put(order, orderItemsList);
		}
		return resultMap;
		
	}
	/** 进行积分的计算和数据保存
	 * @param orderMap
	 */
	@Transactional(timeout=90,propagation=Propagation.REQUIRED)
	public void calPointsAndSaveData(Map<Order,List<OrderItem>>orderMap) {
		//对所有订单进行合计(针对买家)得到买家订单
		Collection<Order>purchaseOrderList=this.sumOrdersForPurchase(orderMap.keySet());
		
		//卖家订单
		Collection<Order>saleOrderList=orderMap.keySet();
		
		//计算买家积分
		List<PointsDetailDTO>purchasePointsDetails=this.calPoints(purchaseOrderList, "purchase");
		//计算卖家积分
		List<PointsDetailDTO>salePointsDetails=this.calPoints(saleOrderList, "sale");
		
		//保存订单信息
		for(Order order:orderMap.keySet()) {
			List<OrderItem>items=orderMap.get(order);
			this.orderService.insertSelective(order);
			
			//保存订单列表
			for(OrderItem item:items) {
				item.setOrderCode(order.getCode());
			}
			this.orderItemService.batchInsert(items);
		}
		
		//保存买家的积分详情
		this.pointsDetailService.batchInsertPointsDetailDTO(purchasePointsDetails);
		//保存卖家的积分详情
		this.pointsDetailService.batchInsertPointsDetailDTO(salePointsDetails);
		
	}

	/** 根据证件号查询id
	 * @param certificateNumber 证件号
	 * @return
	 */
	protected Long findIdByCertificateNumber(String certificateNumber) {
		CustomerApiDTO dto=DTOUtils.newDTO(CustomerApiDTO.class);
		dto.setCertificateNumber(certificateNumber);
		try {
			BaseOutput<List<Customer>>baseOut=this.customerRpc.list(dto);
			if(baseOut.isSuccess()&&!baseOut.getData().isEmpty()) {
				return baseOut.getData().get(0).getId();
			}else {
				throw new AppException("根据证件号:"+certificateNumber+",没有查询到客户信息");
			}
		}catch(Exception e) {
			throw new AppException("查询客户信息出错:"+certificateNumber,e);
		}
		
	}
	
	/** 根据客户类型查询可用的积分规则
	 * @param customerType (卖家sale,买家purchase)
	 * @return 返回规则
	 */
	protected Optional<PointsRule> findPointsRule(String customerType) {
		PointsRule pointsRuleEx=DTOUtils.newDTO(PointsRule.class);
		pointsRuleEx.setYn(1);
		pointsRuleEx.setCustomerType(customerType);
		pointsRuleEx.setBusinessType(10);//10交易,20充值,30开卡
		return this.pointsRuleService.listByExample(pointsRuleEx).stream().findFirst();
	}
	
	/** 查询规则条件
	 * @param pointsRuleId 规则id
	 * @param conditionType 条件类型(交易量 10 交易额 20  商品 30 支付方式:40)
	 * @return 返回可用的条件
	 */
	protected List<RuleCondition> findRuleCondition(Long pointsRuleId,Integer conditionType) {
		RuleCondition condition=DTOUtils.newDTO(RuleCondition.class);
		condition.setConditionType(conditionType);//  交易量 10 交易额 20  商品 30 支付方式:40
		condition.setPointRuleId(pointsRuleId);
		List<RuleCondition>list=ruleConditionService.listByExample(condition);
		return list;
	}
	
	/**
	 * 将多个订单的金额,重量 等计量信息合计
	 * @param orderList 多个订单信息
	 * @return 合计订单
	 */
	protected List<Order> sumOrdersForPurchase(Collection<Order> orderList){
		//如果只有一个订单直接返回
		if(orderList.size()==1) {
			return orderList.stream().collect(Collectors.toList());
		}
		
		
		Order order=DTOUtils.newDTO(Order.class);
		order.setTotalMoney(0L);
		order.setWeight(BigDecimal.ZERO);
		
		//多个订单进行数据合计(金额,重量)
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
	
	/** 根据规则和订单信息计算出基础积分
	 * @param pointsRule 积分规则 
	 * @param order 订单信息
	 * @return 计算出的基础积分
	 */
	protected BigDecimal calculateBasePoints(PointsRule pointsRule,Order order) {
		BigDecimal orderWeight=order.getWeight();//交易量
		
		BigDecimal totalMoney=new BigDecimal(order.getTotalMoney());//交易额
		
		Integer computingStandard=pointsRule.getComputingStandard();
		Float computingParameter=pointsRule.getComputingParameter();
		//根据基准设置,计算基础积分值
		BigDecimal basePoint=BigDecimal.ZERO;
		
		//10 交易量,20 交易额,30 固定值
		//积分计算
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
			pointsDetail.setGenerateWay(10);//积分产生方式(当前固定为交易)
			pointsDetail.setCertificateNumber(certificateNumber);
			pointsDetail.setSourceSystem(order.getSourceSystem());

			//如果买家或者卖家的证件号为空,则不进行积分
			if(StringUtils.isBlank(certificateNumber)) {
				break;
			}
			Long customerId=this.findIdByCertificateNumber(certificateNumber);
			pointsDetail.setCustomerId(customerId);
			
			//根据交易量计算积分
		
			for(RuleCondition ruleCondition:tradeWeightConditionList) {
				
				Float conditionWeight=ruleCondition.getWeight();//权重
				String startValue=ruleCondition.getStartValue();//开始值
				String endValue=ruleCondition.getEndValue();//结束值
				String value=ruleCondition.getValue();//条件值
				
				Integer conditonType=ruleCondition.getConditionType();//区间: 60,  大于等于 20 大于  30, 小于等于 40, 小于 50, 等于 10
				//是否进行权重*积分的计算过程
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
				//是否进行权重*积分的计算过程
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
				//是否进行权重*积分的计算过程
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
