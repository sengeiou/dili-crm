package com.dili.points.service.impl;

import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.dao.ExchangeCommoditiesMapper;
import com.dili.points.dao.PointsDetailMapper;
import com.dili.points.dao.PointsExchangeRecordMapper;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.ExchangeCommodities;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.PointsExchangeRecord;
import com.dili.points.domain.dto.PointsExchangeRecordDTO;
import com.dili.points.domain.dto.PointsRuleDTO;
import com.dili.points.service.PointsExchangeRecordService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 * @author wangguofeng
 */
@Service
public class PointsExchangeRecordServiceImpl extends BaseServiceImpl<PointsExchangeRecord, Long> implements PointsExchangeRecordService {

    public PointsExchangeRecordMapper getActualDao() {
        return (PointsExchangeRecordMapper) getDao();
    }

    @Autowired
    private CustomerPointsMapper customerPointsMapper;
    @Autowired
    private ExchangeCommoditiesMapper exchangeCommoditiesMapper;
    @Autowired
    private PointsDetailMapper pointsDetailMapper;

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
        if (userTicket == null) {
            return BaseOutput.failure("兑换失败，登录超时");
        }
        CustomerPoints query = DTOUtils.newDTO(CustomerPoints.class);
        query.setId(pointsExchangeRecord.getCustomerId());
        query.setYn(1);
        //查询客户的积分信息
        List<CustomerPoints> select = customerPointsMapper.select(query);
        CustomerPoints customerPoints = null;
        if (!CollectionUtils.isEmpty(select)){
            customerPoints = select.get(0);
        }
        if (null != customerPoints && customerPoints.getAvailable().intValue() < pointsExchangeRecord.getPoints().intValue()) {
            return BaseOutput.failure("兑换失败，客户不存在或可用积分不足");
        }
        //查询可兑换的商品
        ExchangeCommodities commodities = exchangeCommoditiesMapper.selectByPrimaryKey(pointsExchangeRecord.getExchangeCommoditiesId());
        if (null == commodities || commodities.getAvailable().intValue() < pointsExchangeRecord.getQuantity().intValue()) {
            return BaseOutput.failure("兑换失败，兑换商品不存在或数量不足");
        }
        int i = 0;
        if (null != customerPoints) {
            //重新设置客户可用积分信息，并修改
            customerPoints.setAvailable(customerPoints.getAvailable() - pointsExchangeRecord.getPoints());
            customerPoints.setTotal(customerPoints.getTotal()-pointsExchangeRecord.getPoints());
            Example example = new Example(CustomerPoints.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id", customerPoints.getId());
            if (null != customerPoints.getModified()) {
                criteria.andEqualTo("modified", customerPoints.getModified());
            }
            customerPoints.setModified(new Date());
            customerPoints.setModifiedId(userTicket.getId());
            i = customerPointsMapper.updateByExample(customerPoints, example);
            if (i < 1) {
                return BaseOutput.failure("兑换失败，客户积分扣减失败，请重新操作");
            }
        }
        //扣减商品的可兑换量
        commodities.setAvailable(commodities.getAvailable() - pointsExchangeRecord.getQuantity());
        Example commodityExample = new Example(ExchangeCommodities.class);
        Example.Criteria commodityCriteria = commodityExample.createCriteria();
        commodityCriteria.andEqualTo("id", commodities.getId());
        if (null != commodities.getModified()) {
            commodityCriteria.andEqualTo("modified", commodities.getModified());
        }
        commodities.setModified(new Date());
        commodities.setModifiedId(userTicket.getId());
        i = exchangeCommoditiesMapper.updateByExample(commodities, commodityExample);
        if (i < 1) {
            throw new BusinessException("error", "兑换失败，商品数扣减失败，请重新操作");
        }
        //客户积分明细表中，插入本次的兑换记录
        PointsDetail pd = DTOUtils.newDTO(PointsDetail.class);
        pd.setCertificateNumber(pointsExchangeRecord.getCertificateNumber());
        pd.setPoints(0 - pointsExchangeRecord.getPoints());
        pd.setBalance(0);
        if (null != customerPoints){
            pd.setBalance(customerPoints.getAvailable());
        }
        //积分兑换
        pd.setGenerateWay(30);
        //支出
        pd.setInOut(20);
        //来源系统：积分系统
        pd.setSourceSystem("points");
        pd.setNotes("兑换" + commodities.getName());
        pd.setCreatedId(userTicket.getId());
        //保存客户积分明细信息
        pointsDetailMapper.insertSelective(pd);
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
        Map<Object, BigDecimal> statistics = getActualDao().statistics(domain);
        BigDecimal points = new BigDecimal(0);
        BigDecimal quantity = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(statistics)) {
            points = statistics.get("points");
            quantity = statistics.get("quantity");
        }
        List<Map> footers = this.buildFooter(points, quantity);
        easyuiPageOutput.setFooter(footers);
        return easyuiPageOutput;
    }

	
    @Override
    public EasyuiPageOutput listEasyuiPageByExample(PointsExchangeRecordDTO pointsExchangeRecordDto,boolean useProvider,List<String>firmCodes) throws Exception {
    	
    	//如果用户数据权限集全为空，则返回空结果集(不再进行数据库查询)
    	if(firmCodes.isEmpty()) {
    		EasyuiPageOutput easyuiPageOutput = new EasyuiPageOutput(0,Collections.emptyList()); 
    		List<Map> footers = this.buildFooter(BigDecimal.ZERO, BigDecimal.ZERO);
    		easyuiPageOutput.setFooter(footers);
    	    return easyuiPageOutput;
    	}
    	pointsExchangeRecordDto.setFirmCodes(firmCodes);
	    EasyuiPageOutput easyuiPageOutput = this.listEasyuiPageByExample(pointsExchangeRecordDto, useProvider);
	    return easyuiPageOutput;
    }
    
    /**
     * 构造footer数据
     * @param points 积分总数
     * @param quantity 兑换总数
     * @return
     */
    private List<Map> buildFooter(BigDecimal points, BigDecimal quantity) {
		List<Map> footers = Lists.newArrayList();
        Map footer = new HashMap(1);
        footer.put("name", "总使用积分:");
        footer.put("organizationType", points);
        footer.put("certificateType", "总兑换数量:");
        footer.put("certificateNumber", quantity);
        footers.add(footer);
		return footers;
	}
}