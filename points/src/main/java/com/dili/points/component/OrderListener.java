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
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
import com.dili.points.service.CategoryService;
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
 * 积分监听组件 Created by asiamaster on 2017/11/7 0007.
 */
@Component
@ConditionalOnExpression("'${mq.enable}'=='true'")
public class OrderListener {
	private static final Logger logger = LoggerFactory.getLogger(OrderListener.class);
	@Autowired
	OrderService orderService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	PointsRuleService pointsRuleService;
	@Autowired
	DataDictionaryValueProvider dataDictionaryValueProvider;
	@Autowired
	RuleConditionService ruleConditionService;
	@Autowired
	PointsDetailService pointsDetailService;
	@Autowired
	CustomerRpc customerRpc;
	@Autowired
	CategoryService categoryService;

	@RabbitListener(queues = "#{rabbitConfiguration.ORDER_TOPIC_QUEUE}")
	public void processBootTask(String orderJson) {
		try {
			Map<Order, List<OrderItem>> orderMap = this.convertOrder(orderJson);
			if (orderMap.isEmpty()) {
				logger.error("收到的交易订单信息格式错误: " + orderJson);
				return;
			}
			if (!this.checkData(orderMap)) {
				logger.error("收到的交易订单信息数据错误: " + orderJson);
				return;
			}
			this.calAndSaveData(orderMap);
		} catch (Exception e) {
			logger.error("根据订单信息" + orderJson + ",计算积分出错", e);
		}
	}

	protected boolean checkData(Map<Order, List<OrderItem>> orderMap) {
		for (Order order : orderMap.keySet()) {
			List<OrderItem> orderItems = orderMap.get(order);
			if (order.getBusinessType() == null) {
				return false;
			}
			if (StringUtils.trimToNull(order.getSourceSystem()) == null) {
				return false;
			}
			if (order.getPayment() == null) {
				return false;
			}
			if (order.getPayDate() == null) {
				return false;
			}
			if (order.getWeight() == null) {
				return false;
			}
			if (order.getTotalMoney() == null) {
				return false;
			}
			if (order.getBuyerCardNo() == null) {
				return false;
			}
			if (order.getSellerCardNo() == null) {
				return false;
			}
			if (StringUtils.trimToNull(order.getSellerCertificateNumber()) == null) {
				return false;
			}
			if (StringUtils.trimToNull(order.getCode()) == null) {
				return false;
			}
			if (orderItems == null || orderItems.isEmpty()) {
				return false;
			}
			for (OrderItem orderItem : orderItems) {
				if (orderItem.getTotalMoney() == null) {
					return false;
				}
				if (orderItem.getWeight() == null) {
					return false;
				}

			}

		}

		return true;
	}

	/**
	 * 将Json转换为dto数据结构
	 * 
	 * @param json
	 *            原始json字符串
	 * @return 订单列表Map
	 */
	protected Map<Order, List<OrderItem>> convertOrder(String json) {
		Map<Order, List<OrderItem>> resultMap = new HashMap<>();
		Map<String, Object> jsonMap = DtoMessageConverter.convertAsMap(json);
		String type = StringUtils.trimToNull(String.valueOf(jsonMap.get("type")));
		if (type == null || type.equalsIgnoreCase("json")) {
			List<Map<String, Object>> dataMap = (List<Map<String, Object>>) jsonMap.get("data");
			for (Map<String, Object> map : dataMap) {

				List<Map<String, Object>> orderItemsObjs = (List<Map<String, Object>>) map.remove("orderItems");
				Order order = DTOUtils.proxy(new DTO(map), Order.class);

				List<OrderItem> orderItemsList = orderItemsObjs.stream().map(m -> {
					return DTOUtils.proxy(new DTO(m), OrderItem.class);
				}).collect(Collectors.toList());

				resultMap.put(order, orderItemsList);
			}
			return resultMap;
		} else {
			return Collections.emptyMap();
		}

	}

	/**
	 * 进行积分的计算和数据保存
	 * 
	 * @param orderMap
	 */
	protected void calAndSaveData(Map<Order, List<OrderItem>> orderMap) {
		// 对所有订单进行合计(针对买家)得到买家订单
		Map<Order, List<OrderItem>> purchaseOrdersMap = this.sumOrdersForPurchase(orderMap);

		// 卖家订单
		Map<Order, List<OrderItem>> saleOrdersMap = orderMap;

		// 计算买家积分
		List<PointsDetailDTO> purchasePointsDetails = this.calPoints(purchaseOrdersMap, "purchase");
		// 计算卖家积分
		List<PointsDetailDTO> salePointsDetails = this.calPoints(saleOrdersMap, "sale");
		this.saveOrdersAndPointsDetailsData(orderMap, purchasePointsDetails, salePointsDetails);
	}

