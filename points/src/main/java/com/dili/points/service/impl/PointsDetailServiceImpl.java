package com.dili.points.service.impl;

import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.dao.PointsDetailMapper;
import com.dili.points.dao.PointsExceptionMapper;
import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.PointsException;
import com.dili.points.domain.SystemConfig;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.rpc.SystemConfigRpc;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.exception.BusinessException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class PointsDetailServiceImpl extends BaseServiceImpl<PointsDetail, Long> implements PointsDetailService {

	public PointsDetailMapper getActualDao() {
		return (PointsDetailMapper) getDao();
	}

	@Autowired CustomerPointsMapper customerPointsMapper;
	@Autowired CustomerRpc customerRpc;
	@Autowired PointsExceptionMapper pointsExceptionMapper; 
	@Autowired
	SystemConfigRpc systemConfigRpc;

	@Transactional(propagation = Propagation.REQUIRED)
	public int batchInsertPointsDetailDTO(List<PointsDetailDTO> pointsDetail) {
		pointsDetail.stream().forEach(p->{
			this.insert(p);//insert error pointsdetails
		});
		return pointsDetail.size();
	}
	//@Transactional(propagation = Propagation.REQUIRED)
	private int insertPointsException(PointsDetailDTO pointsDetail) {
		//转换对象,并进行保存
		PointsException exceptionalPoints=DTOUtils.as(pointsDetail, PointsException.class);
		if(exceptionalPoints.getNeedRecover()==null) {
			exceptionalPoints.setNeedRecover(0);
		}
		return pointsExceptionMapper.insertExact(exceptionalPoints);
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public int insert(PointsDetailDTO pointsDetail) { 
		if(pointsDetail.getException()!=null&&pointsDetail.getException().equals(1)) {
			//异常积分信息保存
			return this.insertPointsException(pointsDetail);
		}
		Long customerId = pointsDetail.getCustomerId();
		CustomerPoints example = DTOUtils.newDTO(CustomerPoints.class);
		example.setCertificateNumber(pointsDetail.getCertificateNumber());
		// 如果用户积分不存在,则先插入用户积分
		CustomerPoints customerPoints = this.customerPointsMapper.select(example).stream().findFirst().orElseGet(() -> {
			// CustomerApiDTO customer=DTOUtils.newDTO(CustomerApiDTO.class);
			// customer.setCertificateNumber(pointsDetail.getCertificateNumber());
			// Customer
			// c=customerRpc.list(customer).getData().stream().findFirst().orElseGet(null);
			// if(c!=null) {
			CustomerPoints cp = DTOUtils.newDTO(CustomerPoints.class);
			cp.setAvailable(0);
			cp.setId(customerId);
			cp.setCertificateNumber(pointsDetail.getCertificateNumber());
			cp.setCreated(new Date());
			cp.setModified(new Date());
			cp.setResetTime(new Date());
			cp.setDayPoints(0);
			cp.setFrozen(0);
			cp.setTotal(0);
			cp.setBuyerPoints(0);
			cp.setSellerPoints(0);
			cp.setYn(1);
			this.customerPointsMapper.insertExact(cp);
			return cp;
			// }
			// return null;
		});
		
		Integer points = pointsDetail.getPoints();
		if (points == null) {
			pointsDetail.setPoints(0);
			points = 0;
		}
		if (pointsDetail.getInOut().equals(10)) {//收入
			points=Math.abs(points);
		} else {//支出
			points=0-Math.abs(points);
		}
		BaseOutput<SystemConfig>output=this.systemConfigRpc.getByCode("customerPoints.day.limits");
		if(!output.isSuccess()||output.getData()==null) {
			throw new AppException("远程查询积分上限出错!");
		}
		int total = Integer.parseInt(output.getData().getValue());// 积分上限
		
		
		Integer dayPoints = customerPoints.getDayPoints();// 当天积分总和
		if(dayPoints==null) {
			dayPoints=0;
		}
		//修改时间
		Instant instant=Instant.now();
		if(customerPoints.getResetTime()!=null) {
			instant = customerPoints.getResetTime().toInstant();
		}else {
			//如果RestTime为空则把当前时间减少一天,做为resttime
			TemporalAmount tm=Duration.ofDays(1);
			instant =instant.minus(tm); 
		}
		 
		
		 ZoneId defaultZoneId = ZoneId.systemDefault();
	     LocalDate resetDate = instant.atZone(defaultZoneId).toLocalDate();
		//当前时间
		LocalDate currentDate = LocalDate.now();
		
		
		if(!pointsDetail.getGenerateWay().equals(50)&&points>0) {//50手工调整(对于手工调整,不做累加不做判断)
	
			// 如果上次累积积分的时间为今天时,进行积分上限处理
			if (resetDate.isEqual(currentDate)) {
				// 如果当天积分总和已经超过上限,当前积分详情的积分归为0,当天积分总和分为total
				if (dayPoints >= total) {
					points = 0;
					dayPoints = total;
				} else {
					// 如果积分上限与当天积分总和差值小于当前积分详情积分,则重新计算当前积分,并且设置当天积分总和分为total
					if ((total - dayPoints) <= points) {
						points = total - dayPoints;
						dayPoints = total;
					} else {// 直接对当天积分总和累加
						dayPoints = dayPoints + points;
					}
				}
			} else {
				//单次积分超过最大值
				if(total<points) {
					points=total;
					dayPoints=total;
				}else {
					dayPoints = points;	
				}
				
			}
		}else {
			// 如果上次修改积分的时间不是今天时,初始化积分上限到0
			if (!resetDate.isEqual(currentDate)) {
				dayPoints = 0;
			} 
		}

		
		customerPoints.setResetTime(new Date());
		customerPoints.setModified(new Date());
		customerPoints.setDayPoints(dayPoints);

		

		// 计算用户可用积分和总积分
		customerPoints.setAvailable(customerPoints.getAvailable() + points);
		// // 如果可用积分小于0,则重设置为0
		// if (customerPoints.getAvailable() < 0) {
		// customerPoints.setAvailable(0);
		// }
		customerPoints.setTotal(customerPoints.getAvailable() + customerPoints.getFrozen());
		pointsDetail.setPoints(points);
		pointsDetail.setBalance(customerPoints.getTotal());
		
		//1:采购,2:销售
		if("1".equals(pointsDetail.getCustomerType())) {
			customerPoints.setBuyerPoints((customerPoints.getBuyerPoints()==null?0:customerPoints.getBuyerPoints())+points);
		}else if("2".equals(pointsDetail.getCustomerType())) {
			customerPoints.setSellerPoints((customerPoints.getSellerPoints()==null?0:customerPoints.getSellerPoints())+points);
		}
		
		if(points==0) {
			return this.insertPointsException(pointsDetail);
		}else {
			// pointsDetail.setId(System.currentTimeMillis());
			this.customerPointsMapper.updateByPrimaryKey(customerPoints);
			// return 0;
			// pointsDetail.setId(null);
			return super.insertSelective(pointsDetail);		
				
		}
		
		

	}

	public PointsDetail manullyGeneratePointsDetail(PointsDetail pointsDetail) {
		pointsDetail.setGenerateWay(50);// 50 手工调整
		this.insert(pointsDetail);
		return pointsDetail;
	}

}