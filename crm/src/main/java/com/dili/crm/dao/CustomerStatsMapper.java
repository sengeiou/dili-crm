package com.dili.crm.dao;

import com.dili.crm.domain.CustomerStats;
import com.dili.ss.base.MyMapper;

import java.util.List;
import java.util.Map;

public interface CustomerStatsMapper extends MyMapper<CustomerStats> {

    /**
     * 插入当日客户数到客户统计表
     */
    void customerStats();

    /**
     * 插入指定日期客户数到客户统计表
     * @param map date指定日期(必填), market市场编码(选填)
     *
     */
    void customerStatsByDate(Map<String, Object> map);

    /**
     * 查询不同的市场类型
     * @return
     */
    List<CustomerStats> selectDistinctFirmCode();

    /**
     * 根据市场和日期修改客户数增量
     * @param customerStats
     */
    void updateCustomerCount(CustomerStats customerStats);

    /**
     * 按市场清除客户统计缓存
     * @param firmCode
     */
    void clearData(String firmCode);
}