package com.dili.crm.service.impl;

import com.dili.crm.dao.CustomerStatsMapper;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.CustomerStats;
import com.dili.crm.domain.dto.CustomerStatsDto;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.CustomerStatsService;
import com.dili.crm.service.FirmService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-09-26 11:58:22.
 */
@Service
public class CustomerStatsServiceImpl extends BaseServiceImpl<CustomerStats, Long> implements CustomerStatsService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private FirmService firmService;

    public CustomerStatsMapper getActualDao() {
        return (CustomerStatsMapper)getDao();
    }

    @Override
    public void customerStats(){
        customerStatsByDate(new Date(), true);
    }

    @Override
    public void customerStatsByDate(Date date) {
        customerStatsByDate(date, true);
    }

    @Override
    public BaseOutput<List<Map>> listCustomerStatsIncrement(CustomerStatsDto customerStatsDto) throws Exception {
        if(customerStatsDto.getStartDate() == null) {
            return BaseOutput.failure("开始日期为空");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("登录超时");
        }
        List<String> firmCodes = firmService.getCurrentUserFirmCodes(userTicket.getId());
        if (CollectionUtils.isEmpty(firmCodes)){
            return BaseOutput.success();
        }
        customerStatsDto.setFirmCodes(firmCodes);
        //没有结束时间，则取今天为结束时间
        if(customerStatsDto.getEndDate() == null) {
            customerStatsDto.setEndDate(new Date());
        }
        //拉取开始时间的统计数据
        customerStatsByDate(customerStatsDto.getStartDate(), false);
        //查询开始和结束日期的统计数据
        CustomerStatsDto customerStats = DTOUtils.newDTO(CustomerStatsDto.class);
        customerStats.setFirmCodes(firmCodes);
        customerStats.setDate(DateUtils.formatDate2DateTimeStart(customerStatsDto.getStartDate()));
        List<CustomerStats> startCustomerStatsList = listByExample(customerStats);
        customerStats.setDate(DateUtils.formatDate2DateTimeStart(customerStatsDto.getEndDate()));
        List<CustomerStats> endCustomerStatsList = listByExample(customerStats);
        if(CollectionUtils.isEmpty(startCustomerStatsList) || CollectionUtils.isEmpty(endCustomerStatsList)){
            return BaseOutput.failure("无开始或结束日期的统计数据");
        }
        //用于保存增量市场客户统计
        List<CustomerStats> incrementList = Lists.newArrayList();
        for(CustomerStats endCustomerStats : endCustomerStatsList){
            CustomerStats startCustomerStats = getByFirmCode(startCustomerStatsList, endCustomerStats.getFirmCode());
            Integer startCustomerCount = startCustomerStats == null ? 0 : startCustomerStats.getCustomerCount();
            CustomerStats increment = DTOUtils.newDTO(CustomerStats.class);
            increment.setCustomerCount(endCustomerStats.getCustomerCount() - startCustomerCount);
            increment.setFirmCode(endCustomerStats.getFirmCode());
            incrementList.add(increment);
        }
        return BaseOutput.success().setData(ValueProviderUtils.buildDataByProvider(customerStatsDto, incrementList));
    }

    @Override
    public BaseOutput<List<Map>> listCustomerStats(CustomerStatsDto customerStatsDto) throws Exception {
        //没有结束时间，则取今天为结束时间
        if(customerStatsDto.getEndDate() == null) {
            customerStatsDto.setEndDate(new Date());
        }
        //有开始时间，则需要拉取统计数据
        if(customerStatsDto.getStartDate() != null) {
            pullData(customerStatsDto);
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("登录超时");
        }
        List<String> firmCodes = firmService.getCurrentUserFirmCodes(userTicket.getId());
        if (CollectionUtils.isEmpty(firmCodes)){
            return BaseOutput.success();
        }
        customerStatsDto.setFirmCodes(firmCodes);
        return BaseOutput.success().setData(ValueProviderUtils.buildDataByProvider(customerStatsDto, listByExample(customerStatsDto)));
    }

    @Override
    public void customerStatsByDates(Date startDate, Date endDate) {
        getDates(startDate, endDate).stream().forEach(date -> customerStatsByDate(date, false));
    }

    /**
     * 循环客户统计对象，根据firmCode获取
     * @param customerStatsList
     * @param firmCode
     * @return
     */
    private CustomerStats getByFirmCode(List<CustomerStats> customerStatsList, String firmCode){
        for(CustomerStats customerStats : customerStatsList){
            if(firmCode.equals(customerStats.getFirmCode())){
                return customerStats;
            }
        }
        return null;
    }

    /**
     * 拉取数据
     * @param customerStatsDto
     */
    private void pullData(CustomerStatsDto customerStatsDto){
        //支持统计客户数的最早时间，2018年1月1日
        Date EARLIEST_DATE = DateUtils.dateStr2Date("2018-1-1 0:0:0", "yyyy-MM-dd HH:mm:ss");
        if(customerStatsDto.getStartDate().before(EARLIEST_DATE)){
            customerStatsDto.setStartDate(EARLIEST_DATE);
        }
        //查询客户统计表已有数据的最早时间
        CustomerStatsDto condition = DTOUtils.newDTO(CustomerStatsDto.class);
        condition.setSelectColumns(Sets.newHashSet("min(date) date"));
        List<CustomerStats> customerStats = listByExample(condition);
        //查询客户表客户的最早创建时间
        Customer customer = DTOUtils.newDTO(Customer.class);
        customer.setYn(1);
        customer.setSelectColumns(Sets.newHashSet("min(created) created"));
        List<Customer> customers = customerService.listByExample(customer);
        //没有客户数据，就不用拉取了
        if(CollectionUtils.isEmpty(customers)){
            return;
        }
        //最早客户创建时间
        Date earliestCreated = customers.get(0).getCreated();
        //开始时间不早于最早的客户的创建时间
        if(customerStatsDto.getStartDate().before(earliestCreated)){
            customerStatsDto.setStartDate(earliestCreated);
        }
        //判断开始时间是否早于CustomerStats表中的已有数据最早时间，如果更早,则需要先拉取开始时间到已有数据最早时间的数据
        if (!customerStats.isEmpty() && customerStatsDto.getStartDate().before(customerStats.get(0).getDate())) {
            //拉取开始时间到已有数据最早时间的数据
            customerStatsByDates(customerStatsDto.getStartDate(), customerStats.get(0).getDate());
        }
        //拉取今天的最新实时数据
        //获取今天的0点0分
        Date today = DateUtils.formatDate2DateTimeStart(new Date());
        //如果结束时间大于等于今天，则需要更新今天的客户统计
        if(customerStatsDto.getEndDate().getTime() >= today.getTime()) {
            customerStats();
        }
    }

    /**
     * 获取两个时间之间的日期，
     * @param startDate
     * @param endDate
     * @return 所有时间会被设置为23:59:59， 用于统计当天的客户数量
     */
    private List<Date> getDates(Date startDate, Date endDate){
        //设置日期结束时间为23:59:59.999
        endDate = DateUtils.formatDate2DateTimeEnd(endDate);
        Calendar startCalendar = Calendar.getInstance();
        //设置日期起始时间
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.HOUR_OF_DAY, 23);
        startCalendar.set(Calendar.MINUTE, 59);
        startCalendar.set(Calendar.SECOND, 59);
        startCalendar.set(Calendar.MILLISECOND, 999);
        List<Date> dates = new ArrayList<>();
        //判断是否到结束日期
        while (startCalendar.getTime().before(endDate) || startCalendar.getTime().equals(endDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dates.add(startCalendar.getTime());
            //进行当前日期月份加1
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    /**
     * 按指定日期统计客户数
     * @param date
     * @param update    是否更新已有日期的统计
     */
    private void customerStatsByDate(Date date, boolean update) {
        CustomerStats customerStats = DTOUtils.newDTO(CustomerStats.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        try {
            customerStats.setDate(sdf.parse(dateStr));
        } catch (ParseException e) {
        }
        //是否存在指定日期的数据
        boolean contains = !getActualDao().select(customerStats).isEmpty();
        if(contains){
            //如果有数据，并且需要更新，则删了重新插入
            if(update) {
                getActualDao().delete(customerStats);
                getActualDao().customerStatsByDate(date);
            }//有数据，不需要更新
        }else {
            //无数据则直接插入
            getActualDao().customerStatsByDate(date);
        }
    }
}