package com.dili.points.service.impl;

import com.dili.points.dao.CustomerCategoryPointsMapper;
import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.dao.PointsDetailMapper;
import com.dili.points.dao.PointsExceptionMapper;
import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerCategoryPoints;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.PointsException;
import com.dili.points.domain.SystemConfig;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.CustomerCategoryPointsDTO;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.rpc.SystemConfigRpc;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.exception.BusinessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dili.sysadmin.sdk.session.SessionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class PointsDetailServiceImpl extends BaseServiceImpl<PointsDetail, Long> implements PointsDetailService {
	private static final Logger logger=LoggerFactory.getLogger(PointsDetailServiceImpl.class);

	public PointsDetailMapper getActualDao() {
		return (PointsDetailMapper) getDao();
	}

	@Autowired CustomerPointsMapper customerPointsMapper;
	@Autowired CustomerRpc customerRpc;
	@Autowired PointsExceptionMapper pointsExceptionMapper; 
	@Autowired CustomerCategoryPointsMapper customerCategoryPointsMapper;
	@Autowired
	SystemConfigRpc systemConfigRpc;
	@Transactional(propagation = Propagation.REQUIRED)
	public int batchInsertPointsDetailDTO(Map<PointsDetailDTO,List<CustomerCategoryPointsDTO>> pointsDetailMap) {
		for(Entry<PointsDetailDTO, List<CustomerCategoryPointsDTO>> entry:pointsDetailMap.entrySet()) {
			PointsDetailDTO pointsDetail=entry.getKey();
			
			Optional<PointsDetailDTO> pointsDetailDTO=this.insert(pointsDetail);
			pointsDetailDTO.ifPresent((p)->{
				//对总积分按照百分比(金额或者重量)进行分配
				List<CustomerCategoryPoints>categoryList=this.reCalculateCategoryPoints(p, entry.getValue());
				Map<Boolean,List<CustomerCategoryPoints>>groupedList=categoryList.stream().collect(Collectors.partitioningBy((c)->{return c.getId()!=null;}));
				List<CustomerCategoryPoints>updateList=groupedList.get(Boolean.TRUE);
				List<CustomerCategoryPoints>insertList=groupedList.get(Boolean.FALSE);
				if(!insertList.isEmpty()) {
					customerCategoryPointsMapper.insertList(insertList);
				}
				if(!updateList.isEmpty()) {
					updateList.stream().forEach((c)->{
						customerCategoryPointsMapper.updateByPrimaryKey(c);	
					});
					
				}
			});
		}
		return pointsDetailMap.size();
	}
	private List<CustomerCategoryPoints>reCalculateCategoryPoints(PointsDetailDTO pointsDetail,List<CustomerCategoryPointsDTO>categoryList){
		if(pointsDetail.getWeightType()==null) {
			return Collections.emptyList();
		}
		Integer totalPoints=pointsDetail.getPoints();
		if(totalPoints==null||totalPoints<0) {
			totalPoints=0;
		}
		
		//累加进行总金额总重量计算
		CustomerCategoryPointsDTO total=DTOUtils.newDTO(CustomerCategoryPointsDTO.class);
		total.setTotalMoney(0L);
		total.setWeight(BigDecimal.ZERO);
		total.setBuyerPoints(0);
		total.setSellerPoints(0);
		for(CustomerCategoryPointsDTO dto:categoryList) {
			total.setTotalMoney(total.getTotalMoney()+dto.getTotalMoney());
			total.setWeight(total.getWeight().add(dto.getWeight()));
		}
		logger.info("对总积分进行百分比分配: CertificateNumber {},CustomerType {},totalPoints: {}",pointsDetail.getCertificateNumber(),pointsDetail.getCustomerType(),totalPoints);
		List<CustomerCategoryPoints>resultList=new ArrayList<>();
		//将总积分按百分比分配
		for(CustomerCategoryPointsDTO dto:categoryList) {
			//// 交易量 10 交易额 20 商品 30 支付方式:40
			
			
//			Long category3Id=dto.getCategory3Id();
//			String certificateNumber=dto.getCertificateNumber();
//			
//			CustomerCategoryPoints condition=DTOUtils.newDTO(CustomerCategoryPoints.class);
//			condition.setCategory3Id(category3Id);
//			condition.setCertificateNumber(certificateNumber);
			
//			CustomerCategoryPoints result=this.customerCategoryPointsMapper.selectOne(condition);
//			if(result==null) {
//				result=dto;
//				result.setAvailable(0);
//				result.setBuyerPoints(0);
//				result.setSellerPoints(0);
//			}else {
//				result.setCategory1Id(dto.getCategory1Id());
//				result.setCategory1Name(dto.getCategory1Name());
//				result.setCategory2Id(dto.getCategory2Id());
//				result.setCategory2Name(dto.getCategory2Name());
//				result.setCategory3Id(dto.getCategory3Id());
//				result.setCategory3Name(dto.getCategory3Name());
//			}
//			
			BigDecimal percentage=BigDecimal.ZERO;
			if(pointsDetail.getWeightType().equals(10)) {
				percentage=dto.getWeight().divide(total.getWeight(),4,RoundingMode.HALF_EVEN);
			}else if(pointsDetail.getWeightType().equals(20)) {
				percentage=new BigDecimal(dto.getTotalMoney()).divide(total.getWeight(),4,RoundingMode.HALF_EVEN);
			}else {
				continue;
			}
			
			int points=percentage.multiply(new BigDecimal(totalPoints)).intValue();
			logger.info("CertificateNumber {},CustomerType {},Category1Id {},Category1Name {},Points: {}",pointsDetail.getCertificateNumber(),pointsDetail.getCustomerType(),dto.getCategory3Id(),dto.getCategory3Name(),points);
			//10:采购,20:销售
			if("purchase".equals(pointsDetail.getCustomerType())) {
				dto.setBuyerPoints(dto.getBuyerPoints()+points);
				total.setBuyerPoints(total.getBuyerPoints()+points);
			}else if("sale".equals(pointsDetail.getCustomerType())) {
				dto.setSellerPoints(dto.getSellerPoints()+points);
				total.setSellerPoints(total.getBuyerPoints()+points);
			}
			dto.setAvailable(dto.getAvailable()+points);
			logger.info("按百分比进行计算后的品类积分为:CertificateNumber {},CustomerType {},BuyerPoints {},SellerPoints {},Available: {}",pointsDetail.getCertificateNumber(),pointsDetail.getCustomerType(),dto.getBuyerPoints(),dto.getSellerPoints(),dto.getAvailable());
			resultList.add(dto);
		}
		
		//对积分进行修正(最后一个的积分等于总积分减去前面积分之和)
		if(!resultList.isEmpty()) {
			CustomerCategoryPoints lastCategoryPointsDTO=categoryList.get(categoryList.size()-1);
			if("purchase".equals(pointsDetail.getCustomerType())) {
				lastCategoryPointsDTO.setBuyerPoints(totalPoints-(total.getBuyerPoints()-lastCategoryPointsDTO.getBuyerPoints()));
			}else if("sale".equals(pointsDetail.getCustomerType())) {
				lastCategoryPointsDTO.setSellerPoints(totalPoints-(total.getSellerPoints()-lastCategoryPointsDTO.getSellerPoints()));
			}
			lastCategoryPointsDTO.setAvailable(lastCategoryPointsDTO.getBuyerPoints()+lastCategoryPointsDTO.getSellerPoints());
			logger.info("最后一条数据修:CertificateNumber {},CustomerType {},BuyerPoints{},SellerPoints{},Available: {}",pointsDetail.getCertificateNumber(),pointsDetail.getCustomerType(),lastCategoryPointsDTO.getBuyerPoints(),lastCategoryPointsDTO.getSellerPoints(),lastCategoryPointsDTO.getAvailable());
		}
		
		//将当前积分累加到已有的积分数据(如果存在)
		for(CustomerCategoryPoints dto:resultList) {
			
			CustomerCategoryPoints condition=DTOUtils.newDTO(CustomerCategoryPoints.class);
			condition.setCategory3Id(dto.getCategory3Id());
			condition.setCertificateNumber(dto.getCertificateNumber());
			CustomerCategoryPoints result=this.customerCategoryPointsMapper.selectOne(condition);
			if(result!=null) {
				dto.setBuyerPoints(dto.getBuyerPoints()+result.getBuyerPoints());
				dto.setSellerPoints(dto.getSellerPoints()+result.getSellerPoints());
				dto.setAvailable(dto.getAvailable()+result.getAvailable());
			}
			
		}
		return resultList;
	}
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
	public Optional<PointsDetailDTO> insert(PointsDetailDTO pointsDetail) { 
		if(pointsDetail.getException()!=null&&pointsDetail.getException().equals(1)) {
			//异常积分信息保存
			 this.insertPointsException(pointsDetail);
			 return Optional.empty();
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
		
		//10:采购,20:销售
		if("purchase".equals(pointsDetail.getCustomerType())) {
			customerPoints.setBuyerPoints((customerPoints.getBuyerPoints()==null?0:customerPoints.getBuyerPoints())+points);
		}else if("sale".equals(pointsDetail.getCustomerType())) {
			customerPoints.setSellerPoints((customerPoints.getSellerPoints()==null?0:customerPoints.getSellerPoints())+points);
		}
		
		if(points==0) {
			this.insertPointsException(pointsDetail);
		}else {
			// pointsDetail.setId(System.currentTimeMillis());
			this.customerPointsMapper.updateByPrimaryKey(customerPoints);
			// return 0;
			// pointsDetail.setId(null);
			super.insertSelective(pointsDetail);	
			return Optional.ofNullable(pointsDetail);
				
		}
		return Optional.empty();
		
		

	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
	public int clear(String notes) {
		PointsDetail detail = DTOUtils.newDTO(PointsDetail.class);
		detail.setNotes("积分清零:" + notes + "");
		detail.setGenerateWay(50);
		detail.setCreatedId(SessionContext.getSessionContext().getUserTicket().getId());
		this.insert(detail);

		CustomerPoints customerPoints = DTOUtils.newDTO(CustomerPoints.class);
		customerPoints.setAvailable(0);
		customerPoints.setFrozen(0);
		customerPoints.setTotal(0);
		customerPoints.setDayPoints(0);
		customerPoints.setBuyerPoints(0);
		customerPoints.setSellerPoints(0);

		Example example = new Example(CustomerPoints.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIsNotNull("id");
		customerPointsMapper.updateByExampleSelective(customerPoints, example);
		return 1;
	}

	public PointsDetail manullyGeneratePointsDetail(PointsDetail pointsDetail) {
		pointsDetail.setGenerateWay(50);// 50 手工调整
		this.insert(pointsDetail);
		return pointsDetail;
	}

}