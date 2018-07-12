package com.dili.points.component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
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
import com.dili.points.domain.dto.CustomerCategoryPointsDTO;
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
	public void processBootTask(Message message) throws UnsupportedEncodingException {
		
		logger.info("收到消息: "+message);
		String orderJson=new String(message.getBody(),"UTF-8");
		try {
			Map<Order, List<OrderItem>> orderMap = this.convertOrder(orderJson);
			if (orderMap.isEmpty()) {
				logger.error("收到的交易订单信息格式错误: " + orderJson);
				return;
			}
			if (!this.checkData(orderMap)) {
				logger.error("收到的交易订单信息数据错误: " + orderJson);
				List<PointsDetailDTO> list=this.exceptionalPointsDetails(orderMap, orderJson);
				this.pointsDetailService.batchInsertPointsDetailDTO(list);
				return;
			}
			this.calAndSaveData(orderMap);
		} catch (Exception e) {
			logger.error("根据订单信息" + orderJson + ",计算积分出错", e);
			PointsDetailDTO detailDTO=DTOUtils.newDTO(PointsDetailDTO.class);
			detailDTO.setNotes("异常信息:"+e.getMessage()+",数据:"+orderJson);
			detailDTO.setException(1);
			detailDTO.setNeedRecover(0);
			try {
				this.pointsDetailService.insert(detailDTO);
			}catch(Exception e2) {
				logger.error("保存异常信息出错:"+e2.getMessage(),e2);	
			}
		}
	}
	protected List<PointsDetailDTO> exceptionalPointsDetails(Map<Order, List<OrderItem>> orderMap,String orderJson) {
		List<PointsDetailDTO>list=new ArrayList<>();
		orderMap.forEach((order,item)->{
			PointsDetailDTO detailDTO=DTOUtils.newDTO(PointsDetailDTO.class);
			
			if(this.isSettlementOrder(order)) {
				detailDTO.setOrderCode(order.getSettlementCode());
			}else {
				detailDTO.setOrderCode(order.getCode());
			}
			detailDTO.setSourceSystem(order.getSourceSystem());
			detailDTO.setException(1);
			detailDTO.setNotes("数据格式错误:"+orderJson);
			detailDTO.setNeedRecover(0);
			list.add(detailDTO);
		});
		return list;
		
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
			if (StringUtils.trimToNull(order.getMarket()) == null) {
				return false;
			}
//			if (order.getBuyerCardNo() == null) {
//				return false;
//			}
//			if (order.getSellerCardNo() == null) {
//				return false;
//			}
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
		 Map<PointsDetailDTO,List<CustomerCategoryPointsDTO>> purchasePointsDetails = this.calPoints(purchaseOrdersMap, "purchase");
		// 计算卖家积分
		 Map<PointsDetailDTO,List<CustomerCategoryPointsDTO>> salePointsDetails = this.calPoints(saleOrdersMap, "sale");
		this.saveOrdersAndPointsDetailsData(orderMap, purchasePointsDetails, salePointsDetails);
	}

	/**
	 * 进行积分数据保存
	 * 
	 * @param orderMap
	 */
	@Transactional(timeout = 90, propagation = Propagation.REQUIRED)
	public void saveOrdersAndPointsDetailsData(Map<Order, List<OrderItem>> orderMap,
			Map<PointsDetailDTO,List<CustomerCategoryPointsDTO>> purchasePointsDetails, Map<PointsDetailDTO,List<CustomerCategoryPointsDTO>> salePointsDetails) {

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
	protected Customer findIdByCertificateNumber(String certificateNumber) {
		CustomerApiDTO dto = DTOUtils.newDTO(CustomerApiDTO.class);
		dto.setYn(1);
		dto.setCertificateNumber(certificateNumber);
		try {
			BaseOutput<List<Customer>> baseOut = this.customerRpc.list(dto);
			if (baseOut.isSuccess() && !baseOut.getData().isEmpty()) {
				return baseOut.getData().get(0);
			} /*
				 * else { throw new AppException("根据证件号:"+certificateNumber+",没有查询到客户信息"); }
				 */
		} catch (Exception e) {
			// throw new AppException("查询客户信息出错:"+certificateNumber,e);
		}
		logger.warn("未能查询到客户信息.证件号:"+certificateNumber);
		return null;

	}

	/**
	 * 根据客户类型查询可用的积分规则
	 * 
	 * @param customerType
	 *            (卖家sale,买家purchase)
	 * @return 返回规则
	 */
	protected Optional<PointsRule> findPointsRule(String customerType, Integer businessType) {
		PointsRule pointsRuleEx = DTOUtils.newDTO(PointsRule.class);
		pointsRuleEx.setYn(1);
		pointsRuleEx.setCustomerType(customerType);
		pointsRuleEx.setBusinessType(businessType);// 10交易,20充值,30开卡
		return this.pointsRuleService.listByExample(pointsRuleEx).stream().findFirst();
	}

	/**
	 * 查询规则条件
	 * 
	 * @param pointsRuleId
	 *            规则id
	 * @param weightType
	 *            权重类型(交易量 10 交易额 20 商品 30 支付方式:40)
	 * @return 返回可用的条件
	 */
	protected List<RuleCondition> findRuleCondition(Long pointsRuleId, Integer weightType) {
		RuleCondition condition = DTOUtils.newDTO(RuleCondition.class);
		condition.setWeightType(weightType);// 交易量 10 交易额 20 商品 30 支付方式:40
		condition.setPointRuleId(pointsRuleId);
		condition.setSort("modified");
		condition.setOrder("DESC");
		
		List<RuleCondition> list = ruleConditionService.listByExample(condition);
		return list;
	}

	/**
	 * 将多个订单的金额,重量 等计量信息合计
	 * 
	 * @param orderMap
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
		String computingParameter = pointsRule.getComputingParameter();
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
	private Optional<Category> findCategory(Long categoryId,String sourceSystem) {
		if(categoryId==null||StringUtils.trimToNull(sourceSystem)==null) {
			return Optional.empty();
		}
		Category example=DTOUtils.newDTO(Category.class);
		example.setCategoryId(String.valueOf(categoryId));
		example.setSourceSystem(sourceSystem);
		return this.categoryService.listByExample(example).stream().findFirst();
	}
	/**
	 * 根据订单类型和订单号,以及订单列表,拼装备注信息
	 * 
	 * @param order
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
			Optional<Category>opt=this.findCategory(orderItem.getCategoryId(), order.getSourceSystem());
			if(opt.isPresent()) {
				categoryNameList.add(opt.get().getName());
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

	protected boolean isBuyer(String customerType) {
		if ("sale".equals(customerType)) {
			return false;
		} else {
			return true;
		}
	}

	// 卖家sale,买家purchase
	private Map<PointsDetailDTO,List<CustomerCategoryPointsDTO>> calPoints(Map<Order, List<OrderItem>> orderMap, String customerType) {
		boolean isBuyer = this.isBuyer(customerType);
//		if ("sale".equals(customerType)) {
//			isPurchase = false;
//		} else if ("purchase".equals(customerType)) {
//			isPurchase = true;
//		} else {
//			return Collections.emptyList();
//		}


		Map<PointsDetailDTO,List<CustomerCategoryPointsDTO>> pointsDetailListMap = new HashMap<>();

		orderMap.forEach((order, orderItemList) -> {
			PointsRule pointsRule = this.findPointsRule(customerType,order.getBusinessType()).orElse(null);
			if (isBuyer) {
				logger.info("对买家["+order.getBuyerCertificateNumber()+"]进行积分计算");
			} else {
				logger.info("对卖家["+order.getSellerCertificateNumber()+"]进行积分");
			}
			if (pointsRule == null) {
				logger.info("没有找到积分规则 customerType {},businessType {}",customerType,order.getBusinessType());
				return ;
			}
			logger.info("基于积分规则进行积分 code: "+pointsRule.getCode());
			// 根据 交易量,交易额 ,支付方式 的权重计算总积分

			// 交易量 10 交易额 20 商品 30 支付方式:40
			List<RuleCondition> tradeWeightConditionList = this.findRuleCondition(pointsRule.getId(), 10);
			List<RuleCondition> tradeTotalMoneyConditionList = this.findRuleCondition(pointsRule.getId(), 20);
			List<RuleCondition> tradeTypeConditionList = this.findRuleCondition(pointsRule.getId(), 40);
			// for (Order order : orderMap.keySet()) {
			//List<OrderItem> orderItemList = orderMap.get(order);
	
			BigDecimal basePoint = this.calculateBasePoints(pointsRule, order);
			
			PointsDetailDTO pointsDetail = DTOUtils.newDTO(PointsDetailDTO.class);
			pointsDetail.setPoints(basePoint.intValue());
			pointsDetail.setSourceSystem(order.getSourceSystem());
			pointsDetail.setFirmCode(order.getMarket());
			pointsDetail.setInOut(10);//收入
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
			pointsDetail.setCustomerType(customerType);
			if (isBuyer) {
				certificateNumber = order.getBuyerCertificateNumber();
				logger.info("对买家["+certificateNumber+"]进行积分计算");
			} else {
				certificateNumber = order.getSellerCertificateNumber();
				logger.info("对卖家["+certificateNumber+"]进行积分");
			}
			logger.info("基本积分值为:"+basePoint.toPlainString());
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
			Customer customer=this.findIdByCertificateNumber(certificateNumber);
			if (customer != null&&customer.getId()!=null) {
				pointsDetail.setCustomerId(customer.getId());
			} else {
				pointsDetail.setNotes("未能通过证件号["+certificateNumber+"]查询到客户信息");
				pointsDetail.setException(1);
				pointsDetail.setNeedRecover(1);// 将会保存到异常积分表,并在某个时间进行恢复
			}
	
			// }catch(Exception e){
	
			// logger.warn("查询客户ID出错,当前积分详情将会被保存到异常积分!");
			// }
			BigDecimal orderWeightValue = order.getWeight();// 交易量
			BigDecimal totalMoneyValue = new BigDecimal(order.getTotalMoney()).divide(new BigDecimal("100"));// 交易额
			BigDecimal paymentValue = new BigDecimal(order.getPayment());// 支付方式
	
			// 三个量值与对应的条件列表匹配权重值
			Entry<Boolean,BigDecimal>tradeWeightEntry=this.calculateWeight(orderWeightValue, tradeWeightConditionList);
			Entry<Boolean,BigDecimal>totalMoneyEntry=this.calculateWeight(totalMoneyValue, tradeTotalMoneyConditionList);
			Entry<Boolean,BigDecimal>paymentEntry=this.calculateWeight(paymentValue, tradeTypeConditionList);
			
			// 交易量 10 交易额 20 商品 30 支付方式:40
			

			Integer computingStandard = pointsRule.getComputingStandard();
			// 10 交易量,20 交易额,30 固定值
			if (computingStandard == 10) {
				logger.info("基于交易量进行积分计算 {}",certificateNumber);
				pointsDetail.setWeightType(10);
			} else if (computingStandard == 20) {
				logger.info("基于交易额进行积分计算 {}",certificateNumber);
				pointsDetail.setWeightType(20);
			} 
			
			List<BigDecimal> weightList = Arrays.asList(tradeWeightEntry.getValue(),
					totalMoneyEntry.getValue(),
					paymentEntry.getValue());
			logger.info("三个积分权重分别为:"+weightList);
			// 计算积分值
			BigDecimal points = weightList.stream().reduce(basePoint, (t, u) -> t.multiply(u));
			logger.info("最终积分为:"+points);
		
			//只保存积分值大于0的积分明细
			if(points.intValue()>0) {
				pointsDetail.setPoints(points.intValue());
				List<CustomerCategoryPointsDTO>itemList=this.combineOrderItemByCategory(customer,pointsDetail,orderItemList);
				pointsDetailListMap.put(pointsDetail,itemList);
			}
		});
		return pointsDetailListMap;

	}
	private List<CustomerCategoryPointsDTO>combineOrderItemByCategory(Customer customer,PointsDetailDTO pointsDetail,List<OrderItem>orderItemList){
		if(customer==null){
			return Collections.emptyList();
		}
		List<CustomerCategoryPointsDTO>list= orderItemList
		.stream()
		//过滤空对象及Id为空的对象
		.filter(item->{return (item!=null&&item.getCategoryId()!=null);})
		.map(item->{
			//对重量和金额进行初始化防止为空
			if(item.getTotalMoney()==null||item.getTotalMoney()<0) {
				item.setTotalMoney(0L);
			}
			if(item.getWeight()==null||item.getWeight().compareTo(BigDecimal.ZERO)<0) {
				item.setWeight(BigDecimal.ZERO);
			}
			return item;
		})
		//按照Id进行分组
		.collect(Collectors.groupingBy(OrderItem::getCategoryId))
		.values()
		.stream()
		//对分组后的集合进行数据合并计算
		.map((itemList)->{
			return itemList.stream()
				.reduce((t,u)->{
					//进行金额和重量累加
					t.setTotalMoney(t.getTotalMoney()+u.getTotalMoney());
					t.setWeight(t.getWeight().add(u.getWeight()));
					return t;
				});
			})
		.map(Optional::get)
		.filter(item->item!=null)
		//对象类型转换
		.map(item->{
			Long categoryId=item.getCategoryId();
			
			//进行数据对象的转换
			CustomerCategoryPointsDTO dto=DTOUtils.newDTO(CustomerCategoryPointsDTO.class);
			dto.setCertificateNumber(customer.getCertificateNumber());
			dto.setCertificateType(customer.getCertificateType());
			dto.setCustomerId(customer.getId());
			dto.setName(customer.getName());
			dto.setOrder(item.getOrder());
			dto.setOrganizationType(customer.getOrganizationType());
			dto.setSourceSystem(pointsDetail.getSourceSystem());
			dto.setOrderCode(item.getOrderCode());
			dto.setTotalMoney(item.getTotalMoney());
			dto.setWeight(item.getWeight());
			dto.setCategory3Id(categoryId);
			dto.setCategory3Name("未知");
			dto.setBuyerPoints(0);
			dto.setSellerPoints(0);
			dto.setAvailable(0);
			Category category3Condition=DTOUtils.newDTO(Category.class);
			category3Condition.setCategoryId(String.valueOf(categoryId));
			category3Condition.setSourceSystem(pointsDetail.getSourceSystem());
			//查询并设置第三级品类ID和名称
			this.categoryService.list(category3Condition).stream().findFirst().ifPresent(c3->{
				dto.setCategory3Id(categoryId);
				dto.setCategory3Name(c3.getName());
				if(StringUtils.trimToNull(c3.getParentCategoryId())!=null) {
					
					Category category2Condition=DTOUtils.newDTO(Category.class);
					category2Condition.setCategoryId(String.valueOf(c3.getParentCategoryId()));
					category2Condition.setSourceSystem(pointsDetail.getSourceSystem());
					//查询并设置第二级品类ID和名称
					this.categoryService.list(category2Condition).stream().findFirst().ifPresent(c2->{
						dto.setCategory2Id(Long.valueOf(c2.getCategoryId()));
						dto.setCategory2Name(c2.getName());
						if(StringUtils.trimToNull(c2.getParentCategoryId())!=null) {
							
							
							Category category1Condition=DTOUtils.newDTO(Category.class);
							category1Condition.setCategoryId(String.valueOf(c2.getParentCategoryId()));
							category1Condition.setSourceSystem(pointsDetail.getSourceSystem());
							//查询并设置第一级品类ID和名称
							this.categoryService.list(category1Condition).stream().findFirst().ifPresent(c->{
								dto.setCategory1Id(Long.valueOf(c.getCategoryId()));
								dto.setCategory1Name(c.getName());
							});
						}
					});
				}
			});
			return dto;
		
		})
		.collect(Collectors.toList());
		
		return list;
	}
	/**
	 * 根据标准值和条件表表,选出条件的第一个权重值
	 * 
	 * @param conditionNumber
	 * @param ruleConditionList
	 * @return
	 */
	protected Entry<Boolean,BigDecimal> calculateWeight(BigDecimal conditionNumber, List<RuleCondition> ruleConditionList) {
		//ruleConditionList.stream().collect(Collectors.groupingBy(r->{return r.getConditionType()}));
		logger.info("conditionNumber="+conditionNumber);
	
		for (RuleCondition ruleCondition : ruleConditionList) {

			String conditionWeight = ruleCondition.getWeight();// 权重
			String startValue = ruleCondition.getStartValue();// 开始值
			String endValue = ruleCondition.getEndValue();// 结束值
			String value = ruleCondition.getValue();// 条件值

			Integer conditonType = ruleCondition.getConditionType();// 区间: 60, 大于等于 20 大于 30, 小于等于 40, 小于 50, 等于 10
			// 是否进行权重*积分的计算过程
			boolean hitCondition = false;
			logger.info("startValue="+startValue+",value="+value+",endValue="+endValue+",conditonType="+conditonType+",ruleCondition.getId()="+ruleCondition.getId());
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
				logger.info("命中积分规则:"+ruleCondition.getId());
				return new AbstractMap.SimpleEntry<>(hitCondition, new BigDecimal(conditionWeight));
			}
		}
		return new AbstractMap.SimpleEntry<>(false, BigDecimal.ONE);
	}

}
