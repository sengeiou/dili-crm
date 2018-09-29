package com.dili.crm.dao;

import com.dili.crm.domain.CustomerStats;
import com.dili.ss.base.MyMapper;

import java.util.Date;

public interface CustomerStatsMapper extends MyMapper<CustomerStats> {

    /**
     * 插入当日客户数到客户统计表
     */
    void customerStats();

    /**
     * 插入指定日期客户数到客户统计表
     */
    void customerStatsByDate(Date date);
}