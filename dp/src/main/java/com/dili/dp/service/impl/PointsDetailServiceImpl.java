package com.dili.dp.service.impl;

import com.dili.dp.dao.CustomerCategoryPointsMapper;
import com.dili.dp.dao.PointsDetailMapper;
import com.dili.dp.dao.PointsExceptionMapper;
import com.dili.dp.domain.CustomerPoints;
import com.dili.dp.domain.PointsDetail;
import com.dili.dp.domain.PointsException;
import com.dili.dp.domain.dto.CustomerFirmPointsDTO;
import com.dili.dp.domain.dto.CustomerPointsDTO;
import com.dili.dp.domain.dto.PointsDetailDTO;
import com.dili.dp.rpc.CustomerRpc;
import com.dili.dp.service.CustomerFirmPointsService;
import com.dili.dp.service.CustomerPointsService;
import com.dili.dp.service.PointsDetailService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.rpc.SystemConfigRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2018-03-20 11:29:31.
 *
 * @author wangguofeng
 */
@Service
public class PointsDetailServiceImpl extends BaseServiceImpl<PointsDetail, Long> implements PointsDetailService {
    private static final Logger logger = LoggerFactory.getLogger(PointsDetailServiceImpl.class);

    public PointsDetailMapper getActualDao() {
        return (PointsDetailMapper) getDao();
    }

    @Autowired
    CustomerPointsService customerPointsService;
    @Autowired
    CustomerRpc customerRpc;
    @Autowired
    PointsExceptionMapper pointsExceptionMapper;
    @Autowired
    CustomerCategoryPointsMapper customerCategoryPointsMapper;
    @Autowired
    SystemConfigRpc systemConfigRpc;

    @Autowired
    CustomerFirmPointsService customerFirmPointsService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Optional<PointsDetailDTO> insert(PointsDetailDTO pointsDetail) {
        //10:采购,20:销售
        if ("purchase".equals(pointsDetail.getCustomerType())) {
            pointsDetail.setBuyer(true);
        } else if ("sale".equals(pointsDetail.getCustomerType())) {
            pointsDetail.setBuyer(false);
        }

        if (pointsDetail.getPoints() == null) {
            pointsDetail.setPoints(0);
        }
        pointsDetail.setFirmCode(pointsDetail.getTradingFirmCode());
        //支出(如果是支出则为积分值设置为负数)
        if (pointsDetail.getInOut().equals(20)) {
            pointsDetail.setPoints(0 - Math.abs(pointsDetail.getPoints()));
        }
        //保存CustomerFirmPointsDTO之后将实际保存的积分通过actualPoints传递回来
        CustomerFirmPointsDTO dto = this.saveCustomerFirmPointsDTO(pointsDetail);
        logger.debug("成功保存CustomerFirmPointsDTO:" + dto);
        Integer actualPoints = dto.getActualPoints();

        //真实积分为0，保存为异常
        if (actualPoints == null || actualPoints == 0) {
            this.savePointsException(pointsDetail);
            return Optional.empty();
        }
        pointsDetail.setActualPoints(actualPoints);
        CustomerPoints customerPoints = this.saveCustomerPointsDTO(pointsDetail);
        logger.debug("成功保存CustomerPoints:" + customerPoints);
        // 计算用户可用积分和总积分
        pointsDetail.setPoints(actualPoints);
        pointsDetail.setBalance(dto.getAvailable());
        super.insertSelective(pointsDetail);
        return Optional.ofNullable(pointsDetail);

    }

    private PointsException savePointsException(PointsDetailDTO pointsDetail) {
        PointsException pointsException = DTOUtils.newDTO(PointsException.class);
        pointsException.setPoints(pointsDetail.getPoints());
        pointsException.setCertificateNumber(pointsDetail.getCertificateNumber());
        pointsException.setNeedRecover(0);
        pointsException.setFirmCode(pointsDetail.getFirmCode());
        pointsException.setGenerateWay(pointsDetail.getGenerateWay());
        pointsException.setCreatedId(pointsDetail.getCreatedId());
        pointsException.setSourceSystem(pointsDetail.getSourceSystem());
        this.pointsExceptionMapper.insertSelective(pointsException);
        return pointsException;
    }

    private CustomerFirmPointsDTO saveCustomerFirmPointsDTO(PointsDetailDTO pointsDetail) {
        CustomerFirmPointsDTO dto = DTOUtils.newDTO(CustomerFirmPointsDTO.class);
        dto.setBuyer(pointsDetail.isBuyer());
        dto.setPoints(pointsDetail.getPoints());
        dto.setTradingFirmCode(pointsDetail.getTradingFirmCode());
        dto.setCertificateNumber(pointsDetail.getCertificateNumber());
        dto.setCustomerId(pointsDetail.getCustomerId());
        dto.setSellerPoints(0);
        dto.setBuyerPoints(0);
        dto.setYn(1);
        dto.setFrozen(0);
        dto.setAdjust(true);
        dto.setModifiedId(pointsDetail.getCreatedId());
        this.customerFirmPointsService.saveCustomerFirmPoints(dto);
        return dto;
    }

    private CustomerPointsDTO saveCustomerPointsDTO(PointsDetailDTO pointsDetail) {
        CustomerPointsDTO dto = DTOUtils.newDTO(CustomerPointsDTO.class);
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



}