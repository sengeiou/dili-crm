package com.dili.points.service;

import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.domain.dto.CustomerFirmPointsDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-07-30 16:20:03.
 */
public interface CustomerFirmPointsService extends BaseService<CustomerFirmPoints, Long> {
    /**
     * 根据客户及市场，获取客户在此市场的积分信息
     * @param customerId 客户ID
     * @param firmCode 市场code
     * @return
     */
    CustomerFirmPoints getByCustomerAndFirm(Long customerId,String firmCode);

    /**
     * 根据条件删除信息
     * @param example
     * @return
     */
    Integer deleteByExample(Object example);
    
    /**
     * 保存CustomerFirmPointsDTO信息
     * @param customerFirmPoints
     * @return
     */
    public int saveCustomerFirmPoints(CustomerFirmPointsDTO customerFirmPoints);
	 
}