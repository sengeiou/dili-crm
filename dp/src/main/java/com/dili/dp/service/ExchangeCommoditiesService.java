package com.dili.dp.service;

import com.dili.dp.domain.ExchangeCommodities;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 兑换商品相关的服务
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
public interface ExchangeCommoditiesService extends BaseService<ExchangeCommodities, Long> {

    /**
     * 新增兑换商品
     *
     * @param exchangeCommodities
     * @return
     */
    BaseOutput insertSelectiveWithOutput(ExchangeCommodities exchangeCommodities);

    /**
     * 更新兑换商品信息
     * @param exchangeCommodities
     * @return
     */
    BaseOutput updateSelectiveWithOutput(ExchangeCommodities exchangeCommodities);
}