package com.dili.dp.component;

import com.alibaba.fastjson.JSONObject;
import com.dili.dp.domain.Address;
import com.dili.dp.domain.City;
import com.dili.dp.domain.Customer;
import com.dili.dp.domain.CustomerExtensions;
import com.dili.dp.service.*;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.InternalException;
import com.dili.ss.util.AESUtil;
import com.dili.ss.util.SystemConfigUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 客户消费组件
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
@ConditionalOnExpression("'${mq.enable}'=='true'")
public class CustomerListener {
    private static final Logger logger = LoggerFactory.getLogger(CustomerListener.class);
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerExtensionsService customerExtensionsService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CityService cityService;
    @Autowired
    CustomerCategoryPointsService customerCategoryPointsService;
    @Value("${aesKey:}")
    private String aesKey;

    @RabbitListener(queues = "#{rabbitConfiguration.TOPIC_QUEUE}")
    public void processBootTask(Message message) throws Exception {
        logger.info("收到消息: " + message);
        String data = new String(message.getBody(), "UTF-8");
        String customerJson = AESUtil.decrypt(data, aesKey);
        logger.info("消息解密: " + customerJson);
        try {
            Optional<Map<String, Object>> mapOpt = convertAsMap(customerJson);
            mapOpt.ifPresent((jsonMap) -> {
                String type = StringUtils.trimToNull(String.valueOf(jsonMap.get("type")));
                if (type == null || type.equalsIgnoreCase("json")) {
                    Map<String, Object> dataMap = (Map<String, Object>) jsonMap.get("data");
                    //获得address
                    List<String> addressObjList = (List<String>) dataMap.remove("address");
                    //获得CustomerExtension
                    List<Map<String, Object>> extensionsObjList = (List<Map<String, Object>>) dataMap.remove("extensions");
                    //map中其余信息为Customer信息
                    Map<String, Object> customerObj = dataMap;

                    this.processCustomer(customerJson, customerObj, addressObjList, extensionsObjList);
                }
            });
        } catch (Exception e) {
            logger.error("转换Customer对象: {} 出错 {}", message, e);
        }
    }

    /**
     * 进行对象转换和验证并将数据插入(更新到数据库)
     *
     * @param customerJson      原始customer json
     * @param customerObj       转换为map的customer数据
     * @param addressObjList    转换为List<String>的address数据
     * @param extensionsObjList 转换为List<Map>的extension数据
     */
    private void processCustomer(String customerJson, Map<String, Object> customerObj, List<String> addressObjList, List<Map<String, Object>> extensionsObjList) {
        Optional<Customer> customerOpt = this.convertAndValidateCustomer(customerJson, customerObj);
        List<CustomerExtensions> extensionList = this.convertAndValidateExtension(customerOpt, extensionsObjList);
        List<Address> addressList = this.convertAndValidateAddress(addressObjList);

        customerOpt.ifPresent(customer -> {
            logger.info("process customer");
            this.saveOrUpdate(customer, extensionList, addressList);
        });
    }

    /**
     * @param addressObjList 转换为List<String>的address数据
     * @return 返回地址对象集合
     */
    private List<Address> convertAndValidateAddress(List<String> addressObjList) {
        List<Address> resultList = new ArrayList<>();
        if (addressObjList != null) {
            for (String addr : addressObjList) {
                //跳过空地址
                if (StringUtils.isBlank(addr)) {
                    continue;
                }
                Address address = DTOUtils.newDTO(Address.class);
                address.setAddress(addr);
                //设置经纬度
                this.setLocationToAddress(address);
                resultList.add(address);
            }
        }
        return resultList;
    }

    /**
     * @param customerJson 原始customer json
     * @param customerObj  转换为map的customer数据
     * @return Customer对象
     */
    private Optional<Customer> convertAndValidateCustomer(String customerJson, Map<String, Object> customerObj) {
        Customer customer = DTOUtils.proxy(new DTO(customerObj), Customer.class);
        if (this.validateCustomer(customer)) {
            //如果上报的客户信息有市场信息，则不做处理
//			if(StringUtils.isBlank(customer.getMarket())) {
//				customer.setMarket(this.getMarket());
//			}
            customer.setYn(1);
            return Optional.ofNullable(customer);
        } else {
            logger.error("验证客户信息出错:" + customerJson);
        }
        return Optional.empty();
    }

    /**
     * @param extensionsObjList 转换为List<Map>的extension数据
     * @return 转换后的List<CustomerExtensions>
     */
    private List<CustomerExtensions> convertAndValidateExtension(Optional<Customer> customerOpt, List<Map<String, Object>> extensionsObjList) {
        List<CustomerExtensions> resultList = new ArrayList<>();
        if (extensionsObjList != null) {
            for (Map<String, Object> map : extensionsObjList) {
                CustomerExtensions customerExtensions = DTOUtils.proxy(new DTO(map), CustomerExtensions.class);
                customerOpt.ifPresent(c -> {
                    customerExtensions.setSystem(c.getSourceSystem());
                });

                resultList.add(customerExtensions);
            }
        }
        return resultList;
    }

