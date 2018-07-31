package com.dili.points.component;

import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.domain.PointsDetail;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.dto.DTOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AsyncTask {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTask.class);

    @Autowired
    private PointsDetailService pointsDetailService;

    @Async
    public void generatePointsDetail(List<CustomerFirmPoints> firmPointsList, String notes) {
        List<PointsDetail> pointsDetails = firmPointsList.stream().map(customerPoints -> {
            PointsDetail detail = DTOUtils.newDTO(PointsDetail.class);
            detail.setNotes("积分清零:" + notes + "");
            detail.setGenerateWay(60);
            detail.setCreatedId(customerPoints.getId());
            detail.setCertificateNumber(customerPoints.getCertificateNumber());
            detail.setPoints(0);
            detail.setBalance(0);
            detail.setInOut(20);
            detail.setSourceSystem("points");
            detail.setFirmCode(customerPoints.getTradingFirmCode());
            return detail;
        }).collect(Collectors.toList());
        pointsDetailService.batchInsert(pointsDetails);
        logger.info("积分清零任务执行完毕");
    }
}