	/**
	 * 进行积分数据保存
	 * 
	 * @param orderMap
	 */
	@Transactional(timeout = 90, propagation = Propagation.REQUIRED)
	public void saveOrdersAndPointsDetailsData(Map<Order, List<OrderItem>> orderMap,
			List<PointsDetailDTO> purchasePointsDetails, List<PointsDetailDTO> salePointsDetails) {

		// 保存订单信息
		orderMap.forEach((order, orderItemList) -> {
			if (this.orderIsExists(order)) {
				throw new AppException("保存订单信息出错:当前订单号已经在数据库存在");
			}
			;
//			List<OrderItem> items = orderMap.get(order);
			this.orderService.insertSelective(order);

			// 保存订单列表
			for (OrderItem item : orderItemList) {
				item.setOrderCode(order.getCode());
			}
			this.orderItemService.batchInsert(orderItemList);
		});
//		for (Order order : orderMap.keySet()) {
//			if (this.orderIsExists(order)) {
//				throw new AppException("保存订单信息出错:当前订单号已经在数据库存在");
//			}
//			;
//			List<OrderItem> items = orderMap.get(order);
//			this.orderService.insertSelective(order);
//
//			// 保存订单列表
//			for (OrderItem item : items) {
//				item.setOrderCode(order.getCode());
//			}
//			this.orderItemService.batchInsert(items);
//		}

		// 保存买家的积分详情
		this.pointsDetailService.batchInsertPointsDetailDTO(purchasePointsDetails);
		// 保存卖家的积分详情
		this.pointsDetailService.batchInsertPointsDetailDTO(salePointsDetails);

	}

	/**
	 * 根据证件号查询id
	 * 
	 * @param certificateNumber
	 *            证件号
	 * @return
	 */
	protected Long findIdByCertificateNumber(String certificateNumber) {
		CustomerApiDTO dto = DTOUtils.newDTO(CustomerApiDTO.class);
		dto.setCertificateNumber(certificateNumber);
		try {
			BaseOutput<List<Customer>> baseOut = this.customerRpc.list(dto);
			if (baseOut.isSuccess() && !baseOut.getData().isEmpty()) {
				return baseOut.getData().get(0).getId();
			} /*
				 * else { throw new AppException("根据证件号:"+certificateNumber+",没有查询到客户信息"); }
				 */
		} catch (Exception e) {
			// throw new AppException("查询客户信息出错:"+certificateNumber,e);
		}
		return null;

	}

	/**
	 * 根据客户类型查询可用的积分规则
	 * 
	 * @param customerType
	 *            (卖家sale,买家purchase)
	 * @return 返回规则
	 */
	protected Optional<PointsRule> findPointsRule(String customerType) {
		PointsRule pointsRuleEx = DTOUtils.newDTO(PointsRule.class);
		pointsRuleEx.setYn(1);
		pointsRuleEx.setCustomerType(customerType);
		pointsRuleEx.setBusinessType(10);// 10交易,20充值,30开卡
		return this.pointsRuleService.listByExample(pointsRuleEx).stream().findFirst();
	}

	/**
	 * 查询规则条件
	 * 
	 * @param pointsRuleId
	 *            规则id
	 * @param conditionType
	 *            条件类型(交易量 10 交易额 20 商品 30 支付方式:40)
	 * @return 返回可用的条件
	 */
	protected List<RuleCondition> findRuleCondition(Long pointsRuleId, Integer conditionType) {
		RuleCondition condition = DTOUtils.newDTO(RuleCondition.class);
		condition.setConditionType(conditionType);// 交易量 10 交易额 20 商品 30 支付方式:40
		condition.setPointRuleId(pointsRuleId);
		condition.setSort("modified");
		condition.setOrder("DESC");
		
		List<RuleCondition> list = ruleConditionService.listByExample(condition);
		return list;
	}

