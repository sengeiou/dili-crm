package com.dili.points.service.impl;

import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.dao.ExchangeCommoditiesMapper;
import com.dili.points.dao.PointsExchangeRecordMapper;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.ExchangeCommodities;
import com.dili.points.domain.PointsExchangeRecord;
import com.dili.points.service.PointsExchangeRecordService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dao.CommonMapper;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CustomerPointsMapper customerPointsMapper;
    @Autowired
    private ExchangeCommoditiesMapper exchangeCommoditiesMapper;
    @Autowired
    private CommonMapper commonMapper;

    /**
     * 新增商品兑换记录
     *
     * @param pointsExchangeRecord 兑换记录信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput insertSelectiveWithOutput(PointsExchangeRecord pointsExchangeRecord) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("兑换失败，登录超时");
        }
        //查询客户的积分信息
        CustomerPoints customerPoints = customerPointsMapper.selectByPrimaryKey(pointsExchangeRecord.getCustomerId());
        if (null == customerPoints || customerPoints.getAvailable().intValue() < pointsExchangeRecord.getPoints().intValue()){
            return BaseOutput.failure("兑换失败，客户可用积分不足");
        }
        //查询可兑换的商品
        ExchangeCommodities commodities = exchangeCommoditiesMapper.selectByPrimaryKey(pointsExchangeRecord.getExchangeCommoditiesId());
        if (null == commodities || commodities.getAvailable().intValue() < pointsExchangeRecord.getQuantity().intValue()){
            return BaseOutput.failure("兑换失败，兑换商品不存在或数量不足");
        }
        //重新设置客户积分信息，并修改
        customerPoints.setAvailable(customerPoints.getAvailable()-pointsExchangeRecord.getPoints());
        Example example = new Example(CustomerPoints.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",customerPoints.getId());
        if (null != customerPoints.getModified()){
            criteria.andEqualTo("modified",customerPoints.getModified());
        }
        customerPoints.setModified(new Date());
        customerPoints.setModifiedId(userTicket.getId());
        int i= customerPointsMapper.updateByExample(customerPoints,example);
        if (i < 1) {
            return BaseOutput.failure("兑换失败，客户积分扣减失败，请重新操作");
        }
        //扣减商品的可兑换量
        commodities.setAvailable(commodities.getAvailable()-pointsExchangeRecord.getQuantity());
        Example commodityExample = new Example(ExchangeCommodities.class);
        Example.Criteria commodityCriteria = commodityExample.createCriteria();
        commodityCriteria.andEqualTo("id",commodities.getId());
        if (null != customerPoints.getModified()){
            commodityCriteria.andEqualTo("modified",commodities.getModified());
        }
        commodities.setModified(new Date());
        commodities.setModifiedId(userTicket.getId());
        i= exchangeCommoditiesMapper.updateByExample(commodities,commodityExample);
        if (i < 1) {
            throw new BusinessException("error","兑换失败，商品数扣减失败，请重新操作");
        }
        //保存客户兑换信息
        pointsExchangeRecord.setCreatedId(userTicket.getId());
        insertSelective(pointsExchangeRecord);
        return BaseOutput.success("兑换成功");
    }

    /**
     * 用于支持like, order by 的easyui分页查询
     *
     * @param domain
     * @param useProvider
     * @return
     */
    @Override
    public EasyuiPageOutput listEasyuiPageByExample(PointsExchangeRecord domain, boolean useProvider) throws Exception {
        EasyuiPageOutput easyuiPageOutput = super.listEasyuiPageByExample(domain, useProvider);
        //查询总使用积分和总兑换量
        List<Map> maps = commonMapper.selectMap("SELECT SUM(quantity) AS quantity,SUM(points) AS points FROM points_exchange_record");
        String points = "0";
        String quantity = "0";
        Map map = maps.get(0);
        if(!CollectionUtils.isEmpty(map)){
            points = map.get("points").toString();
            quantity = map.get("quantity").toString();
        }
        List<Map> footers = Lists.newArrayList();
        Map footer = new HashMap(1);
        footer.put("name", "总使用积分:");
        footer.put("certificateNumber", points);
        footer.put("organizationType","总兑换数量:");
        footer.put("certificateType",quantity);
        footers.add(footer);
        easyuiPageOutput.setFooter(footers);
        return easyuiPageOutput;
    }
}