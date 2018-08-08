package com.dili.points.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.points.dao.CustomerFirmPointsMapper;
import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.CustomerFirmPointsDTO;
import com.dili.points.domain.dto.CustomerPointsDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.rpc.DataDictionaryRpc;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.points.service.FirmService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dao.mapper.CommonMapper;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.stream.Collectors;

import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-07-30 16:20:03.
 */
@Service
public class CustomerFirmPointsServiceImpl extends BaseServiceImpl<CustomerFirmPoints, Long> implements CustomerFirmPointsService {

    @Autowired
    DataDictionaryRpc dataDictionaryRpc;

    @Autowired
    CustomerRpc customerRpc;

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    FirmService firmService;

    public CustomerFirmPointsMapper getActualDao() {
        return (CustomerFirmPointsMapper) getDao();
    }

    @Override
    public CustomerFirmPoints getByCustomerAndFirm(Long customerId, String firmCode) {
        CustomerFirmPoints query = DTOUtils.newDTO(CustomerFirmPoints.class);
        query.setTradingFirmCode(firmCode);
        query.setCustomerId(customerId);
        CustomerFirmPoints customerPoints = getActualDao().select(query).stream().findFirst().orElseGet(() -> {
            CustomerFirmPoints temp = DTOUtils.newDTO(CustomerFirmPoints.class);
            temp.setAvailable(0);
            temp.setSellerPoints(0);
            temp.setBuyerPoints(0);
            temp.setCustomerId(customerId);
            temp.setTradingFirmCode(firmCode);
            return temp;
        });
        return customerPoints;
    }

    @Override
    public int deleteByExample(Object example) {
        return getActualDao().deleteByExample(example);
    }

    @Override
    public Map findCustomerFirmPointsByCertificateNumber(String certificateNumber) {
        CustomerApiDTO dto = DTOUtils.newDTO(CustomerApiDTO.class);
        dto.setCertificateNumber(certificateNumber);
        EasyuiPageOutput output = this.listCustomerFirmPointsByCustomer(dto);
        List<Map> list = output.getRows();
        return list.stream().findFirst().orElseGet(() -> {
            CustomerFirmPointsDTO cp = DTOUtils.newDTO(CustomerFirmPointsDTO.class);
            cp.setId(0L);
            cp.setCertificateNumber(certificateNumber);
            cp.setAvailable(0);
            cp.setFrozen(0);
            cp.setTotal(0);
            return DTOUtils.go(cp);
        });
    }

