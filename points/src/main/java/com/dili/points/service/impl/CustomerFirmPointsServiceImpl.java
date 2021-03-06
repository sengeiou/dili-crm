package com.dili.points.service.impl;

import com.dili.points.constant.PointsConstants;
import com.dili.points.dao.CustomerFirmPointsMapper;
import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.CustomerFirmPointsDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.points.service.FirmService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dao.mapper.CommonMapper;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.POJOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.stream.Collectors;

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
    public CustomerFirmPoints getByCustomerIdAndFirm(Long customerId, String firmCode) {
        CustomerFirmPoints query = DTOUtils.newDTO(CustomerFirmPoints.class);
        query.setTradingFirmCode(firmCode);
        query.setCustomerId(customerId);
        CustomerFirmPoints customerPoints = getActualDao().select(query).stream().findFirst().orElseGet(() -> {
//            CustomerFirmPoints temp = DTOUtils.newDTO(CustomerFirmPoints.class);
//            temp.setAvailable(0);
//            temp.setSellerPoints(0);
//            temp.setBuyerPoints(0);
//            temp.setCustomerId(customerId);
//            temp.setTradingFirmCode(firmCode);
//            return temp;
            return null;
        });
        return customerPoints;
    }

    @Override
    public CustomerFirmPoints getByCertificateNumberAndFirm(String certificateNumber, String firmCode) {
        CustomerFirmPoints query = DTOUtils.newDTO(CustomerFirmPoints.class);
        query.setTradingFirmCode(firmCode);
        query.setCertificateNumber(certificateNumber);
        CustomerFirmPoints customerPoints = getActualDao().select(query).stream().findFirst().orElseGet(() -> {
//            CustomerFirmPoints temp = DTOUtils.newDTO(CustomerFirmPoints.class);
//            temp.setAvailable(0);
//            temp.setSellerPoints(0);
//            temp.setBuyerPoints(0);
//            temp.setCertificateNumber(certificateNumber);
//            temp.setTradingFirmCode(firmCode);
//            return temp;
            return null;
        });
        return customerPoints;
    }

    @Override
    public int mapperDeleteByExample(Object example) {
        return getActualDao().deleteByExample(example);
    }

    @Override
    public Map findCustomerFirmPointsByCertificateNumber(String customerId, String tradingFirmCode) {
        CustomerApiDTO dto = DTOUtils.newDTO(CustomerApiDTO.class);
        dto.setCustomerId(customerId);
        dto.setTradingFirmCode(tradingFirmCode);
        EasyuiPageOutput output = this.listCustomerFirmPointsByCustomer(dto);
        List<Map> list = output.getRows();
        return list.stream().findFirst().orElseGet(() -> {
            CustomerFirmPointsDTO cp = DTOUtils.newDTO(CustomerFirmPointsDTO.class);
            cp.setId(0L);
            cp.setCertificateNumber("");
            cp.setAvailable(0);
            cp.setFrozen(0);
            cp.setTotal(0);
            return DTOUtils.go(cp);
        });
    }

    @Override
    public EasyuiPageOutput listCustomerFirmPointsByCustomer(CustomerApiDTO customer) {
        customer.setSort(combineSortPrefix(customer.getSort()));

        //如果不是查询所有市场，并且未传入市场信息，则只能查询当前用户有数据权限的市场的数据
        if (!PointsConstants.ALL_MARKET.equals(customer.getTradingFirmCode()) && StringUtils.isBlank(customer.getTradingFirmCode()) && CollectionUtils.isEmpty(customer.getTradingFirmCodes())){
            List<String> firmCodes = firmService.getCurrentUserFirmCodes(customer.getUserId());
            if (CollectionUtils.isEmpty(firmCodes)) {
                return new EasyuiPageOutput(0, Collections.EMPTY_LIST);
            }
            customer.setTradingFirmCodes(firmCodes);
        }
        if(PointsConstants.ALL_MARKET.equals(customer.getTradingFirmCode())){
            customer.setTradingFirmCode(null);
            customer.setTradingFirmCodes(null);
        }
        if(StringUtils.isNotBlank(customer.getTradingFirmCode())){
            customer.setTradingFirmCodes(null);
        }

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
        footer.put("organizationType", this.calculateTotalPoints(customer));
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

        List<String> cSort = cPrefix.stream().filter( item -> POJOUtils.humpToLine(sort).equalsIgnoreCase(item)).map(item -> "c."+item).collect(Collectors.toList());
        return (cSort == null || cSort.isEmpty()) ? "cfp."+POJOUtils.humpToLine(sort) : cSort.get(0);
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
        CustomerFirmPoints item = super.list(example).stream().findFirst().orElseGet(() -> {
            CustomerFirmPoints customerFirmPoints = DTOUtils.clone(dto, CustomerFirmPoints.class);
            customerFirmPoints.setId(null);
            customerFirmPoints.setDayPoints(0);
            customerFirmPoints.setResetTime(new Date());
            customerFirmPoints.setAvailable(0);
            customerFirmPoints.setTotal(0);
            customerFirmPoints.setFrozen(0);
            return customerFirmPoints;
        });
        item.setTradingFirmCode(dto.getTradingFirmCode());
        item.setModifiedId(dto.getModifiedId());

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
        //非积分调整才修改买卖方积分
        if(!dto.isAdjust()) {
            if (dto.isBuyer()) {
                item.setBuyerPoints(item.getBuyerPoints() + actualPoints);
            } else {
                item.setSellerPoints(item.getSellerPoints() + actualPoints);
            }
        }
        item.setResetTime(new Date());
        item.setDayPoints(dayPoints);
        item.setAvailable(item.getAvailable() + actualPoints);
        item.setTotal(item.getAvailable());
        item.setFrozen(0);
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

    // 计算总可用积分
    private Long calculateTotalPoints(CustomerApiDTO customer) {
//        BigDecimal totalAvailablePoints = commonMapper.selectMap("select sum(available) as available from customer_firm_points where yn=1").stream()
//                .filter(m -> m != null && m.containsKey("available"))
//                .map(m -> {
//                    return (BigDecimal) m.get("available");
//                })
//                .findFirst()
//                .orElse(BigDecimal.ZERO);
//        return totalAvailablePoints.longValue();
        Long point = getActualDao().calculateTotalPoints(customer);
        return point == null ? 0 : point;
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

        BaseOutput<List<DataDictionaryValue>> output = this.dataDictionaryRpc.listDataDictionaryValue(condtion);
        if (!output.isSuccess() || output.getData() == null || output.getData().size() != 1) {
            throw new AppException("远程查询积分上限出错!");
        }
        String name = output.getData().get(0).getName();
        int limits = Integer.parseInt(name);
        return limits;
    }

}