package com.dili.points.service.impl;

import com.dili.points.dao.PointsExchangeRecordMapper;
import com.dili.points.domain.Customer;
import com.dili.points.domain.PointsExchangeRecord;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.service.PointsExchangeRecordService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class PointsExchangeRecordServiceImpl extends BaseServiceImpl<PointsExchangeRecord, Long> implements PointsExchangeRecordService {

    public PointsExchangeRecordMapper getActualDao() {
        return (PointsExchangeRecordMapper)getDao();
    }

    @Autowired
    private CustomerRpc customerRpc;

    /**
     * 用于支持like, order by 的easyui分页查询
     *
     * @param domain
     * @param useProvider
     * @return
     */
    @Override
    public EasyuiPageOutput listEasyuiPageByExample(PointsExchangeRecord domain, boolean useProvider) throws Exception {
        Customer c = DTOUtils.newDTO(Customer.class);
        c.setName("张");
        BaseOutput<List<Customer>> list = customerRpc.list(c);
        return super.listEasyuiPageByExample(domain, useProvider);
    }
}