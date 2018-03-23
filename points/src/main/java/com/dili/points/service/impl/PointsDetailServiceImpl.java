package com.dili.points.service.impl;

import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.dao.PointsDetailMapper;
import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public int insert(PointsDetail pointsDetail) {
		CustomerPoints example = DTOUtils.newDTO(CustomerPoints.class);
		example.setCertificateNumber(pointsDetail.getCertificateNumber());
		// 如果用户积分不存在,则先插入用户积分
		CustomerPoints customerPoints = this.customerPointsMapper.select(example).stream().findFirst()
				.orElseGet(() -> {
					CustomerApiDTO customer=DTOUtils.newDTO(CustomerApiDTO.class);
					customer.setCertificateNumber(pointsDetail.getCertificateNumber());
					Customer c=customerRpc.list(customer).getData().stream().findFirst().orElseGet(null);
					if(c!=null) {
						CustomerPoints cp = DTOUtils.newDTO(CustomerPoints.class);
						cp.setAvailable(0);
						cp.setId(c.getId());
						cp.setCertificateNumber(pointsDetail.getCertificateNumber());
						cp.setCreated(new Date());
						cp.setFrozen(0);
						cp.setTotal(0);
						this.customerPointsMapper.insertExact(cp);
						return cp;
					}
					return null;
				});
		//无法找到客户信息
		if(customerPoints==null) {
			return 0;
		}
		Integer points = pointsDetail.getPoints();
		if (points == null) {
			pointsDetail.setPoints(0);
			points = 0;
		}
		if (points > 0) {
			pointsDetail.setInOut(10);// 收入
		} else {
			pointsDetail.setInOut(20);// 支出
		}

		// 计算用户可用积分和总积分
		customerPoints.setAvailable(customerPoints.getAvailable() + points);
//		// 如果可用积分小于0,则重设置为0
//		if (customerPoints.getAvailable() < 0) {
//			customerPoints.setAvailable(0);
//		}
		customerPoints.setTotal(customerPoints.getAvailable() + customerPoints.getFrozen());

		pointsDetail.setBalance(customerPoints.getTotal());
		pointsDetail.setId(System.currentTimeMillis());
		this.customerPointsMapper.updateByPrimaryKey(customerPoints);
//return 0;
		return super.insertSelective(pointsDetail);
	}

	public PointsDetail manullyGeneratePointsDetail(PointsDetail pointsDetail) {
		pointsDetail.setGenerateWay(50);// 50 手工调整
		this.insert(pointsDetail);
		return pointsDetail;
	}

}