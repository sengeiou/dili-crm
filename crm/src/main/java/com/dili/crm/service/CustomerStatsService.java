package com.dili.crm.service;

import com.dili.crm.domain.CustomerStats;
import com.dili.crm.domain.dto.CustomerStatsDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-09-26 11:58:22.
 */
public interface CustomerStatsService extends BaseService<CustomerStats, Long> {

    /**
     * 统计当天各市场客户数
     */
    void customerStats();

    /**
     * 查询客户统计
     * @param customerStatsDto
     * @return
     * @throws Exception
     */
    BaseOutput<List<Map>> listCustomerStats(CustomerStatsDto customerStatsDto) throws Exception;

    /**
     * 查询客户增量统计
     * @param customerStatsDto
     * @return
     * @throws Exception
     */
    BaseOutput<List<Map>> listCustomerStatsIncrement(CustomerStatsDto customerStatsDto) throws Exception;
    /**
     * 插入指定日期客户数到客户统计表
     */
    void customerStatsByDate(Date date);

    /**
     * 统计时间范围内所有市场的客户数
     * @param startDate
     * @param endDate
     * @param firmCode 拉取的市场，所有市场为null
     */
    void customerStatsByDates(Date startDate, Date endDate, String firmCode);

    /**
     * 拉取数据
     * @param customerStatsDto
     */
    BaseOutput pullData(CustomerStatsDto customerStatsDto);

    /**
     * 强制拉取指定市场在某时间段内的数据
     * @param startDate
     * @param endDate
     * @param firmCodes
     */
    void pullCustomerStatsByMarkets(Date startDate, Date endDate, List<String> firmCodes);
    /**
     * 根据市场和日期修改客户数增量
     * @param customerStats
     */
    void updateCustomerCount(CustomerStats customerStats);
}