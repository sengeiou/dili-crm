package com.dili.points.service.impl;

import com.dili.points.dao.CustomerFirmPointsMapper;
import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.domain.dto.CustomerFirmPointsDTO;
import com.dili.points.rpc.DataDictionaryRpc;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.uap.sdk.domain.DataDictionaryValue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-07-30 16:20:03.
 */
@Service
public class CustomerFirmPointsServiceImpl extends BaseServiceImpl<CustomerFirmPoints, Long> implements CustomerFirmPointsService {
	@Autowired DataDictionaryRpc dataDictionaryRpc;
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
            temp.setAvailable(0);
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
	/**
	 * 保存数据
	 * @param dto
	 * @return
	 */
	
	  public int saveCustomerFirmPoints(CustomerFirmPointsDTO dto) {
		if(StringUtils.isBlank(dto.getCertificateNumber()) || dto.getCustomerId()==null) {
			return 0;
		}
		if(StringUtils.isBlank(dto.getTradingFirmCode())) {
			return 0;
		}
		//数据设置默认值
		if(dto.getPoints()==null) {
			dto.setPoints(0);
		}
 		if(dto.getBuyerPoints()==null) {
 			dto.setBuyerPoints(0);
		}
 		if(dto.getSellerPoints()==null) {
 			dto.setSellerPoints(0);
		}
 		
 		
    	
    	CustomerFirmPoints example = DTOUtils.newDTO(CustomerFirmPoints.class);
    	example.setCustomerId(dto.getCustomerId());
    	example.setTradingFirmCode(dto.getTradingFirmCode());
    	
    	//查询
    	CustomerFirmPoints item=this.list(example).stream().findFirst().orElseGet(()->{
			CustomerFirmPoints customerFirmPoints = DTOUtils.clone(dto, CustomerFirmPoints.class);
			customerFirmPoints.setId(null);
			customerFirmPoints.setDayPoints(0);
			customerFirmPoints.setResetTime(new Date());
			return customerFirmPoints;
    		
    	});
    	
		// 积分上限
		int total = this.findDailyLimit(dto.getTradingFirmCode());
		// 已有当天积分总和
		Integer dayPoints = item.getDayPoints();
		//修改时间
		 LocalDate resetDate = Optional.ofNullable(item.getResetTime().toInstant()).orElseGet(()->{
			TemporalAmount tm=Duration.ofDays(1);
			return Instant.now().minus(tm); 
		}).atZone(ZoneId.systemDefault()).toLocalDate();
		 
		// 本次可积分
		Integer points = dto.getPoints();
		// 本次实际积分
		Integer actualPoints = 0;
		//在积分值大于零的时候进行上限判断是否超过上限
	     if(points>0) {
	    	//当前时间
			 LocalDate currentDate = LocalDate.now();
			// 如果上次累积积分的时间为今天时,进行积分上限处理
			if (resetDate.isEqual(currentDate)) {
				// 如果当天积分总和已经超过上限,当前积分详情的积分归为0,当天积分总和分为total
				if (dayPoints >= total) {
					actualPoints = 0;
					dayPoints = total;
				} else {
					// 如果积分上限与当天积分总和差值小于当前积分详情积分,则重新计算当前积分,并且设置当天积分总和分为total
					if ((total - dayPoints) <= points) {
						actualPoints = total - dayPoints;
						dayPoints = total;
					} else {// 直接对当天积分总和累加
						actualPoints=points;
						dayPoints = dayPoints + points;
					}
				}
			} else {
				//单次积分超过最大值
				if(total<points) {
					actualPoints=total;
					dayPoints=total;
				}else {
					actualPoints=points;
					dayPoints = points;	
				}
			}
	     }else {
	    	 //针对扣分的情况
	    	 actualPoints=points;
	     }

		
		 
		if(dto.isBuyer()) {
			item.setBuyerPoints(item.getBuyerPoints()+actualPoints);
		}else {
			item.setSellerPoints(item.getSellerPoints()+actualPoints);
		}
		item.setResetTime(new Date());
		item.setDayPoints(dayPoints);
		item.setAvailable(item.getBuyerPoints()+item.getSellerPoints());
		//将实际积分和余额通过dto传回去
		dto.setActualPoints(actualPoints);
		dto.setAvailable(item.getAvailable());
		if(item.getId()==null) {
			return this.getActualDao().insertExact(item);
		}else {
			return this.updateExact(item);
		}
    	
    }
	  /**
	   * 查询指定市场的每天积分上限值
	   * @param firmCode
	   * @return
	   */
	  private Integer findDailyLimit(String firmCode) {

		DataDictionaryValue condtion = DTOUtils.newDTO(DataDictionaryValue.class);
		condtion.setDdCode("customerPoints.day.limits");
		condtion.setName(firmCode);

		BaseOutput<List<DataDictionaryValue>> output = this.dataDictionaryRpc.list(condtion);
		if (!output.isSuccess() || output.getData() == null || output.getData().size() != 1) {
			throw new AppException("远程查询积分上限出错!");
		}
		String code = output.getData().get(0).getCode();
		int limits = Integer.parseInt(code);
		return limits;
	  }
}