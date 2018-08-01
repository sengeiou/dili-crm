package com.dili.points.service.impl;

import com.dili.points.dao.CustomerFirmPointsMapper;
import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-07-30 16:20:03.
 */
@Service
public class CustomerFirmPointsServiceImpl extends BaseServiceImpl<CustomerFirmPoints, Long> implements CustomerFirmPointsService {

    public CustomerFirmPointsMapper getActualDao() {
        return (CustomerFirmPointsMapper)getDao();
    }

    @Override
    public CustomerFirmPoints getByCustomerAndFirm(Long customerId, String firmCode) {
        CustomerFirmPoints query = DTOUtils.newDTO(CustomerFirmPoints.class);
        query.setTradingFirmCode(firmCode);
        query.setCustomerId(customerId);
        CustomerFirmPoints customerPoints = getActualDao().select(query).stream().findFirst().orElseGet(() -> {
            CustomerFirmPoints temp = DTOUtils.newDTO(CustomerFirmPoints.class);
            temp.setAvailable(0L);
            temp.setSellerPoints(0);
            temp.setBuyerPoints(0);
            temp.setCustomerId(customerId);
            temp.setTradingFirmCode(firmCode);
            return temp;
        });
        return customerPoints;
    }

    @Override
    public Integer deleteByExample(Object example) {
        return getActualDao().deleteByExample(example);
    }
}