    private String getMarket() {
        return SystemConfigUtils.getProperty("customer.market", "hd");
    }

    /**
     * @param customer 要进行验证的customer对象
     * @return 验证无误返回true
     */
    private boolean validateCustomer(Customer customer) {
        if (StringUtils.isBlank(customer.getCertificateType())) {
            return false;
        }
        if (StringUtils.isBlank(customer.getCertificateNumber())) {
            return false;
        }
        if (StringUtils.isBlank(customer.getOrganizationType())) {
            return false;
        }
        if (StringUtils.isBlank(customer.getMarket())) {
            return false;
        }
        if (StringUtils.isBlank(customer.getName())) {
            return false;
        }
        if (StringUtils.isBlank(customer.getSex())) {
            return false;
        }
        if (StringUtils.isBlank(customer.getPhone())) {
            return false;
        }
        if (StringUtils.isBlank(customer.getSourceSystem())) {
            return false;
        }
        if (customer.getCreated() == null) {
            return false;
        }
        return true;
    }

    private void syncCustomerName(Long customerId, String customerName) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", customerId);
            jsonObject.put("name", customerName);
            customerCategoryPointsService.updateCustomerName(jsonObject.getString("name"),jsonObject.getLong("id"));
        } catch (Exception e) {
            logger.error("同步客户名修改失败:" + e.getMessage(), e);
        }
    }

    /**
     * @param customer               要插入或者更新到crm的客户信息
     * @param customerExtensionsList 要插入到或者更新到cmr的帐户信息
     * @return true保存成功, false 保存失败
     */
    @Transactional
    private boolean saveOrUpdate(Customer customer, List<CustomerExtensions> customerExtensionsList, List<Address> address) {


        Customer customerQueryCondition = DTOUtils.newDTO(Customer.class);
        customerQueryCondition.setCertificateNumber(customer.getCertificateNumber());
        customerQueryCondition.setCertificateType(customer.getCertificateType());
//		customerQueryCondition.setOrganizationType(customer.getOrganizationType());
        //用身份证号在crm系统查询用户信息
        List<Customer> list = this.customerService.list(customerQueryCondition);
        if (list != null && list.size() == 1) {
            Customer customerItem = list.get(0);
//            //更新用户信息
//            if (customerItem.getSyncTime() == null) {
//                customerItem.setSyncTime(customerItem.getCreated());
//            }
//            if (customer.getSyncTime() == null) {
//                customer.setSyncTime(new Date());
//            }
            //上报的客户类型和当前客户类型都不为空，并且不相等，合并客户类型为买卖家
            if (StringUtils.isNotBlank(customer.getType()) && StringUtils.isNotBlank(customerItem.getType()) && !customerItem.getType().equalsIgnoreCase(customer.getType())) {
                customerItem.setType("purchaseSale");
            }
//            if (customerItem.getSyncTime().before(customer.getSyncTime())) {
                String name = StringUtils.trimToEmpty(customer.getName());
                String phone = StringUtils.trimToEmpty(customer.getPhone());
                String sex = StringUtils.trimToEmpty(customer.getSex());
                if (StringUtils.isNotBlank(name) && !name.equals(customerItem.getName())) {
                    customerItem.setName(name);
                    this.syncCustomerName(customerItem.getId(), name);
                }
                if (StringUtils.isNotBlank(phone) && !phone.equals(customerItem.getPhone())) {
                    customerItem.setPhone(phone);
                }
                if (StringUtils.isNotBlank(sex) && !sex.equals(customerItem.getSex())) {
                    customerItem.setSex(sex);
                }
//            }
            customerItem.setSyncTime(customer.getSyncTime());
            customer = customerItem;
        }
//        logger.info("customer id:{},name:{},certificateNumber:{},system:{}", customer.getId(), customer.getName(), customer.getCertificateNumber(), customer.getSourceSystem());
        this.customerService.saveOrUpdate(customer);

        //对账号信息做插入或者更新的判断
        List<CustomerExtensions> insertExtensionList = new ArrayList<>();

        List<CustomerExtensions> updateExtensionList = new ArrayList<>();
        logger.info("customerExtensionsList.size:" + customerExtensionsList.size());
        for (CustomerExtensions customerExtensions : customerExtensionsList) {
            CustomerExtensions extensionsQueryCondition = DTOUtils.newDTO(CustomerExtensions.class);
            extensionsQueryCondition.setCustomerId(customer.getId());
            extensionsQueryCondition.setAcctId(customerExtensions.getAcctId());
            extensionsQueryCondition.setSystem(customerExtensions.getSystem());
            extensionsQueryCondition.setAcctType(customerExtensions.getAcctType());
            List<CustomerExtensions> extensions = this.customerExtensionsService.list(extensionsQueryCondition);
            if (extensions == null || extensions.size() == 0) {
                logger.info("新增客户扩展信息:"+customerExtensions.toString());
                customerExtensions.setCustomerId(customer.getId());
                insertExtensionList.add(customerExtensions);
            } else if (extensions.size() == 1) {
                CustomerExtensions extensionItem = extensions.get(0);
                logger.info("修改客户扩展信息:"+extensionItem.toString());
                if (!StringUtils.trimToEmpty(extensionItem.getNotes()).equals(StringUtils.trimToEmpty(customerExtensions.getNotes()))) {
                    extensionItem.setNotes(StringUtils.trimToEmpty(customerExtensions.getNotes()));
                    updateExtensionList.add(extensionItem);
                }
            }
        }
//        logger.info("insertExtensionList size:{}", insertExtensionList.size());
        if (insertExtensionList.size() > 0) {
            logger.info("新增客户扩展信息:"+insertExtensionList.size()+"条");
            this.customerExtensionsService.batchInsert(insertExtensionList);
        }
//        logger.info("updateExtensionList size:{}", updateExtensionList.size());
        if (updateExtensionList.size() > 0) {
            logger.info("修改客户扩展信息:"+updateExtensionList.size()+"条");
            this.customerExtensionsService.batchUpdate(updateExtensionList);
        }

        Address defaultAddrCondtion = DTOUtils.newDTO(Address.class);
        defaultAddrCondtion.setIsDefault(1);
        defaultAddrCondtion.setCustomerId(customer.getId());
        boolean hasDefaultAddr = this.addressService.listByExample(defaultAddrCondtion).size() > 0;

        //对地址信息做插入或者更新的判断
        List<Address> addrList = new ArrayList<>();
        for (Address addr : address) {
            Address addrCondtion = DTOUtils.newDTO(Address.class);

            addrCondtion.setAddress(addr.getAddress());
            addrCondtion.setCustomerId(customer.getId());
//					customerExtensions.setSystem(customerExtensions.getSystem());
//					
            List<Address> existedAddress = this.addressService.listByExample(addrCondtion);
            if (existedAddress == null || existedAddress.size() == 0) {
                addr.setCustomerId(customer.getId());
                addr.setIsDefault(0);
                //如果当前客户没有默认地址,将第一个设置为默认地址
                if (!hasDefaultAddr) {
                    if (StringUtils.isNotBlank(addr.getCityId())) {
                        addr.setIsDefault(1);
                        hasDefaultAddr = true;
                    }
                }
                addrList.add(addr);
            }
        }
        if (addrList.size() > 0) {
            this.addressService.batchInsert(addrList);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Address setLocationToAddress(Address address) {
        String addr = address.getAddress();
        try {
            //根据地址查询经纬度
            BaseOutput<Map<String, Object>> baseOut = addressService.locationAddress(addr);
            if (baseOut.isSuccess()) {
                Map<String, Object> cityJson = baseOut.getData();
                String lat = cityJson.get("lat").toString();
                String lng = cityJson.get("lng").toString();
                //根据经纬度查询行政区划代码
                BaseOutput<City> cityBaseOut = addressService.locationReverse(lat, lng);
                if (cityBaseOut.isSuccess()) {
                    City addressCity = cityBaseOut.getData();
                    City city = this.findParentCity(addressCity, 2);
                    if (city != null) {
                        //行政区划代码即为CityId [level_type:2]
                        String cityId = String.valueOf(city.getId());
                        //设置经纬度,城市
                        address.setCityId(cityId);
                    }
                    address.setLat(lat);
                    address.setLng(lng);

                } else {
                    logger.info("根据经纬度未能查询到行政区划代码");
                }
            } else {
                logger.info("根据地址未能查询到经纬度");
            }
        } catch (Exception e) {
            logger.error("同步地址信息,查询地址信息异常" + e.getMessage(), e);
        }

        return address;
    }

    private City findParentCity(City city, Integer levelType) {
        if (city == null || levelType == null) {
            return null;
        }
        int compareValue = levelType.compareTo(city.getLevelType());
        if (compareValue == 0) {
            return city;
        } else if (compareValue < 0) {
            Long parentId = city.getParentId();
            if (parentId != null) {
                City parentCity = this.cityService.get(parentId);
                if (parentCity != null) {
                    return this.findParentCity(parentCity, levelType);
                }
            }
            return null;
        } else {
            return null;
        }
    }


    /**
     * @param json 要进行转换为Map的原json字符串
     * @return 返回转换后的Optional<Map>
     */
    private Optional<Map<String,Object>> convertAsMap(String json) {
        if(StringUtils.isNoneBlank(json)) {
            ObjectMapper gson = new ObjectMapper();
            gson.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            gson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            gson.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            try {
                HashMap<String, Object> map = gson.readValue(json, new TypeReference<HashMap<String, Object>>() {});
                return Optional.ofNullable(map);
            } catch (IOException e) {
                throw new InternalException(e);
            }
        }
        return Optional.empty();
    }
}
