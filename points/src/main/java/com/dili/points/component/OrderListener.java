package com.dili.points.component;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dili.ss.util.AESUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dili.points.converter.DtoMessageConverter;
import com.dili.points.domain.Category;
import com.dili.points.domain.Customer;
import com.dili.points.domain.Order;
import com.dili.points.domain.OrderItem;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.PointsException;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.RuleCondition;
import com.dili.points.domain.dto.CustomerCategoryPointsDTO;
import com.dili.points.domain.dto.CustomerFirmPointsDTO;
import com.dili.points.domain.dto.CustomerPointsDTO;
import com.dili.points.domain.dto.OrderPointsDataInfo;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.points.provider.DataDictionaryValueProvider;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.service.CategoryService;
import com.dili.points.service.CustomerCategoryPointsService;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.points.service.CustomerPointsService;
import com.dili.points.service.OrderItemService;
import com.dili.points.service.OrderService;
import com.dili.points.service.PointsDetailService;
import com.dili.points.service.PointsExceptionService;
import com.dili.points.service.PointsRuleService;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;

/**
 * ?????????????????? Created by asiamaster on 2017/11/7 0007.
 *
 * @author wangguofeng
 */
//@Component
//@ConditionalOnExpression("'${mq.enable}'=='true'")
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
    @Autowired
    CustomerFirmPointsService customerFirmPointsService;
    @Autowired
    CustomerCategoryPointsService customerCategoryPointsService;
    @Autowired
    PointsExceptionService pointsExceptionService;
    @Autowired
    CustomerPointsService customerPointsService;
    @Value("${aesKey:}")
    private String aesKey;

    @RabbitListener(queues = "#{rabbitConfiguration.ORDER_TOPIC_QUEUE}")
    public void processBootTask(Message message) throws UnsupportedEncodingException {

        logger.info("????????????: " + message);
        String data = new String(message.getBody(), "UTF-8");
        String orderJson = AESUtils.decrypt(data, aesKey);
        try {
            Map<Order, List<OrderItem>> orderMap = this.convertOrder(orderJson);
            if (orderMap.isEmpty()) {
                logger.error("???????????????????????????????????????: " + orderJson);
                return;
            }
            if (!this.checkData(orderMap)) {
                logger.error("???????????????????????????????????????: " + orderJson);
                List<PointsException> list = this.pointsExceptions(orderMap, orderJson);
                this.pointsExceptionService.batchInsert(list);
                return;
            }
            this.calAndSaveData(orderMap);
        } catch (Exception e) {
            logger.error("??????????????????" + orderJson + ",??????????????????", e);
            PointsException detailDTO = DTOUtils.newDTO(PointsException.class);
            detailDTO.setNotes("????????????:" + e.getMessage() + ",??????:" + orderJson);
            detailDTO.setNeedRecover(0);
            detailDTO.setPoints(0);
            try {
                this.pointsExceptionService.insert(detailDTO);
            } catch (Exception e2) {
                logger.error("????????????????????????:" + e2.getMessage(), e2);
            }
        }
    }

    protected List<PointsException> pointsExceptions(Map<Order, List<OrderItem>> orderMap, String orderJson) {
        List<PointsException> list = new ArrayList<>();
        orderMap.forEach((order, item) -> {
            PointsException exceptionObj = DTOUtils.newDTO(PointsException.class);

            if (this.isSettlementOrder(order)) {
                exceptionObj.setOrderCode(order.getSettlementCode());
            } else {
                exceptionObj.setOrderCode(order.getCode());
            }
            exceptionObj.setSourceSystem(order.getSourceSystem());
            exceptionObj.setNotes("??????????????????:" + orderJson);
            exceptionObj.setPoints(0);
            exceptionObj.setNeedRecover(0);
            list.add(exceptionObj);
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
     * ???Json?????????dto????????????
     *
     * @param json ??????json?????????
     * @return ????????????Map
     */
    @SuppressWarnings("unchecked")
    protected Map<Order, List<OrderItem>> convertOrder(String json) {
        Map<Order, List<OrderItem>> resultMap = new HashMap<>();
        Map<String, Object> jsonMap = DtoMessageConverter.convertAsMap(json);
        String type = StringUtils.trimToNull(String.valueOf(jsonMap.get("type")));
        if (type == null || "json".equalsIgnoreCase(type)) {
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

    protected void savePointsData(Map<Order, List<OrderItem>> orderMap, String customerType) {

        Map<Boolean, List<OrderPointsDataInfo>> map = this.calculatePoints(orderMap, customerType)
                .stream()
                .filter(data -> {
                    return data.getPoints().intValue() > 0;
                })
                .collect(Collectors.partitioningBy(data -> {
                    return (data.getCustomer() == null);
                }));


        List<OrderPointsDataInfo> pointsDataInfoList = map.get(Boolean.FALSE);

        List<OrderPointsDataInfo> withoutCustomerExceptionList = map.get(Boolean.TRUE);


        List<OrderPointsDataInfo> list = this.saveCustomerFirmPoints(pointsDataInfoList);

        Map<Boolean, List<OrderPointsDataInfo>> afterSaveMap = list.stream()
                .collect(Collectors.partitioningBy((OrderPointsDataInfo data) -> {
                    return (data.getActualPoints().equals(0));
                }));

        List<OrderPointsDataInfo> zeroPointsExceptionList = afterSaveMap.get(Boolean.TRUE);

        this.savePointsException(withoutCustomerExceptionList, zeroPointsExceptionList);


        List<OrderPointsDataInfo> orderPointsDataInfoList = afterSaveMap.get(Boolean.FALSE);

        this.saveCustomPoints(orderPointsDataInfoList);


        this.savePointsDetail(orderPointsDataInfoList);


        this.saveCustomerCategoryPoints(orderPointsDataInfoList);


    }

    /**
     * ??????CustomerFirmPoints??????
     *
     * @param pointsDataInfoList
     * @return ????????????actualPoints???OrderPointsDataInfo
     */
    private List<OrderPointsDataInfo> saveCustomerFirmPoints(
            List<OrderPointsDataInfo> pointsDataInfoList) {
        logger.debug("????????????CustomerFirmPoints??????");
        return pointsDataInfoList.stream().map(data -> {
            CustomerFirmPointsDTO customerFirmPoints = DTOUtils.newDTO(CustomerFirmPointsDTO.class);
            customerFirmPoints.setBuyer(data.isBuyer());
            customerFirmPoints.setPoints(data.getPoints().intValue());
            customerFirmPoints.setBuyerPoints(0);
            customerFirmPoints.setSellerPoints(0);
            customerFirmPoints.setCertificateNumber(data.getCertificateNumber());
            customerFirmPoints.setCustomerId(data.getCustomer().getId());
            customerFirmPoints.setTradingFirmCode(data.getFirmCode());
            int value = customerFirmPointsService.saveCustomerFirmPoints(customerFirmPoints);
            if (value > 0) {
                //????????????????????????????????????????????????customerFirmPoints.actualPoints ????????????
                data.setActualPoints(customerFirmPoints.getActualPoints());
                data.setAvailable(customerFirmPoints.getAvailable());
            }

//			Entry<OrderPointsDataInfo,CustomerFirmPoints>entry=new AbstractMap.SimpleEntry<>(data, customerFirmPoints);
            return data;
        }).collect(Collectors.toList());
    }

    /**
     * ??????CustomerCategoryPointsDTO??????
     *
     * @param orderPointsDataInfoList
     */
    private void saveCustomerCategoryPoints(List<OrderPointsDataInfo> orderPointsDataInfoList) {
        logger.debug("????????????CustomerCategoryPointsDTO??????");
        List<CustomerCategoryPointsDTO> customerCategoryList = orderPointsDataInfoList.stream().flatMap(data -> {
            List<CustomerCategoryPointsDTO> combineList = this.combineOrderItemByCategory(data);
            List<CustomerCategoryPointsDTO> resultList = this.reCalculateCategoryPoints(data, combineList);
            return resultList.stream();

        }).collect(Collectors.toList());
        this.customerCategoryPointsService.batchSaveCustomerCategoryPointsDTO(customerCategoryList);
    }

    /**
     * ??????PointsDetailDTO??????
     *
     * @param orderPointsDataInfoList
     */
    private void savePointsDetail(List<OrderPointsDataInfo> orderPointsDataInfoList) {
        logger.debug("????????????PointsDetailDTO??????");
        List<PointsDetail> pointsDetailList = orderPointsDataInfoList.stream().map(data -> {
            PointsDetailDTO pointsDetail = DTOUtils.newDTO(PointsDetailDTO.class);

            pointsDetail.setCertificateNumber(data.getCertificateNumber());
            pointsDetail.setCustomerId(data.getCustomer().getId());
            pointsDetail.setCustomerType(data.getCustomerType());
            pointsDetail.setFirmCode(data.getFirmCode());
            pointsDetail.setGenerateWay(data.getGenerateWay());
            pointsDetail.setInOut(data.getInOut());
            pointsDetail.setOrderCode(data.getOrderCode());
            pointsDetail.setOrderType(data.getOrderType());

            pointsDetail.setActualPoints(data.getActualPoints());
            pointsDetail.setPoints(data.getActualPoints());
            pointsDetail.setBalance(data.getAvailable());

            pointsDetail.setSourceSystem(data.getSourceSystem());
            pointsDetail.setWeightType(data.getWeightType());
            pointsDetail.setCreatedId(0L);
            pointsDetail.setNotes(data.getNotes());
            return pointsDetail;
        })
                .collect(Collectors.toList());
        if (!pointsDetailList.isEmpty()) {
            this.pointsDetailService.batchInsert(pointsDetailList);
        }
    }

    /**
     * ??????CustomerPointsDTO??????
     *
     * @param orderPointsDataInfoList
     */
    private void saveCustomPoints(List<OrderPointsDataInfo> orderPointsDataInfoList) {
        logger.debug("????????????CustomerPointsDTO??????");
        List<CustomerPointsDTO> customerPointsDTOList = orderPointsDataInfoList.stream().map(data -> {
            CustomerPointsDTO pointsDetail = DTOUtils.newDTO(CustomerPointsDTO.class);


            pointsDetail.setCustomerId(data.getCustomer().getId());
            pointsDetail.setCertificateNumber(data.getCertificateNumber());
            pointsDetail.setBuyerPoints(0);
            pointsDetail.setSellerPoints(0);
            pointsDetail.setResetTime(new Date());
            pointsDetail.setCreated(new Date());
            pointsDetail.setModifiedId(0L);
            pointsDetail.setYn(1);
            pointsDetail.setName(data.getCustomer().getName());
            pointsDetail.setOrganizationType(data.getCustomer().getOrganizationType());

//			pointsDetail.setProfession(data.getCustomer().getProfession());
            pointsDetail.setType(data.getCustomer().getType());

            pointsDetail.setCertificateType(data.getCustomer().getCertificateType());
            pointsDetail.setPhone(data.getCustomer().getPhone());

            pointsDetail.setBuyer(data.isBuyer());
            pointsDetail.setActualPoints(data.getActualPoints());
            return pointsDetail;
        })
                .collect(Collectors.toList());

        this.customerPointsService.batchSaveCustomerPointsDTO(customerPointsDTOList);
    }

    /**
     * ??????PointsException??????
     *
     * @param withoutCustomerExceptionList
     * @param zeroPointsExceptionList
     */
    private void savePointsException(List<OrderPointsDataInfo> withoutCustomerExceptionList,
                                     List<OrderPointsDataInfo> zeroPointsExceptionList) {
        logger.debug("????????????PointsException??????");
        List<PointsException> exceptionList = Stream.concat(withoutCustomerExceptionList.stream(), zeroPointsExceptionList.stream())
                .map(data -> {
                    PointsDetailDTO pointsDetail = DTOUtils.newDTO(PointsDetailDTO.class);
                    if (data.getCustomer() == null) {
                        pointsDetail.setNotes("?????????????????????[" + data.getCertificateNumber() + "]?????????????????????");
                        pointsDetail.setException(1);
                        pointsDetail.setNeedRecover(1);// ??????????????????????????????,??????????????????????????????
                        pointsDetail.setCustomerId(null);
                    } else {
                        pointsDetail.setNeedRecover(0);
                        pointsDetail.setCustomerId(data.getCustomer().getId());
                    }

                    pointsDetail.setCertificateNumber(data.getCertificateNumber());
                    pointsDetail.setCustomerType(data.getCustomerType());
                    pointsDetail.setFirmCode(data.getFirmCode());
                    pointsDetail.setGenerateWay(data.getGenerateWay());
                    pointsDetail.setInOut(data.getInOut());
                    pointsDetail.setOrderCode(data.getOrderCode());
                    pointsDetail.setOrderType(data.getOrderType());
                    pointsDetail.setPoints(data.getPoints().intValue());
                    pointsDetail.setSourceSystem(data.getSourceSystem());
                    pointsDetail.setWeightType(data.getWeightType());

                    PointsException exceptionalPoints = DTOUtils.as(pointsDetail, PointsException.class);
                    if (exceptionalPoints.getNeedRecover() == null) {
                        exceptionalPoints.setNeedRecover(0);
                    }

                    return exceptionalPoints;
                })
                .collect(Collectors.toList());
        if (exceptionList.size() > 0) {
            this.pointsExceptionService.batchInsert(exceptionList);
        }

    }

    private List<CustomerCategoryPointsDTO> reCalculateCategoryPoints(OrderPointsDataInfo data, List<CustomerCategoryPointsDTO> categoryList) {
        if (data.getWeightType() == null) {
            logger.info("????????????????????????????????????????????? CertificateNumber {},CustomerType {}", data.getCertificateNumber(), data.getCustomerType());
            return Collections.emptyList();
        }
        Integer totalPoints = data.getActualPoints();
        if (totalPoints == null || totalPoints < 0) {
            totalPoints = 0;
        }

        //????????????????????????????????????
        CustomerCategoryPointsDTO total = DTOUtils.newDTO(CustomerCategoryPointsDTO.class);
        total.setTotalMoney(0L);
        total.setWeight(BigDecimal.ZERO);
        total.setBuyerPoints(0);
        total.setSellerPoints(0);
        total.setActualPoints(0);
        for (CustomerCategoryPointsDTO dto : categoryList) {
            total.setTotalMoney(total.getTotalMoney() + dto.getTotalMoney());
            total.setWeight(total.getWeight().add(dto.getWeight()));
        }
        logger.info("?????????????????????????????????: CertificateNumber {},CustomerType {},totalPoints: {}", data.getCertificateNumber(), data.getCustomerType(), totalPoints);
        List<CustomerCategoryPointsDTO> resultList = new ArrayList<>();
        //??????????????????????????????
        for (CustomerCategoryPointsDTO dto : categoryList) {
            //// ????????? 10 ????????? 20 ?????? 30 ????????????:40


            logger.info("CertificateNumber {},Weight{},Money:{},TotalWeight {},TotalMoney {},Category1Id {},Category1Name {}"
                    , data.getCertificateNumber(), dto.getWeight().toPlainString(), dto.getTotalMoney(), total.getWeight().toPlainString()
                    , total.getTotalMoney(), dto.getCategory3Id(), dto.getCategory3Name());
            BigDecimal pointsDecimal = BigDecimal.ZERO;
            if (data.getWeightType().equals(10)) {
                pointsDecimal = new BigDecimal(totalPoints).multiply(dto.getWeight()).divide(total.getWeight(), 0, RoundingMode.DOWN);
            } else if (data.getWeightType().equals(20)) {
                pointsDecimal = new BigDecimal(totalPoints).multiply(new BigDecimal(dto.getTotalMoney())).divide(new BigDecimal(total.getTotalMoney()), 0, RoundingMode.DOWN);
            } else {
                continue;
            }
            BigDecimal percentage = pointsDecimal.divide(new BigDecimal(totalPoints), 10, RoundingMode.HALF_EVEN);
            int points = pointsDecimal.intValue();
            logger.info("CertificateNumber {},CustomerType {},Category1Id {},Category1Name {},TotalPoints,{},Percentage:{},Points: {}"
                    , data.getCertificateNumber(), data.getCustomerType(), dto.getCategory3Id(), dto.getCategory3Name(), totalPoints, percentage.toPlainString(), points);
            //10:??????,20:??????
            total.setActualPoints(total.getActualPoints() + points);
            dto.setActualPoints(points);
            logger.info("?????????????????????????????????????????????:CertificateNumber {},CustomerType {},BuyerPoints {},SellerPoints {},Available: {}", data.getCertificateNumber(), data.getCustomerType(), dto.getBuyerPoints(), dto.getSellerPoints(), dto.getAvailable());
            resultList.add(dto);
        }

        //?????????????????????(????????????????????????????????????????????????????????????)
        if (!resultList.isEmpty()) {
            CustomerCategoryPointsDTO lastCategoryPointsDTO = categoryList.get(categoryList.size() - 1);
            lastCategoryPointsDTO.setActualPoints(totalPoints - (total.getActualPoints() - lastCategoryPointsDTO.getActualPoints()));

//			lastCategoryPointsDTO.setAvailable(lastCategoryPointsDTO.getPoints());
            logger.info("?????????????????????:CertificateNumber {},CustomerType {},BuyerPoints{},SellerPoints{},Available: {}", data.getCertificateNumber(), data.getCustomerType(), lastCategoryPointsDTO.getBuyerPoints(), lastCategoryPointsDTO.getSellerPoints(), lastCategoryPointsDTO.getAvailable());
        }
//		
        return resultList;
    }

    /**
     * ????????????????????????????????????
     *
     * @param orderMap
     */
    protected void calAndSaveData(Map<Order, List<OrderItem>> orderMap) {
        // ???????????????????????????(????????????)??????????????????
        Map<Order, List<OrderItem>> purchaseOrdersMap = this.sumOrdersForPurchase(orderMap);
        //??????????????????
        Map<Order, List<OrderItem>> saleOrdersMap = orderMap;

        //??????????????????
        this.saveAllData(orderMap, purchaseOrdersMap, saleOrdersMap);
    }

    @Transactional(timeout = 90, propagation = Propagation.REQUIRED)
    private void saveAllData(Map<Order, List<OrderItem>> orderMap, Map<Order, List<OrderItem>> purchaseOrdersMap, Map<Order, List<OrderItem>> saleOrdersMap) {
        //??????????????????
        this.saveOrderAndItem(orderMap);
        // ???????????????????????????
        this.savePointsData(purchaseOrdersMap, "purchase");
        // ???????????????????????????
        this.savePointsData(saleOrdersMap, "sale");

    }

    /**
     * ????????????????????????
     *
     * @param orderMap
     */

    private Map<Order, List<OrderItem>> saveOrderAndItem(Map<Order, List<OrderItem>> orderMap) {


        orderMap.forEach((order, orderItemList) -> {
            if (this.orderIsExists(order)) {
                throw new AppException("????????????????????????:???????????????????????????????????????");
            }
            // ??????????????????
            this.orderService.insertSelective(order);

            // ??????????????????
            for (OrderItem item : orderItemList) {
                item.setOrderCode(order.getCode());
            }
            this.orderItemService.batchInsert(orderItemList);
        });
        return orderMap;
    }


    /**
     * ?????????????????????id
     *
     * @param certificateNumber ?????????
     * @return
     */
    protected Customer findIdByCertificateNumber(String certificateNumber) {
        try {
            BaseOutput<Customer> baseOut = this.customerRpc.getByCertificateNumber(certificateNumber);
            if (baseOut.isSuccess()) {
                String json = baseOut.getData().toString();
                Customer customer = DTOUtils.newDTO(Customer.class);
                try {
                    BeanUtils.copyProperties(customer, JSONObject.parse(json));
                    return customer;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error(e.getMessage(), e);
                }

            } /*
             * else { throw new AppException("???????????????:"+certificateNumber+",???????????????????????????"); }
             */
        } catch (Exception e) {
            // throw new AppException("????????????????????????:"+certificateNumber,e);
        }
        logger.warn("???????????????????????????.?????????:" + certificateNumber);
        return null;

    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param customerType (??????sale,??????purchase)
     * @return ????????????
     */
    protected Optional<PointsRule> findPointsRule(String customerType, Integer businessType, String market) {
        PointsRule pointsRuleEx = DTOUtils.newDTO(PointsRule.class);
        pointsRuleEx.setYn(1);
        pointsRuleEx.setCustomerType(customerType);
        pointsRuleEx.setBusinessType(businessType);// 10??????,20??????,30??????
        pointsRuleEx.setFirmCode(market);
        return this.pointsRuleService.listByExample(pointsRuleEx).stream().findFirst();
    }

    /**
     * ??????????????????
     *
     * @param pointsRuleId ??????id
     * @param weightType   ????????????(????????? 10 ????????? 20 ?????? 30 ????????????:40)
     * @return ?????????????????????
     */
    protected List<RuleCondition> findRuleCondition(Long pointsRuleId, Integer weightType) {
        RuleCondition condition = DTOUtils.newDTO(RuleCondition.class);
        condition.setWeightType(weightType);// ????????? 10 ????????? 20 ?????? 30 ????????????:40
        condition.setPointRuleId(pointsRuleId);
        condition.setSort("modified");
        condition.setOrder("DESC");

        List<RuleCondition> list = ruleConditionService.listByExample(condition);
        return list;
    }

    /**
     * ????????????????????????,?????? ?????????????????????
     *
     * @param orderMap ??????????????????
     * @return ????????????
     */
    protected Map<Order, List<OrderItem>> sumOrdersForPurchase(Map<Order, List<OrderItem>> orderMap) {
        Collection<Order> orderList = orderMap.keySet();
        // ????????????????????????????????????
        if (orderList.size() == 1) {
            return orderMap;
        }

        Order order = DTOUtils.newDTO(Order.class);
        order.setTotalMoney(0L);
        order.setWeight(BigDecimal.ZERO);

        // ??????????????????????????????(??????,??????)
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
     * ????????????????????????????????????????????????
     *
     * @param pointsRule ????????????
     * @param order      ????????????
     * @return ????????????????????????
     */
    protected BigDecimal calculateBasePoints(PointsRule pointsRule, OrderPointsDataInfo order) {
        BigDecimal orderWeight = order.getWeight();// ?????????

        BigDecimal totalMoney = new BigDecimal(order.getTotalMoney()).divide(new BigDecimal("100"));// ?????????(??????100,??????????????????)

        Integer computingStandard = pointsRule.getComputingStandard();
        String computingParameter = pointsRule.getComputingParameter();
        // ??????????????????,?????????????????????
        BigDecimal basePoint = BigDecimal.ZERO;

        // 10 ?????????,20 ?????????,30 ?????????
        // ????????????
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

    private Optional<Category> findCategory(Long categoryId, String sourceSystem) {
        if (categoryId == null || StringUtils.trimToNull(sourceSystem) == null) {
            return Optional.empty();
        }
        Category example = DTOUtils.newDTO(Category.class);
        example.setCategoryId(String.valueOf(categoryId));
        example.setSourceSystem(sourceSystem);
        return this.categoryService.listByExample(example).stream().findFirst();
    }

    /**
     * ??????????????????????????????,??????????????????,??????????????????
     *
     * @param order         ????????????
     * @param orderItemList ?????????????????????
     * @return ??????????????????
     */
    protected String findNotes(Order order, List<OrderItem> orderItemList) {

        StringBuffer notesStr = new StringBuffer();

        List<String> categoryNameList = new ArrayList<>();
        // ????????????,??????????????????
        for (OrderItem orderItem : orderItemList) {
            // ????????????????????????
            if (categoryNameList.size() >= 4) {
                break;
            }

            // ????????????
            Optional<Category> opt = this.findCategory(orderItem.getCategoryId(), order.getSourceSystem());
            if (opt.isPresent()) {
                categoryNameList.add(opt.get().getName());
            }

        }
        if (!categoryNameList.isEmpty()) {
            notesStr.append("??????");
            if (categoryNameList.size() <= 3) {
                // ?????????????????????,????????????
                notesStr.append(String.join(",", categoryNameList));
            } else {
                // ????????????,???????????????,???????????????????????????
                categoryNameList = categoryNameList.subList(0, categoryNameList.size() - 1);
                notesStr.append(String.join(",", categoryNameList)).append("...");
            }
            // ?????????????????????????????????
            notesStr.append("; ");
        }

        if (this.isSettlementOrder(order)) {
            notesStr.append("????????????:").append(order.getSettlementCode());
        } else {
            notesStr.append("?????????:").append(order.getCode());
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

    // ??????sale,??????purchase
    private List<OrderPointsDataInfo> calculatePoints(Map<Order, List<OrderItem>> orderMap, String customerType) {
        boolean isBuyer = this.isBuyer(customerType);

        List<OrderPointsDataInfo> orderList = new ArrayList<>();
        orderMap.forEach((order, orderItemList) -> {
            OrderPointsDataInfo orderPointsDataInfo = new OrderPointsDataInfo();
            String notes = this.findNotes(order, orderItemList);
            orderPointsDataInfo.setNotes(notes);
            orderPointsDataInfo.setSourceSystem(order.getSourceSystem());
            orderPointsDataInfo.setCustomerType(customerType);
            PointsRule pointsRule = this.findPointsRule(customerType, order.getBusinessType(), order.getMarket()).orElse(null);
            if (isBuyer) {
                logger.info("?????????[" + order.getBuyerCertificateNumber() + "]??????????????????");
                orderPointsDataInfo.setCertificateNumber(order.getBuyerCertificateNumber());
            } else {
                logger.info("?????????[" + order.getSellerCertificateNumber() + "]????????????");
                orderPointsDataInfo.setCertificateNumber(order.getSellerCertificateNumber());
            }
            orderPointsDataInfo.setBuyer(isBuyer);
            if (pointsRule == null) {
                logger.info("???????????????????????? customerType {},businessType {}", customerType, order.getBusinessType());
                return;
            }


            Integer computingStandard = pointsRule.getComputingStandard();
            // 10 ?????????,20 ?????????,30 ?????????
            if (computingStandard == 10) {
                logger.info("????????????????????????????????? {}", orderPointsDataInfo.getCertificateNumber());
                orderPointsDataInfo.setWeightType(10);
            } else if (computingStandard == 20) {
                logger.info("????????????????????????????????? {}", orderPointsDataInfo.getCertificateNumber());
                orderPointsDataInfo.setWeightType(20);
            }

            if (this.isSettlementOrder(order)) {
                orderPointsDataInfo.setOrderCode(order.getSettlementCode());
                orderPointsDataInfo.setOrderType("settlementOrder");// settlementOrder????????????,order??????
            } else {
                orderPointsDataInfo.setOrderCode(order.getCode());
                orderPointsDataInfo.setOrderType("order");// settlementOrder????????????,order??????
            }


            String certificateNumber = orderPointsDataInfo.getCertificateNumber();
            // ??????????????????????????????????????????,??????????????????
            if (StringUtils.isNotBlank(certificateNumber)) {
                Customer customer = this.findIdByCertificateNumber(certificateNumber);
                if (customer != null && customer.getId() != null) {
                    orderPointsDataInfo.setCustomer(customer);
                } else {
                    orderPointsDataInfo.setCustomer(null);
                }
            }
            orderPointsDataInfo.setInOut(10);//??????
            orderPointsDataInfo.setGenerateWay(10);//????????????
            orderPointsDataInfo.setPointsRule(pointsRule);
            orderPointsDataInfo.setOrderItems(orderItemList);
            orderPointsDataInfo.setFirmCode(order.getMarket());
            orderPointsDataInfo.setTotalMoney(order.getTotalMoney());
            orderPointsDataInfo.setWeight(order.getWeight());
            orderPointsDataInfo.setPayment(order.getPayment());
            orderPointsDataInfo.setAvailable(0);
            orderList.add(orderPointsDataInfo);
        });

        return orderList.stream().filter(order -> {
            String certificateNumber = order.getCertificateNumber();
            // ??????????????????????????????????????????,??????????????????
            return StringUtils.isNotBlank(certificateNumber);
        }).map(order -> {

            PointsRule pointsRule = order.getPointsRule();
            logger.info("?????????????????????????????? code: " + pointsRule.getCode());
            // ?????? ?????????,????????? ,???????????? ????????????????????????

            // ????????? 10 ????????? 20 ?????? 30 ????????????:40
            List<RuleCondition> tradeWeightConditionList = this.findRuleCondition(pointsRule.getId(), 10);
            List<RuleCondition> tradeTotalMoneyConditionList = this.findRuleCondition(pointsRule.getId(), 20);
            List<RuleCondition> tradeTypeConditionList = this.findRuleCondition(pointsRule.getId(), 40);

            BigDecimal basePoint = this.calculateBasePoints(pointsRule, order);
            logger.info("??????????????????:" + basePoint.toPlainString());

//				order.setPoints(basePoint);

            BigDecimal orderWeightValue = order.getWeight();// ?????????
            BigDecimal totalMoneyValue = new BigDecimal(order.getTotalMoney()).divide(new BigDecimal("100"));// ?????????
            BigDecimal paymentValue = new BigDecimal(order.getPayment());// ????????????

            // ???????????????????????????????????????????????????
            Entry<Boolean, BigDecimal> tradeWeightEntry = this.calculateWeight(orderWeightValue, tradeWeightConditionList);
            Entry<Boolean, BigDecimal> totalMoneyEntry = this.calculateWeight(totalMoneyValue, tradeTotalMoneyConditionList);
            Entry<Boolean, BigDecimal> paymentEntry = this.calculateWeight(paymentValue, tradeTypeConditionList);

            List<BigDecimal> weightList = Arrays.asList(tradeWeightEntry.getValue(),
                    totalMoneyEntry.getValue(),
                    paymentEntry.getValue());
            logger.info("???????????????????????????:" + weightList);
            // ???????????????
            BigDecimal points = weightList.stream().reduce(basePoint, (t, u) -> t.multiply(u));
            logger.info("???????????????:" + points);
            order.setPoints(points);
            return order;
        }).collect(Collectors.toList());

    }

    /**
     * ??????????????????orderitem?????????????????????
     *
     * @param orderPointsDataInfo
     * @return
     */
    private List<CustomerCategoryPointsDTO> combineOrderItemByCategory(OrderPointsDataInfo orderPointsDataInfo) {
        logger.info("???{}????????????orderitem,?????????Category??????", orderPointsDataInfo.getCertificateNumber());
        Customer customer = orderPointsDataInfo.getCustomer();
        if (customer == null) {
            return Collections.emptyList();
        }
        List<CustomerCategoryPointsDTO> list = orderPointsDataInfo.getOrderItems()
                .stream()
                //??????????????????Id???????????????
                .filter(item -> {
                    return (item != null && item.getCategoryId() != null);
                })
                .map(item -> {
                    //?????????????????????????????????????????????
                    if (item.getTotalMoney() == null || item.getTotalMoney() < 0) {
                        item.setTotalMoney(0L);
                    }
                    if (item.getWeight() == null || item.getWeight().compareTo(BigDecimal.ZERO) < 0) {
                        item.setWeight(BigDecimal.ZERO);
                    }
                    return item;
                })
                //??????Id????????????
                .collect(Collectors.groupingBy(OrderItem::getCategoryId))
                .values()
                .stream()
                //?????????????????????????????????????????????
                .map((itemList) -> {
                    return itemList.stream()
                            .reduce((t, u) -> {
                                //???????????????????????????
                                t.setTotalMoney(t.getTotalMoney() + u.getTotalMoney());
                                t.setWeight(t.getWeight().add(u.getWeight()));
                                return t;
                            });
                })
                .map(Optional::get)
                .filter(item -> item != null)
                //??????????????????
                .map(item -> {
                    Long categoryId = item.getCategoryId();

                    //???????????????????????????
                    CustomerCategoryPointsDTO dto = DTOUtils.newDTO(CustomerCategoryPointsDTO.class);
                    dto.setCertificateNumber(customer.getCertificateNumber());
                    dto.setCertificateType(customer.getCertificateType());
                    dto.setCustomerId(customer.getId());
                    dto.setName(customer.getName());
                    dto.setOrder(item.getOrder());
                    dto.setOrganizationType(customer.getOrganizationType());
                    dto.setSourceSystem(orderPointsDataInfo.getSourceSystem());
                    dto.setFirmCode(orderPointsDataInfo.getFirmCode());
                    dto.setOrderCode(item.getOrderCode());
                    dto.setTotalMoney(item.getTotalMoney());
                    dto.setWeight(item.getWeight());
                    dto.setCategory3Id(categoryId);
                    dto.setCategory3Name("??????");
                    dto.setBuyer(orderPointsDataInfo.isBuyer());
                    dto.setBuyerPoints(0);
                    dto.setSellerPoints(0);
                    dto.setAvailable(0);
                    Category category3Condition = DTOUtils.newDTO(Category.class);
                    category3Condition.setCategoryId(String.valueOf(categoryId));
                    category3Condition.setSourceSystem(orderPointsDataInfo.getSourceSystem());
                    //??????????????????????????????ID?????????
                    this.categoryService.list(category3Condition).stream().findFirst().ifPresent(c3 -> {
                        dto.setCategory3Id(categoryId);
                        dto.setCategory3Name(c3.getName());
                        if (StringUtils.trimToNull(c3.getParentCategoryId()) != null) {

                            Category category2Condition = DTOUtils.newDTO(Category.class);
                            category2Condition.setCategoryId(String.valueOf(c3.getParentCategoryId()));
                            category2Condition.setSourceSystem(orderPointsDataInfo.getSourceSystem());
                            //??????????????????????????????ID?????????
                            this.categoryService.list(category2Condition).stream().findFirst().ifPresent(c2 -> {
                                dto.setCategory2Id(Long.valueOf(c2.getCategoryId()));
                                dto.setCategory2Name(c2.getName());
                                if (StringUtils.trimToNull(c2.getParentCategoryId()) != null) {


                                    Category category1Condition = DTOUtils.newDTO(Category.class);
                                    category1Condition.setCategoryId(String.valueOf(c2.getParentCategoryId()));
                                    category1Condition.setSourceSystem(orderPointsDataInfo.getSourceSystem());
                                    //??????????????????????????????ID?????????
                                    this.categoryService.list(category1Condition).stream().findFirst().ifPresent(c -> {
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
     * ??????????????????????????????,?????????????????????????????????
     *
     * @param conditionNumber
     * @param ruleConditionList
     * @return
     */
    protected Entry<Boolean, BigDecimal> calculateWeight(BigDecimal conditionNumber, List<RuleCondition> ruleConditionList) {
        //ruleConditionList.stream().collect(Collectors.groupingBy(r->{return r.getConditionType()}));
        logger.info("conditionNumber=" + conditionNumber);

        for (RuleCondition ruleCondition : ruleConditionList) {

            String conditionWeight = ruleCondition.getWeight();// ??????
            String startValue = ruleCondition.getStartValue();// ?????????
            String endValue = ruleCondition.getEndValue();// ?????????
            String value = ruleCondition.getValue();// ?????????

            Integer conditonType = ruleCondition.getConditionType();// ??????: 60, ???????????? 20 ?????? 30, ???????????? 40, ?????? 50, ?????? 10
            // ??????????????????*?????????????????????
            boolean hitCondition = false;
            logger.info("startValue=" + startValue + ",value=" + value + ",endValue=" + endValue + ",conditonType=" + conditonType + ",ruleCondition.getId()=" + ruleCondition.getId());
            // ????????????????????????
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
            // ?????????????????????????????????????????????1
            if (hitCondition) {
                logger.info("??????????????????:" + ruleCondition.getId());
                return new AbstractMap.SimpleEntry<>(hitCondition, new BigDecimal(conditionWeight));
            }
        }
        return new AbstractMap.SimpleEntry<>(false, BigDecimal.ONE);
    }

}
