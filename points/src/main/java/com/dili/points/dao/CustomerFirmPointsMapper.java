package com.dili.points.dao;

import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
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