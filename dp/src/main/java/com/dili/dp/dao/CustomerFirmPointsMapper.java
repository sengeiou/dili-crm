package com.dili.dp.dao;

import com.dili.dp.domain.CustomerFirmPoints;
import com.dili.dp.domain.dto.CustomerApiDTO;
import com.dili.ss.base.MyMapper;

import java.util.List;
import java.util.Map;

public interface CustomerFirmPointsMapper extends MyMapper<CustomerFirmPoints> {

    /**
     * 按条件查询客户积分(关联客户表)
     * @param customerApiDTO
     * @return
     */
    List<Map> listByPage(CustomerApiDTO customerApiDTO);
}