    @Override
    public EasyuiPageOutput listCustomerFirmPointsByCustomer(CustomerApiDTO customer) {
        customer.setSort(combineSortPrefix(customer.getSort()));
        Integer page = customer.getPage();
        page = (page == null) ? Integer.valueOf(1) : page;
        if(customer.getRows() != null && customer.getRows() >= 1) {
            //为了线程安全,请勿改动下面两行代码的顺序
            PageHelper.startPage(page, customer.getRows());
        }else{
            PageHelper.startPage(1, 10000);
        }
        List<Map> list = getActualDao().listByPage(customer);
        Page<Map> pageList = (Page) list;
        try {
            list = ValueProviderUtils.buildDataByProvider(customer, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EasyuiPageOutput easyuiPageOutput = new EasyuiPageOutput(Integer.parseInt(String.valueOf(pageList.getTotal())), list);
        //页脚汇总
        List<Map<String,Object>> footers = Lists.newArrayList();
        Map<String,Object> footer = new HashMap<>(2);
        footer.put("name", "总可用积分:");
        footer.put("organizationType", this.calculateTotalPoints());
        footers.add(footer);
        easyuiPageOutput.setFooter(footers);
        return easyuiPageOutput;
    }

    /**
     * 组合客户积分查询排序列sql表前缀
     * @param sort
     * @return
     */
    private String combineSortPrefix(String sort){
        if(StringUtils.isBlank(sort)){
            return null;
        }
        List<String> cPrefix = Lists.newArrayList("name", "organization_type", "certificate_type", "type", "certificate_number", "profession");

        List<String> cSort = cPrefix.stream().filter( item -> sort.equalsIgnoreCase(item)).map(item -> "c."+item).collect(Collectors.toList());
        return (cSort == null || cSort.isEmpty()) ? "cfp."+sort : cSort.get(0);
    }

    /**
     * 保存数据
     * @param dto
     * @return
     */
    @Override
    public int saveCustomerFirmPoints(CustomerFirmPointsDTO dto) {
        if (StringUtils.isBlank(dto.getCertificateNumber()) || dto.getCustomerId() == null) {
            return 0;
        }
        if (StringUtils.isBlank(dto.getTradingFirmCode())) {
            return 0;
        }
        //数据设置默认值
        if (dto.getPoints() == null) {
            dto.setPoints(0);
        }
        if (dto.getBuyerPoints() == null) {
            dto.setBuyerPoints(0);
        }
        if (dto.getSellerPoints() == null) {
            dto.setSellerPoints(0);
        }
        CustomerFirmPoints example = DTOUtils.newDTO(CustomerFirmPoints.class);
        example.setCustomerId(dto.getCustomerId());
        example.setTradingFirmCode(dto.getTradingFirmCode());

        //查询
        CustomerFirmPoints item = this.list(example).stream().findFirst().orElseGet(() -> {
            CustomerFirmPoints customerFirmPoints = DTOUtils.clone(dto, CustomerFirmPoints.class);
            customerFirmPoints.setId(null);
            customerFirmPoints.setDayPoints(0);
            customerFirmPoints.setResetTime(new Date());
            return customerFirmPoints;
        });

        // 积分上限
        int total = this.findDailyLimit(dto.getTradingFirmCode());
        // 已有当天积分总和
        Integer dayPoints = item.getDayPoints();
        //修改时间
        LocalDate resetDate = Optional.ofNullable(item.getResetTime().toInstant()).orElseGet(() -> {
            TemporalAmount tm = Duration.ofDays(1);
            return Instant.now().minus(tm);
        }).atZone(ZoneId.systemDefault()).toLocalDate();

        // 本次可积分
        Integer points = dto.getPoints();
        // 本次实际积分
        Integer actualPoints = 0;
        //在积分值大于零的时候进行上限判断是否超过上限
        if (points > 0) {
            //当前时间
            LocalDate currentDate = LocalDate.now();
            // 如果上次累积积分的时间为今天时,进行积分上限处理
            if (resetDate.isEqual(currentDate)) {
                // 如果当天积分总和已经超过上限,当前积分详情的积分归为0,当天积分总和分为total
                if (dayPoints >= total) {
                    actualPoints = 0;
                    dayPoints = total;
                } else {
                    // 如果积分上限与当天积分总和差值小于当前积分详情积分,则重新计算当前积分,并且设置当天积分总和分为total
                    if ((total - dayPoints) <= points) {
                        actualPoints = total - dayPoints;
                        dayPoints = total;
                    } else {// 直接对当天积分总和累加
                        actualPoints = points;
                        dayPoints = dayPoints + points;
                    }
                }
            } else {
                //单次积分超过最大值
                if (total < points) {
                    actualPoints = total;
                    dayPoints = total;
                } else {
                    actualPoints = points;
                    dayPoints = points;
                }
            }
        } else {
            //针对扣分的情况
            actualPoints = points;
        }


        if (dto.isBuyer()) {
            item.setBuyerPoints(item.getBuyerPoints() + actualPoints);
        } else {
            item.setSellerPoints(item.getSellerPoints() + actualPoints);
        }
        item.setResetTime(new Date());
        item.setDayPoints(dayPoints);
        item.setAvailable(item.getBuyerPoints() + item.getSellerPoints());
        //将实际积分和余额通过dto传回去
        dto.setActualPoints(actualPoints);
        dto.setAvailable(item.getAvailable());
        if (item.getId() == null) {
            return this.getActualDao().insertExact(item);
        } else {
            return this.updateExact(item);
        }

    }

//===================================   私有方法分割  =========================================

    /**
     * 根据非可用积分排序的查询
     * @param customer
     * @return	返回总记录数
     */
    private EasyuiPageOutput sortByNoneAvailable(CustomerApiDTO customer){
        //        客户积分列表的最终结果
        List<CustomerFirmPointsDTO> resultList = Lists.newArrayList();
        customer.setYn(1);
        CustomerApiDTO apiDto=	DTOUtils.clone(customer, CustomerApiDTO.class);
        BaseOutput<EasyuiPageOutput> baseOut = customerRpc.listPage(apiDto);
        if (!baseOut.isSuccess()) {
            throw new AppException("远程调用失败:"+baseOut.getResult());
        }
        //设置总数，计算完resultList后返回
        int total = baseOut.getData().getTotal();
        List<JSONObject> customerDataRows = baseOut.getData().getRows();
        //构建客户列表
        List<Customer> customerList = customerDataRows.stream().map(json -> {
            return (Customer) DTOUtils.as(new DTO(json), Customer.class);
        }).collect(Collectors.toList());
        //客户列表的证件号码
        List<String> certificateNumbers = customerList.stream()
                .map(Customer::getCertificateNumber)
                .collect(Collectors.toList());

        //查询客户市场积分
        CustomerFirmPointsDTO example = DTOUtils.newDTO(CustomerFirmPointsDTO.class);
        example.setCertificateNumbers(certificateNumbers);
        example.setPage(customer.getPage());
        example.setRows(customer.getRows());
        //如果没有交易市场，取当前数据权限范围内所有交易市场
        if(customer.getTradingFirmCode() == null) {
            example.setTradingFirmCodes(firmService.getCurrentUserFirmCodes());
        }else{
            example.setTradingFirmCode(customer.getTradingFirmCode());
        }
        example.setYn(1);
        List<CustomerFirmPoints> customerFirmPoints = this.listByExample(example);

        Map<String, CustomerFirmPoints> certificateNumber2CustomerPointsMap = customerFirmPoints.stream()
                .collect(Collectors.toMap(CustomerFirmPoints::getCertificateNumber, cp -> cp));
        resultList.addAll(customerList.stream().map(c -> {
            CustomerFirmPoints cp = certificateNumber2CustomerPointsMap.get(c.getCertificateNumber());
            // 如果客户没有对应的积分信息,则创建一个新的默认积分信息显示到页面
            CustomerFirmPointsDTO cpdto = DTOUtils.newDTO(CustomerFirmPointsDTO.class);
            if (cp == null) {
                cpdto.setCustomerId(c.getId());
                cpdto.setCertificateNumber(c.getCertificateNumber());
                cpdto.setAvailable(0);
                cpdto.setFrozen(0);
                cpdto.setTotal(0);
            } else {
                cpdto = DTOUtils.link(cpdto, cp, CustomerFirmPointsDTO.class);
            }
            // 将客户的其他信息(名字,组织类型等信息附加到积分信息)
            cpdto.setName(c.getName());
            cpdto.setOrganizationType(c.getOrganizationType());
            cpdto.setProfession(c.getProfession());
            cpdto.setType(c.getType());
            cpdto.setCertificateType(c.getCertificateType());
            cpdto.setPhone(c.getPhone());
            return cpdto;
        }).collect(Collectors.toList()));
        return new EasyuiPageOutput(total, resultList);
    }

    /**
     * 根据可用积分排序的查询
     * @param customer
     * @return	返回EasyuiPageOutput
     */
    private EasyuiPageOutput sortByAvailable(CustomerApiDTO customer){
//        客户积分列表的最终结果
        List<CustomerFirmPointsDTO> resultList = Lists.newArrayList();
        //如果是可用积分排序，需要去掉available字段，因为RPC调用CRM的客户时，会因为客户表没有available字段而报错
        //这里先记录下来，后面会用于积分表的排序
        String order = customer.getOrder();
        //记录每页条数和页数
        Integer rows = customer.getRows();
        Integer page = customer.getPage();
        //记录列表总数
        int total = 0;
        //客户表不能按可用积分列表，所以要去掉
        customer.setSort(null);
        customer.setOrder(null);
        customer.setYn(1);
        //关联积分表只能全查客户
        customer.setRows(null);
        customer.setPage(null);
        BaseOutput<EasyuiPageOutput> baseOut = customerRpc.listPage(customer);
        if (!baseOut.isSuccess()) {
            throw new AppException("远程调用失败:"+baseOut.getResult());
        }
        List<JSONObject> jsonList = baseOut.getData().getRows();
        //构建客户列表
        List<Customer> customerList = jsonList.stream().map(json -> {
            return (Customer) DTOUtils.as(new DTO(json), Customer.class);
        }).collect(Collectors.toList());

        //构建客户积分查询条件，查询所有客户积分
        CustomerFirmPointsDTO example = DTOUtils.newDTO(CustomerFirmPointsDTO.class);
        example.setPage(customer.getPage());
        example.setRows(customer.getRows());
        example.setOrder(order);
        //如果没有交易市场，取当前数据权限范围内所有交易市场
        if(customer.getTradingFirmCode() == null) {
            example.setTradingFirmCodes(firmService.getCurrentUserFirmCodes());
        }else{
            example.setTradingFirmCode(customer.getTradingFirmCode());
        }
        example.setSort("available");
        example.setYn(1);
        List<CustomerFirmPoints> customerPointsList = this.listByExample(example);
        //构建客户证件号为key，客户积分为value的Map
        Map<String, CustomerFirmPoints> certificateNumber2CustomerPointsMap = customerPointsList.stream()
                .collect(Collectors.toMap(CustomerFirmPoints::getCertificateNumber, c -> c));
        //先根据客户积分表的数据构建客户积分结果列表
        resultList.addAll(customerList.stream().map(c -> {
            CustomerFirmPointsDTO cpdto = DTOUtils.newDTO(CustomerFirmPointsDTO.class);
            CustomerFirmPoints customerPoints = certificateNumber2CustomerPointsMap.get(c.getCertificateNumber());
            if (customerPoints == null) {
                cpdto.setCustomerId(c.getId());
                cpdto.setCertificateNumber(c.getCertificateNumber());
                cpdto.setAvailable(0);
                cpdto.setFrozen(0);
                cpdto.setTotal(0);
            } else {
                cpdto = DTOUtils.as(customerPoints, CustomerFirmPointsDTO.class);
            }
            // 将客户的其他信息(名字,组织类型等信息附加到积分信息)
            cpdto.setName(c.getName());
            cpdto.setOrganizationType(c.getOrganizationType());
            cpdto.setProfession(c.getProfession());
            cpdto.setType(c.getType());
            cpdto.setCertificateType(c.getCertificateType());
            cpdto.setPhone(c.getPhone());
            return cpdto;
        }).collect(Collectors.toList()));
        //排序
        if("desc".equalsIgnoreCase(order)){
            resultList.sort(Comparator.comparingInt(CustomerFirmPointsDTO::getAvailable).reversed());
        }else{
            resultList.sort(Comparator.comparingInt(CustomerFirmPointsDTO::getAvailable));
        }
        total = resultList.size();
        if(rows != null && page != null) {
            //根据分页信息进行本地分页
            int startIndex = (page - 1) * rows;
            if(rows > total){
                rows = total;
            }
            return new EasyuiPageOutput(total, resultList.subList(startIndex, startIndex + rows));
        }
        return new EasyuiPageOutput(total, resultList);
    }

    // 计算总可用积分
    private Long calculateTotalPoints() {
        BigDecimal totalAvailablePoints = commonMapper.selectMap("select sum(available) as available from customer_firm_points where yn=1").stream()
                .filter(m -> m != null && m.containsKey("available"))
                .map(m -> {
                    return (BigDecimal) m.get("available");
                })
                .findFirst()
                .orElse(BigDecimal.ZERO);
        return totalAvailablePoints.longValue();
    }

    /**
     * 查询指定市场的每天积分上限值
     *
     * @param firmCode
     * @return
     */
    private Integer findDailyLimit(String firmCode) {
        DataDictionaryValue condtion = DTOUtils.newDTO(DataDictionaryValue.class);
        condtion.setDdCode("points_daily_limits");
        condtion.setCode(firmCode);

        BaseOutput<List<DataDictionaryValue>> output = this.dataDictionaryRpc.list(condtion);
        if (!output.isSuccess() || output.getData() == null || output.getData().size() != 1) {
            throw new AppException("远程查询积分上限出错!");
        }
        String name = output.getData().get(0).getName();
        int limits = Integer.parseInt(name);
        return limits;
    }

}