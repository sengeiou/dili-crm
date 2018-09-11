package com.dili.dp.service;

import com.dili.dp.domain.Address;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-15 11:16:14.
 */
public interface AddressService extends BaseService<Address, Long> {

    /**
     * 获取逆地理编码
     * @param lat,lng
     * @return
     */
    BaseOutput locationReverse(String lat, String lng) throws Exception;
    /**
     * 获取地理编码
     * @param address
     * @return
     */
    BaseOutput locationAddress(String address) throws Exception;
}