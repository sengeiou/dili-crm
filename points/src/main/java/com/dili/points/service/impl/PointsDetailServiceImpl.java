package com.dili.points.service.impl;

import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.dao.PointsDetailMapper;
import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.PointsDetail;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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

	@Autowired
	CustomerPointsMapper customerPointsMapper;
	@Autowired
	CustomerRpc customerRpc;

	@Autowired
	SystemConfigRpc systemConfigRpc;

	@Transactional(propagation = Propagation.REQUIRED)
	public int batchInsertPointsDetailDTO(List<PointsDetailDTO> pointsDetail) {
		pointsDetail.forEach(p -> {
			this.insert(p);
		});
		return pointsDetail.size();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int insert(PointsDetailDTO pointsDetail) {
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
			cp.setFrozen(0);
			cp.setTotal(0);
			this.customerPointsMapper.insertExact(cp);
			return cp;
			// }
			// return null;
		});
		// 无法找到客户信息
		if (customerPoints == null) {
			return 0;
		}
		Integer points = pointsDetail.getPoints();
		if (points == null) {
			pointsDetail.setPoints(0);
			points = 0;
		}
		if (points >= 0) {
			pointsDetail.setInOut(10);// 收入
		} else {
			pointsDetail.setInOut(20);// 支出
		}
		BaseOutput<SystemConfig>output=this.systemConfigRpc.getByCode("customerPoints.day.limits");
		if(!output.isSuccess()||output.getData()==null) {
			throw new AppException("远程查询积分上限出错!");
		}
		int total = Integer.parseInt(output.getData().getValue());// 积分上限
		
		
		Integer dayPoints = customerPoints.getDayPoints();// 当天积分总和
		//修改时间
		 Instant instant = customerPoints.getModified().toInstant();
		 ZoneId defaultZoneId = ZoneId.systemDefault();
	        LocalDate modifiedDate = instant.atZone(defaultZoneId).toLocalDate();
		//当前时间
		LocalDate currentDate = LocalDate.now();
		
		
		if(!pointsDetail.getGenerateWay().equals(50)) {//50手工调整(对于手工调整,不做累加不做判断)
	
			// 如果上次修改积分的时间为今天时,进行积分上限处理
			if (modifiedDate.isEqual(currentDate)) {
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
				dayPoints = points;
			}
		}else {
			// 如果上次修改积分的时间不是今天时,初始化积分上限到0
			if (!modifiedDate.isEqual(currentDate)) {
				dayPoints = 0;
			} 
		}
		

		customerPoints.setModified(new Date());
		customerPoints.setDayPoints(dayPoints);

		// 计算用户可用积分和总积分
		customerPoints.setAvailable(customerPoints.getAvailable() + points);
		// // 如果可用积分小于0,则重设置为0
		// if (customerPoints.getAvailable() < 0) {
		// customerPoints.setAvailable(0);
		// }
		customerPoints.setTotal(customerPoints.getAvailable() + customerPoints.getFrozen());

		pointsDetail.setBalance(customerPoints.getTotal());
		// pointsDetail.setId(System.currentTimeMillis());
		this.customerPointsMapper.updateByPrimaryKey(customerPoints);
		// return 0;
		// pointsDetail.setId(null);
		return super.insertSelective(pointsDetail);
	}

	public PointsDetail manullyGeneratePointsDetail(PointsDetail pointsDetail) {
		pointsDetail.setGenerateWay(50);// 50 手工调整
		this.insert(pointsDetail);
		return pointsDetail;
	}

}