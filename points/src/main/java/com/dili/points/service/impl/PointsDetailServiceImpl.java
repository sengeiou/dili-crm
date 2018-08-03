package com.dili.points.service.impl;

import com.dili.points.component.AsyncTask;
import com.dili.points.dao.CustomerCategoryPointsMapper;
import com.dili.points.dao.CustomerFirmPointsMapper;
import com.dili.points.dao.PointsDetailMapper;
import com.dili.points.dao.PointsExceptionMapper;
import com.dili.points.domain.*;
import com.dili.points.domain.dto.CustomerCategoryPointsDTO;
import com.dili.points.domain.dto.CustomerFirmPointsDTO;
import com.dili.points.domain.dto.CustomerPointsDTO;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.rpc.SystemConfigRpc;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.points.service.CustomerPointsService;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-03-20 11:29:31.
 * @author wangguofeng
 * 
 */
@Service
public class PointsDetailServiceImpl extends BaseServiceImpl<PointsDetail, Long> implements PointsDetailService {
	private static final Logger logger=LoggerFactory.getLogger(PointsDetailServiceImpl.class);

	public PointsDetailMapper getActualDao() {
		return (PointsDetailMapper) getDao();
	}

	@Autowired
	CustomerPointsService customerPointsService;
	@Autowired CustomerRpc customerRpc;
	@Autowired PointsExceptionMapper pointsExceptionMapper; 
	@Autowired CustomerCategoryPointsMapper customerCategoryPointsMapper;
	@Autowired
	SystemConfigRpc systemConfigRpc;
	@Autowired
	AsyncTask asyncTask;

