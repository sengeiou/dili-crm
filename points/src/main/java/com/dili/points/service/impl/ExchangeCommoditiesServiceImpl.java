package com.dili.points.service.impl;

import com.dili.points.dao.ExchangeCommoditiesMapper;
import com.dili.points.domain.ExchangeCommodities;
import com.dili.points.service.ExchangeCommoditiesService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class ExchangeCommoditiesServiceImpl extends BaseServiceImpl<ExchangeCommodities, Long> implements ExchangeCommoditiesService {

    public ExchangeCommoditiesMapper getActualDao() {
        return (ExchangeCommoditiesMapper)getDao();
    }

    /**
     * 新增兑换商品
     *
     * @param exchangeCommodities
     * @return
     */
    @Override
    public BaseOutput insertSelectiveWithOutput(ExchangeCommodities exchangeCommodities) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        //新增时，可兑换量即等于总量
        exchangeCommodities.setAvailable(exchangeCommodities.getTotal());
        exchangeCommodities.setCreatedId(userTicket.getId());
        super.insertSelective(exchangeCommodities);
        return BaseOutput.success("新增成功").setData(exchangeCommodities);
    }

    /**
     * 更新兑换商品信息
     *
     * @param exchangeCommodities
     * @return
     */
    @Override
    public BaseOutput updateSelectiveWithOutput(ExchangeCommodities exchangeCommodities) {
        ExchangeCommodities old = get(exchangeCommodities.getId());
        Integer add = exchangeCommodities.getTotal()-old.getTotal();
        Integer newAvailable = old.getAvailable()+add;
        if (newAvailable<=0){
            exchangeCommodities.setAvailable(0);
        }else{
            exchangeCommodities.setAvailable(newAvailable);
        }
        super.updateSelective(exchangeCommodities);
        return BaseOutput.success("更新成功").setData(exchangeCommodities);
    }
}