	/**
	 * 将多个订单的金额,重量 等计量信息合计
	 * 
	 * @param orderList
	 *            多个订单信息
	 * @return 合计订单
	 */
	protected Map<Order, List<OrderItem>> sumOrdersForPurchase(Map<Order, List<OrderItem>> orderMap) {
		Collection<Order> orderList = orderMap.keySet();
		// 如果只有一个订单直接返回
		if (orderList.size() == 1) {
			return orderMap;
		}

		Order order = DTOUtils.newDTO(Order.class);
		order.setTotalMoney(0L);
		order.setWeight(BigDecimal.ZERO);

		// 多个订单进行数据合计(金额,重量)
		for (Order orderObj : orderList) {
			order.setTotalMoney(orderObj.getTotalMoney() + order.getTotalMoney());
			order.setWeight(order.getWeight().add(orderObj.getWeight()));
			order.setPayment(orderObj.getPayment());

			order.setBusinessType(orderObj.getBusinessType());
			order.setBuyerCardNo(orderObj.getBuyerCardNo());
			order.setBuyerCertificateNumber(orderObj.getBuyerCertificateNumber());

			order.setPayment(orderObj.getPayment());
			// order.setSellerCardNo(orderObj.getSellerCardNo());
			// order.setSellerCertificateNumber(orderObj.getSellerCertificateNumber());
			// order.setCode(orderObj.getCode());
			order.setSettlementCode(orderObj.getSettlementCode());
			order.setSourceSystem(orderObj.getSourceSystem());
		}
		Map<Order, List<OrderItem>> map = new HashMap<>();
		List<OrderItem> itemList = orderMap.values().stream().flatMap(list -> list.stream())
				.collect(Collectors.toList());
		map.put(order, itemList);
		return map;
	}

	/**
	 * 根据规则和订单信息计算出基础积分
	 * 
	 * @param pointsRule
	 *            积分规则
	 * @param order
	 *            订单信息
	 * @return 计算出的基础积分
	 */
	protected BigDecimal calculateBasePoints(PointsRule pointsRule, Order order) {
		BigDecimal orderWeight = order.getWeight();// 交易量

		BigDecimal totalMoney = new BigDecimal(order.getTotalMoney()).divide(new BigDecimal("100"));// 交易额(除以100,转换单位为元)

		Integer computingStandard = pointsRule.getComputingStandard();
		Float computingParameter = pointsRule.getComputingParameter();
		// 根据基准设置,计算基础积分值
		BigDecimal basePoint = BigDecimal.ZERO;

		// 10 交易量,20 交易额,30 固定值
		// 积分计算
		if (computingStandard == 10) {
			basePoint = (orderWeight).multiply(new BigDecimal(computingParameter));
		} else if (computingStandard == 20) {
			basePoint = (totalMoney).multiply(new BigDecimal(computingParameter));
		} else if (computingStandard == 30) {
			basePoint = (new BigDecimal(computingParameter));
		} else {
			return BigDecimal.ZERO;
		}
		return basePoint;
	}