	@Autowired
	CustomerFirmPointsService customerFirmPointsService;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Optional<PointsDetailDTO> insert(PointsDetailDTO pointsDetail) {
		//10:采购,20:销售
		if("purchase".equals(pointsDetail.getCustomerType())) {
			pointsDetail.setBuyer(true);
		}else if("sale".equals(pointsDetail.getCustomerType())) {
			pointsDetail.setBuyer(false);
		}
		
		if (pointsDetail.getPoints() == null) {
			pointsDetail.setPoints(0);
		}
		
		//支出(如果是支出则为积分值设置为负数)
		if (pointsDetail.getInOut().equals(20)) {
			pointsDetail.setPoints(0 - Math.abs(pointsDetail.getPoints()));
		}
		//保存CustomerFirmPointsDTO之后将实际保存的积分通过actualPoints传递回来
		CustomerFirmPointsDTO dto=this.saveCustomerFirmPointsDTO(pointsDetail);
		logger.debug("成功保存CustomerFirmPointsDTO:"+dto);
		int actualPoints=dto.getActualPoints();
		
		//真实积分为0，保存为异常
		if(actualPoints==0) {
			this.savePointsException(pointsDetail);
			return Optional.empty();
		}
		pointsDetail.setActualPoints(actualPoints);
		CustomerPoints customerPoints = this.saveCustomerPointsDTO(pointsDetail);
		logger.debug("成功保存CustomerPoints:"+customerPoints);
		// 计算用户可用积分和总积分
		pointsDetail.setPoints(actualPoints);
		pointsDetail.setBalance(dto.getAvailable());
		super.insertSelective(pointsDetail);	
		return Optional.ofNullable(pointsDetail);

	}
	private PointsException savePointsException(PointsDetailDTO pointsDetail) {
		PointsException pointsException=DTOUtils.newDTO(PointsException.class);
		pointsException.setPoints(pointsDetail.getPoints());
		pointsException.setCertificateNumber(pointsDetail.getCertificateNumber());
		pointsException.setPoints(0);
		pointsException.setNeedRecover(0);
		pointsException.setFirmCode(pointsDetail.getFirmCode());
		pointsException.setGenerateWay(pointsDetail.getGenerateWay());
		pointsException.setCreatedId(pointsDetail.getCreatedId());
		pointsException.setSourceSystem(pointsDetail.getSourceSystem());
		this.pointsExceptionMapper.insertSelective(pointsException);
		return pointsException;
	}
	private CustomerFirmPointsDTO saveCustomerFirmPointsDTO(PointsDetailDTO pointsDetail) {
		CustomerFirmPointsDTO dto=DTOUtils.newDTO(CustomerFirmPointsDTO.class);
	
		dto.setBuyer(pointsDetail.isBuyer());
		dto.setPoints(pointsDetail.getPoints());
		dto.setTradingFirmCode(pointsDetail.getFirmCode());
		dto.setAvailable(0);
		dto.setCertificateNumber(pointsDetail.getCertificateNumber());
		dto.setCustomerId(pointsDetail.getCustomerId());
		dto.setSellerPoints(0);
		dto.setBuyerPoints(0);
		this.customerFirmPointsService.saveCustomerFirmPoints(dto);
		return dto;
	}
	private CustomerPointsDTO saveCustomerPointsDTO(PointsDetailDTO pointsDetail) {
		CustomerPointsDTO dto=DTOUtils.newDTO(CustomerPointsDTO.class);

		dto.setBuyer(pointsDetail.isBuyer());
		dto.setAvailable(0);
		dto.setCertificateNumber(pointsDetail.getCertificateNumber());
		dto.setCustomerId(pointsDetail.getCustomerId());
		dto.setSellerPoints(0);
		dto.setBuyerPoints(0);
		dto.setActualPoints(pointsDetail.getActualPoints());
		this.customerPointsService.saveCustomerPointsDTO(dto);
		return dto;
	}
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public int clear(String firmCode,String notes) {
		//查询在需要清除积分的市场的客户信息
		CustomerFirmPoints customerFirmPoints = DTOUtils.newDTO(CustomerFirmPoints.class);
		customerFirmPoints.setTradingFirmCode(firmCode);
		List<CustomerFirmPoints> firmPointsList = customerFirmPointsService.list(customerFirmPoints);
		//如果没有客户在此市场产生过积分，则直接返回
		if (CollectionUtils.isEmpty(firmPointsList)){
			return 1;
		}

		//转换出 证件号-积分 结构的map
		Map<String, Integer> map = firmPointsList.stream().collect(Collectors.toMap(CustomerFirmPoints::getCertificateNumber, CustomerFirmPoints::getAvailable));
		/**
		 * 更改客户积分表中的可用积分
		 */
		CustomerPointsDTO customerPoints = DTOUtils.newDTO(CustomerPointsDTO.class);
		customerPoints.setCertificateNumbers(Lists.newArrayList(map.keySet()));
		/**
		 * 转换客户积分信息，设置客户的可用余额
		 */
		List<CustomerPoints> customerPointsList = customerPointsService.list(customerPoints)
				.stream()
				.map(customerPoint -> {
					//客户的总可用余额为：当前余额-需要清零的值
					customerPoint.setAvailable(customerPoint.getAvailable() - map.get(customerPoint.getCertificateNumber()).intValue());
					return customerPoint;
				})
				.collect(Collectors.toList());
		customerPointsService.batchUpdate(customerPointsList);
		//删除客户积分市场信息
		Example example = new Example(CustomerPoints.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("certificateNumber",map.keySet());
		customerFirmPointsService.deleteByExample(example);
        asyncTask.generatePointsDetail(firmPointsList,notes);
		return 1;
	}

	public PointsDetail manullyGeneratePointsDetail(PointsDetail pointsDetail) {
		pointsDetail.setGenerateWay(50);// 50 手工调整
		this.insert(pointsDetail);
		return pointsDetail;
	}
	
    @Override
    public EasyuiPageOutput listEasyuiPageByExample(PointsDetailDTO pointsDetail,boolean useProvider,List<String>firmCodes) throws Exception {
    	
    	//如果用户数据权限集全为空，则返回空结果集(不再进行数据库查询)
    	if(firmCodes.isEmpty()) {
    		return new EasyuiPageOutput(0,Collections.emptyList()); 
    	}
    	pointsDetail.setFirmCodes(firmCodes);
	    EasyuiPageOutput easyuiPageOutput = super.listEasyuiPageByExample(pointsDetail, useProvider);
	    return easyuiPageOutput;
    }

}