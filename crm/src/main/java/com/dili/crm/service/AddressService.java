package com.dili.crm.service;

import com.dili.crm.domain.Address;
import com.dili.crm.domain.CustomerVisit;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-15 11:16:14.
 */
public interface AddressService extends BaseService<Address, Long> {

    /**
     * 新增地址信息
     * @param address
     * @return
     */
    BaseOutput insertSelectiveWithOutput(Address address) throws Exception;

    /**
     * 修改地址信息
     * @param address
     * @return
     */
    BaseOutput updateSelectiveWithOutput(Address address) throws Exception;
}