	protected boolean isSettlementOrder(Order order) {
		if (StringUtils.isNoneBlank(order.getSettlementCode())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据订单类型和订单号,以及订单列表,拼装备注信息
	 * 
	 * @param pointsDetail
	 *            积分详情
	 * @param orderItemList
	 *            对应的订单列表
	 * @return 返回备注信息
	 */
	protected String findNotes(Order order, List<OrderItem> orderItemList) {

		StringBuffer notesStr = new StringBuffer();

		List<String> categoryNameList = new ArrayList<>();
		// 遍历列表,获得品类名称
		for (OrderItem orderItem : orderItemList) {
			// 只取前四个品类名
			if (categoryNameList.size() >= 4) {
				break;
			}

			// 查询品类
			Category category = categoryService.get(orderItem.getCategoryId());
			if (category != null) {
				categoryNameList.add(category.getName());
			}
		}
		if (!categoryNameList.isEmpty()) {
			notesStr.append("交易");
			if (categoryNameList.size() <= 3) {
				// 不超过三个时候,全部显示
				notesStr.append(String.join(",", categoryNameList));
			} else {
				// 取前三个,用逗号相连,并在后面连接三个点
				categoryNameList = categoryNameList.subList(0, categoryNameList.size() - 1);
				notesStr.append(String.join(",", categoryNameList)).append("...");
			}
			// 将品类名称和订单号分隔
			notesStr.append("; ");
		}

		if (this.isSettlementOrder(order)) {
			notesStr.append("结算单号:").append(order.getSettlementCode());
		} else {
			notesStr.append("订单号:").append(order.getCode());
		}

		return notesStr.toString();
	}

	protected boolean orderIsExists(Order order) {
		Order example = DTOUtils.newDTO(Order.class);
		example.setCode(order.getCode());
		example.setSettlementCode(order.getSettlementCode());
		example.setSourceSystem(order.getSourceSystem());
		boolean isNotEmpty = !this.orderService.listByExample(example).isEmpty();
		return isNotEmpty;

	}

	protected boolean isPurchase(String customerType) {
		if ("sale".equals(customerType)) {
			return false;
		} else {
			return true;
		}
	}

	// 卖家sale,买家purchase
	private List<PointsDetailDTO> calPoints(Map<Order, List<OrderItem>> orderMap, String customerType) {
		boolean isPurchase = this.isPurchase(customerType);
//		if ("sale".equals(customerType)) {
//			isPurchase = false;
//		} else if ("purchase".equals(customerType)) {
//			isPurchase = true;
//		} else {
//			return Collections.emptyList();
//		}
		PointsRule pointsRule = this.findPointsRule(customerType).orElse(null);
		if (pointsRule == null) {
			return Collections.emptyList();
		}

		// 根据 交易量,交易额 ,支付方式 的权重计算总积分

		// 交易量 10 交易额 20 商品 30 支付方式:40
		List<RuleCondition> tradeWeightConditionList = this.findRuleCondition(pointsRule.getId(), 10);
		List<RuleCondition> tradeTotalMoneyConditionList = this.findRuleCondition(pointsRule.getId(), 20);
		List<RuleCondition> tradeTypeConditionList = this.findRuleCondition(pointsRule.getId(), 40);

		List<PointsDetailDTO> pointsDetailList = new ArrayList<>();

		orderMap.forEach((order, orderItemList) -> {

		
		// for (Order order : orderMap.keySet()) {
		//List<OrderItem> orderItemList = orderMap.get(order);

		BigDecimal basePoint = this.calculateBasePoints(pointsRule, order);

		PointsDetailDTO pointsDetail = DTOUtils.newDTO(PointsDetailDTO.class);
		pointsDetail.setPoints(basePoint.intValue());
		pointsDetail.setSourceSystem(order.getSourceSystem());

		if (this.isSettlementOrder(order)) {
			pointsDetail.setOrderCode(order.getSettlementCode());
			pointsDetail.setOrderType("settlementOrder");// settlementOrder结算单号,order主单
		} else {
			pointsDetail.setOrderCode(order.getCode());
			pointsDetail.setOrderType("order");// settlementOrder结算单号,order主单
		}
		//
		// if(StringUtils.isNoneBlank(order.getSettlementCode())) {
		// pointsDetail.setOrderCode(order.getSettlementCode());
		// pointsDetail.setOrderType("settlementOrder");//settlementOrder结算单号,order主单
		// }else{
		// pointsDetail.setOrderCode(order.getCode());
		// pointsDetail.setOrderType("order");//settlementOrder结算单号,order主单
		// }
		String notes = this.findNotes(order, orderItemList);
		pointsDetail.setNotes(notes);

		String certificateNumber = null;
		// buyer
		if (isPurchase) {
			certificateNumber = order.getBuyerCertificateNumber();
		} else {
			certificateNumber = order.getSellerCertificateNumber();
		}
		// 交易10 充值20 兑换礼品30 扣减:40 手工调整50
		pointsDetail.setGenerateWay(10);// 积分产生方式(当前固定为交易)
		pointsDetail.setCertificateNumber(certificateNumber);
		pointsDetail.setSourceSystem(order.getSourceSystem());

		// 如果买家或者卖家的证件号为空,则不进行积分
		if (StringUtils.isBlank(certificateNumber)) {
			// pointsDetail.setCertificateNumber(certificateNumber);
			// pointsDetail.setNeedRecover(0);
			// pointsDetail.setPoints(0);
			// pointsDetailList.add(pointsDetail);
//			continue;
			return;
		}
		// try {
		Long customerId = this.findIdByCertificateNumber(certificateNumber);
		if (customerId != null) {
			pointsDetail.setCustomerId(customerId);
		} else {
			pointsDetail.setNeedRecover(1);// 将会保存到异常积分表,并在某个时间进行恢复
		}

		// }catch(Exception e){

		// logger.warn("查询客户ID出错,当前积分详情将会被保存到异常积分!");
		// }
		BigDecimal orderWeight = order.getWeight();// 交易量
		BigDecimal totalMoney = new BigDecimal(order.getTotalMoney()).divide(new BigDecimal("100"));// 交易额
		BigDecimal payment = new BigDecimal(order.getPayment());// 支付方式

		// 三个量值与对应的条件列表匹配权重值
		List<BigDecimal> weightList = Arrays.asList(this.calculateWeight(orderWeight, tradeWeightConditionList),
				this.calculateWeight(totalMoney, tradeTotalMoneyConditionList),
				this.calculateWeight(payment, tradeTypeConditionList));
		// 计算积分值
		BigDecimal points = weightList.stream().reduce(basePoint, (t, u) -> t.multiply(u));
		// System.out.println(points.intValue());
		/*
		 * //根据交易量计算积分
		 * 
		 * for(RuleCondition ruleCondition:tradeWeightConditionList) {
		 * 
		 * Float conditionWeight=ruleCondition.getWeight();//权重 String
		 * startValue=ruleCondition.getStartValue();//开始值 String
		 * endValue=ruleCondition.getEndValue();//结束值 String
		 * value=ruleCondition.getValue();//条件值
		 * 
		 * Integer conditonType=ruleCondition.getConditionType();//区间: 60, 大于等于 20 大于
		 * 30, 小于等于 40, 小于 50, 等于 10 //是否进行权重*积分的计算过程 boolean hitCondition=false;
		 * if(conditonType.equals(60)&&(orderWeight.compareTo(new
		 * BigDecimal(startValue))>=0&&orderWeight.compareTo(new
		 * BigDecimal(endValue))<0)) {//区间 hitCondition=true; }else
		 * if(conditonType.equals(20)&&(orderWeight.compareTo(new
		 * BigDecimal(value))>=0)) {//大于等于 hitCondition=true; }else
		 * if(conditonType.equals(30)&&(orderWeight.compareTo(new BigDecimal(value))>0))
		 * {//大于 hitCondition=true; }else
		 * if(conditonType.equals(40)&&(orderWeight.compareTo(new
		 * BigDecimal(value))<=0)) {//小于等于 hitCondition=true; }else
		 * if(conditonType.equals(50)&&(orderWeight.compareTo(new BigDecimal(value))<0))
		 * {// 小于 hitCondition=true; }else
		 * if(conditonType.equals(10)&&(orderWeight.compareTo(new
		 * BigDecimal(value))==0)) {//等于 hitCondition=true; }else { hitCondition=false;
		 * } //对当前条件进行积分 if(hitCondition) { //如果有对上一个条件进行了积分,则当前积分:原来积分*权重*条件值,否则:权重*条件值
		 * basePoint=new BigDecimal(conditionWeight).multiply(basePoint); break; } }
		 * 
		 * 
		 * //根据交易额计算积分
		 * 
		 * // 交易量 10 交易额 20 商品 30 支付方式:40 for(RuleCondition
		 * ruleCondition:tradeTotalMoneyConditionList) {
		 * 
		 * 
		 * Float conditionWeight=ruleCondition.getWeight();//权重 String
		 * startValue=ruleCondition.getStartValue();//开始值 String
		 * endValue=ruleCondition.getEndValue();//结束值 String
		 * value=ruleCondition.getValue();//条件值
		 * 
		 * Integer conditonType=ruleCondition.getConditionType();//区间: 60, 大于等于 20 大于
		 * 30, 小于等于 40, 小于 50, 等于 10 //是否进行权重*积分的计算过程 boolean hitCondition=false;
		 * if(conditonType.equals(60)&&(totalMoney.compareTo(new
		 * BigDecimal(startValue))>=0&&totalMoney.compareTo(new
		 * BigDecimal(endValue))<0)) { hitCondition=true; }else
		 * if(conditonType.equals(20)&&(totalMoney.compareTo(new BigDecimal(value))>=0))
		 * { hitCondition=true; }else
		 * if(conditonType.equals(30)&&(totalMoney.compareTo(new BigDecimal(value))>0))
		 * { hitCondition=true; }else
		 * if(conditonType.equals(40)&&(totalMoney.compareTo(new BigDecimal(value))<=0))
		 * { hitCondition=true; }else
		 * if(conditonType.equals(50)&&(totalMoney.compareTo(new BigDecimal(value))<0))
		 * { hitCondition=true; }else
		 * if(conditonType.equals(10)&&(totalMoney.compareTo(new BigDecimal(value))==0))
		 * { hitCondition=true; }else { hitCondition=false; }
		 * 
		 * if(hitCondition) { basePoint=new
		 * BigDecimal(conditionWeight).multiply(basePoint); break; }
		 * 
		 * }
		 * 
		 * 
		 * //根据支付方式计算积分
		 * 
		 * for(RuleCondition ruleCondition:tradeTypeConditionList) {
		 * 
		 * Float conditionWeight=ruleCondition.getWeight();//权重 String
		 * startValue=ruleCondition.getStartValue();//开始值 String
		 * endValue=ruleCondition.getEndValue();//结束值 String
		 * value=ruleCondition.getValue();//条件值
		 * 
		 * Integer conditonType=ruleCondition.getConditionType();//区间: 60, 大于等于 20 大于
		 * 30, 小于等于 40, 小于 50, 等于 10 //是否进行权重*积分的计算过程 boolean hitCondition=false;
		 * if(conditonType.equals(60)&&(payment.compareTo(new
		 * BigDecimal(startValue))>=0&&payment.compareTo(new BigDecimal(endValue))<0)) {
		 * hitCondition=true; }else if(conditonType.equals(20)&&(payment.compareTo(new
		 * BigDecimal(value))>=0)) { hitCondition=true; }else
		 * if(conditonType.equals(30)&&(payment.compareTo(new BigDecimal(value))>0)) {
		 * hitCondition=true; }else if(conditonType.equals(40)&&(payment.compareTo(new
		 * BigDecimal(value))<=0)) { hitCondition=true; }else
		 * if(conditonType.equals(50)&&(payment.compareTo(new BigDecimal(value))<0)) {
		 * hitCondition=true; }else if(conditonType.equals(10)&&(payment.compareTo(new
		 * BigDecimal(value))==0)) { hitCondition=true; }else { hitCondition=false; }
		 * 
		 * if(hitCondition) { basePoint=new
		 * BigDecimal(conditionWeight).multiply(basePoint); break; } }
		 */
		pointsDetail.setPoints(points.intValue());
		pointsDetailList.add(pointsDetail);
	//}
		});
	return pointsDetailList;

	}

	/**
	 * 根据标准值和条件表表,选出条件的第一个权重值
	 * 
	 * @param conditionNumber
	 * @param ruleConditionList
	 * @return
	 */
	protected BigDecimal calculateWeight(BigDecimal conditionNumber, List<RuleCondition> ruleConditionList) {

		for (RuleCondition ruleCondition : ruleConditionList) {

			Float conditionWeight = ruleCondition.getWeight();// 权重
			String startValue = ruleCondition.getStartValue();// 开始值
			String endValue = ruleCondition.getEndValue();// 结束值
			String value = ruleCondition.getValue();// 条件值

			Integer conditonType = ruleCondition.getConditionType();// 区间: 60, 大于等于 20 大于 30, 小于等于 40, 小于 50, 等于 10
			// 是否进行权重*积分的计算过程
			boolean hitCondition = false;

			// 区间包括两端的值
			if (conditonType.equals(60) && (conditionNumber.compareTo(new BigDecimal(startValue)) >= 0
					&& conditionNumber.compareTo(new BigDecimal(endValue)) <= 0)) {
				hitCondition = true;
			} else if (conditonType.equals(20) && (conditionNumber.compareTo(new BigDecimal(value)) >= 0)) {
				hitCondition = true;
			} else if (conditonType.equals(30) && (conditionNumber.compareTo(new BigDecimal(value)) > 0)) {
				hitCondition = true;
			} else if (conditonType.equals(40) && (conditionNumber.compareTo(new BigDecimal(value)) <= 0)) {
				hitCondition = true;
			} else if (conditonType.equals(50) && (conditionNumber.compareTo(new BigDecimal(value)) < 0)) {
				hitCondition = true;
			} else if (conditonType.equals(10) && (conditionNumber.compareTo(new BigDecimal(value)) == 0)) {
				hitCondition = true;
			} else {
				hitCondition = false;
			}
			// 如果有匹配的权重就返回否则返回1
			if (hitCondition) {
				return new BigDecimal(conditionWeight);
			}
		}

		return BigDecimal.ONE;
	}

}
