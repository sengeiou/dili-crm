package com.dili.dp.service;

import com.dili.dp.domain.CustomerFirmPoints;
import com.dili.dp.domain.dto.CustomerApiDTO;
import com.dili.dp.domain.dto.CustomerFirmPointsDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;

import java.util.Map;

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
    CustomerFirmPoints getByCustomerIdAndFirm(Long customerId, String firmCode);

    /**
     * 根据客户及市场，获取客户在此市场的积分信息
     * @param certificateNumber 客户证件号
     * @param firmCode 市场code
     * @return
     */
    CustomerFirmPoints getByCertificateNumberAndFirm(String certificateNumber, String firmCode);

    /**
     * 根据条件删除信息
     * @param example
     * @return
     */
    int deleteByExample(Object example);
    
    /**
     * 保存CustomerFirmPointsDTO信息
     * @param customerFirmPoints
     * @return
     */
    int saveCustomerFirmPoints(CustomerFirmPointsDTO customerFirmPoints);

}