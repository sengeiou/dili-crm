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
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        customerStatsByDate(new Date(), null, true);
    }

    @Override
    public void customerStatsByDate(Date date) {
        customerStatsByDate(date, null, true);
    }

    @Override
    public BaseOutput<List<Map>> listRealTimeCustomerStatsIncrement(CustomerStatsDto customerStatsDto) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("登录超时");
        }
        setDefaultDateCondition(customerStatsDto);
        //默认查一个月
        initStartDate(customerStatsDto);
        List<String> firmCodes = firmService.getCurrentUserFirmCodes(userTicket.getId());
        if (CollectionUtils.isEmpty(firmCodes)){
            return BaseOutput.success();
        }
        customerStatsDto.setFirmCodes(firmCodes);
        List<CustomerStats> customerStats = getActualDao().listCustomerIncrement(customerStatsDto);
        return BaseOutput.success().setData(ValueProviderUtils.buildDataByProvider(customerStatsDto, customerStats));
    }


    @Override
    public BaseOutput<List<Map>> listRealTimeCustomerStats(CustomerStatsDto customerStatsDto) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("登录超时");
        }
        setDefaultDateCondition(customerStatsDto);
        //默认查一个月
        initStartDate(customerStatsDto);
        List<String> firmCodes = firmService.getCurrentUserFirmCodes(userTicket.getId());
        if (CollectionUtils.isEmpty(firmCodes)){
            return BaseOutput.success();
        }
        customerStatsDto.setFirmCodes(firmCodes);

        customerStatsDto.setDates(getDates(customerStatsDto.getStartDate(), customerStatsDto.getEndDate()));
        List<CustomerStats> customerStats = getActualDao().listCustomerStats(customerStatsDto);
        return BaseOutput.success().setData(ValueProviderUtils.buildDataByProvider(customerStatsDto, customerStats));
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
        //默认查一个月
        initStartDate(customerStatsDto);
        List<String> firmCodes = firmService.getCurrentUserFirmCodes(userTicket.getId());
        if (CollectionUtils.isEmpty(firmCodes)){
            return BaseOutput.success();
        }
        //开始时间不晚于结束时间
        if(customerStatsDto.getStartDate().after(customerStatsDto.getEndDate())) {
            customerStatsDto.setStartDate(customerStatsDto.getEndDate());
        }
        //拉取的开始时间必须比查询的开始时间早一点，用于查询客户增量时，必须查前一天的客户量
        customerStatsDto.setStartDate(DateUtils.addDays(customerStatsDto.getStartDate(), -1));
        customerStatsDto.setFirmCodes(firmCodes);
        //没有结束时间，则取今天为结束时间
        if(customerStatsDto.getEndDate() == null) {
            customerStatsDto.setEndDate(getCurrentDate());
        }
        //查询开始和结束日期的统计数据
        CustomerStatsDto customerStats = DTOUtils.newDTO(CustomerStatsDto.class);
        customerStats.setFirmCodes(firmCodes);
        customerStats.setDate(DateUtils.formatDate2DateTimeStart(customerStatsDto.getEndDate()));
        List<CustomerStats> endCustomerStatsList = listByExample(customerStats);
        if(CollectionUtils.isEmpty(endCustomerStatsList)){
            return BaseOutput.success();
        }
        customerStats.setDate(DateUtils.formatDate2DateTimeStart(customerStatsDto.getStartDate()));
        List<CustomerStats> startCustomerStatsList = listByExample(customerStats);
        //用于保存增量市场客户统计
        List<CustomerStats> incrementList = Lists.newArrayList();
        for(CustomerStats endCustomerStats : endCustomerStatsList){
            CustomerStats startCustomerStats = getByFirmCode(startCustomerStatsList, endCustomerStats.getFirmCode());
            Integer startCustomerCount = startCustomerStats == null ? 0 : startCustomerStats.getCustomerCount();
            CustomerStats increment = DTOUtils.newDTO(CustomerStats.class);
            int count = endCustomerStats.getCustomerCount() - startCustomerCount;
            increment.setCustomerCount(count < 0 ? 0 : count);
            increment.setFirmCode(endCustomerStats.getFirmCode());
            incrementList.add(increment);
        }
        return BaseOutput.success().setData(ValueProviderUtils.buildDataByProvider(customerStatsDto, incrementList));
    }

    @Override
    public BaseOutput<List<Map>> listCustomerStats(CustomerStatsDto customerStatsDto) throws Exception {
        //没有结束时间，则取今天为结束时间
        if(customerStatsDto.getEndDate() == null) {
            customerStatsDto.setEndDate(getCurrentDate());
        }
        //有开始时间，则需要拉取统计数据
//        if(customerStatsDto.getStartDate() != null) {
//            pullData(customerStatsDto);
//        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("登录超时");
        }
        //默认查一个月
        initStartDate(customerStatsDto);
        List<String> firmCodes = firmService.getCurrentUserFirmCodes(userTicket.getId());
        if (CollectionUtils.isEmpty(firmCodes)){
            return BaseOutput.success();
        }
        customerStatsDto.setFirmCodes(firmCodes);
        return BaseOutput.success().setData(ValueProviderUtils.buildDataByProvider(customerStatsDto, listByExample(customerStatsDto)));
    }

    @Override
    public void customerStatsByDates(Date startDate, Date endDate, String firmCode) {
        getDates(startDate, endDate).stream().forEach(date -> customerStatsByDate(date, firmCode, false));
    }


    /**
     * 拉取数据
     * @param customerStatsDto
     */
    @Override
    public BaseOutput pullData(CustomerStatsDto customerStatsDto){
        //支持统计客户数的最早时间，2018年1月1日
        final Date EARLIEST_DATE = DateUtils.dateStr2Date("2018-1-1 0:0:0", "yyyy-MM-dd HH:mm:ss");
        if(customerStatsDto.getStartDate().before(EARLIEST_DATE)){
            customerStatsDto.setStartDate(EARLIEST_DATE);
        }
        //没有结束时间，则取今天为结束时间
        if(customerStatsDto.getEndDate() == null) {
            customerStatsDto.setEndDate(getCurrentDate());
        }
        //查询客户表客户的最早创建时间
        Customer customer = DTOUtils.newDTO(Customer.class);
        customer.setYn(1);
        customer.setSelectColumns(Sets.newHashSet("min(created) created"));
        List<Customer> customers = customerService.listByExample(customer);
        //没有客户数据，就不用拉取了
        if(CollectionUtils.isEmpty(customers) || customers.get(0) == null){
            return BaseOutput.success();
        }
        //最早客户创建时间
        Date earliestCreated = customers.get(0).getCreated();
        //拉取的开始时间不早于最早的客户的创建时间，以提高性能
        if(customerStatsDto.getStartDate().before(earliestCreated)){
            customerStatsDto.setStartDate(earliestCreated);
        }
        //拉取的开始时间必须比查询的开始时间早一点，用于查询客户增量时，必须查前一天的客户量
        customerStatsDto.setStartDate(DateUtils.addDays(customerStatsDto.getStartDate(), -1));
        // == 先要判断是否有新的市场导入 ==
        //查询客户表有哪些市场的客户
        customer.setSelectColumns(Sets.newHashSet("distinct market"));
        List<Customer> customerMarkets = customerService.listByExample(customer);
        //如果客户表为空，则不需要拉取数据
        if(CollectionUtils.isEmpty(customerMarkets)){
            return BaseOutput.success();
        }
        //查询客户统计表有哪些市场的数据
        List<CustomerStats> customerStatsFirmCodes = getActualDao().selectDistinctFirmCode();
        //没有新的市场，则直接拉更早的所有市场数据和最新的数据
        if(CollectionUtils.isEmpty(customerStatsFirmCodes) || customerMarkets.size() == customerStatsFirmCodes.size()){
            pullEarlyData(customerStatsDto);
            pullLatestData(customerStatsDto);
        }else{//有新的市场，则需要按客户表中的市场分别拉取
            pullDistinctMarketData(customerStatsDto, customerMarkets);
        }
        //拉取今天所有市场的最新实时数据
        //获取今天的0点0分
        Date today = DateUtils.formatDate2DateTimeStart(new Date());
        //如果结束时间大于等于今天，则需要更新今天的客户统计
        if(customerStatsDto.getEndDate() == null || customerStatsDto.getEndDate().getTime() >= today.getTime()) {
            customerStats();
        }
        return BaseOutput.success();
    }

    @Override
    public void updateCustomerCount(CustomerStats customerStats) {
        getActualDao().updateCustomerCount(customerStats);
    }

    @Override
    public void pullCustomerStatsByMarkets(Date startDate, Date endDate, List<String> firmCodes){
        if(firmCodes == null){
            return;
        }
        //如果拉取的开始时间早于现有统计数据的最早时间，则会统计其它所有市场从startDate到现有时间的数据
        //如果现有数据未统计，则统计startDate到当前时间的所有数据
        CustomerStatsDto customerStatsDto = DTOUtils.newDTO(CustomerStatsDto.class);
        customerStatsDto.setStartDate(startDate);
        pullEarlyData(customerStatsDto);
        //强制更新指定市场的数据
        for(String firmCode : firmCodes){
            //判断开始时间是否早于CustomerStats表中的已有数据最早时间，如果更早,则需要先拉取开始时间到已有数据最早时间的数据
            if (startDate.before(endDate)) {
                //拉取开始时间到已有数据最早时间的数据
                getDates(startDate, endDate).stream().forEach(date -> customerStatsByDate(date, firmCode, true));
            }
        }
    }

    /**
     * 分别拉取各市场数据
     * @param customerStatsDto
     * @param customerMarkets 客户表中的市场
     */
    private void pullDistinctMarketData(CustomerStatsDto customerStatsDto, List<Customer> customerMarkets){
        for(Customer customerMarket : customerMarkets){
            customerStatsDto.setFirmCode(customerMarket.getMarket());
            pullEarlyData(customerStatsDto);
        }
    }

    /**
     * 拉取更早的数据
     * @param customerStatsDto
     * @return
     */
    private void pullEarlyData(CustomerStatsDto customerStatsDto){
        //查询客户统计表已有数据的最早时间
        CustomerStatsDto condition = DTOUtils.newDTO(CustomerStatsDto.class);
        condition.setSelectColumns(Sets.newHashSet("min(date) date"));
        if(StringUtils.isNotBlank(customerStatsDto.getFirmCode())){
            condition.setFirmCode(customerStatsDto.getFirmCode());
        }
        List<CustomerStats> customerStats = listByExample(condition);
        //客户统计表已有数据最早时间，因为是min(date)聚合函数，所以一定会有一条数据，不过第一条可能是null
        //如果客户统计表没数据，则取当前时间为统计表时间
        Date earliestCustomerStats = customerStats.get(0) == null ? new Date() : customerStats.get(0).getDate();
        //判断开始时间是否早于CustomerStats表中的已有数据最早时间，如果更早,则需要先拉取开始时间到已有数据最早时间的数据
        if (customerStatsDto.getStartDate().before(earliestCustomerStats)) {
            //拉取开始时间到已有数据最早时间的数据
            customerStatsByDates(customerStatsDto.getStartDate(), earliestCustomerStats, customerStatsDto.getFirmCode());
        }
    }

    /**
     * 拉取最近的数据
     * @param customerStatsDto
     * @return
     */
    private void pullLatestData(CustomerStatsDto customerStatsDto){
        //查询客户统计表已有数据的最早时间
        CustomerStatsDto condition = DTOUtils.newDTO(CustomerStatsDto.class);
        condition.setSelectColumns(Sets.newHashSet("max(date) date"));
        if(StringUtils.isNotBlank(customerStatsDto.getFirmCode())){
            condition.setFirmCode(customerStatsDto.getFirmCode());
        }
        List<CustomerStats> customerStats = listByExample(condition);
        //客户统计表已有数据最早时间，因为是max(date)聚合函数，所以一定会有一条数据，不过第一条可能是null
        //如果客户统计表没数据，则取当前时间为统计表时间
        Date latestCustomerStats = customerStats.get(0) == null ? new Date() : customerStats.get(0).getDate();
        //判断结束时间是否晚于CustomerStats表中的已有数据最晚时间，如果更晚,则需要拉取已有数据最晚时间到结束时间的数据
        if (customerStatsDto.getEndDate().after(latestCustomerStats)) {
            //拉取开始时间到已有数据最早时间的数据
            customerStatsByDates(latestCustomerStats, customerStatsDto.getEndDate(), customerStatsDto.getFirmCode());
        }
    }

    /**
     * 循环客户统计对象，根据firmCode获取
     * 用于查询客户增量图表
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
    private void customerStatsByDate(Date date, String market, boolean update) {
        CustomerStats customerStats = DTOUtils.newDTO(CustomerStats.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        try {
            customerStats.setDate(sdf.parse(dateStr));
        } catch (ParseException e) {
        }
        if(StringUtils.isNotBlank(market)){
            customerStats.setFirmCode(market);
        }
        //是否存在指定日期的数据
        boolean contains = !super.listByExample(customerStats).isEmpty();
        Map<String, Object> map = new HashMap<>(2);
        map.put("date", date);
        if(StringUtils.isNotBlank(market)){
            map.put("market", market);
        }
        if(contains){
            //如果有数据，并且需要更新，则删了重新插入
            if(update) {
                super.deleteByExample(customerStats);
                getActualDao().customerStatsByDate(map);
            }//有数据，不需要更新
        }else {
            //无数据则直接插入
            getActualDao().customerStatsByDate(map);
        }
    }

    //初始化开始时间，默认查一个月
    private void initStartDate(CustomerStatsDto customerStats){
        if("true".equals(customerStats.aget("init"))){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            customerStats.setStartDate(calendar.getTime());
        }
    }

    /**
     * 按市场清除客户统计缓存
     * @param firmCode
     */
    @Override
    public void clearData(String firmCode){
        getActualDao().clearData(firmCode);
    }

    /**
     * 获取当前日期，时间为0
     * @return
     */
    private Date getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取指定日期，时间为0
     * @return
     */
    private Date getDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取指定日期，时间为0
     * @return
     */
    private Date getDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 设置默认查询条件
     * @param customerStatsDto
     */
    private void setDefaultDateCondition(CustomerStatsDto customerStatsDto){
        if(customerStatsDto.getStartDate() == null) {
            customerStatsDto.setStartDate(getDate("2018-01-01"));
        }
        if(customerStatsDto.getEndDate() == null) {
            customerStatsDto.setEndDate(getCurrentDate());
        }
    }
}