package com.dili.points.component;

import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.PointsDetail;
import com.dili.points.service.CustomerPointsService;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.dto.DTOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsyncTask {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTask.class);

    @Autowired
    private CustomerPointsService customerPointsService;

    @Autowired
    private PointsDetailService pointsDetailService;

    @Async
    public void generatePointsDetail(String notes) {
        List<CustomerPoints> list = customerPointsService.list(DTOUtils.newDTO(CustomerPoints.class));
        list.forEach(customerPoints -> {
            PointsDetail detail = DTOUtils.newDTO(PointsDetail.class);
            detail.setNotes("积分清零:" + notes + "");
            detail.setGenerateWay(60);
            detail.setCreatedId(customerPoints.getId());
            detail.setCertificateNumber(customerPoints.getCertificateNumber());
            detail.setPoints(0);
            detail.setBalance(0);
            detail.setInOut(20);
            detail.setSourceSystem("points");
            pointsDetailService.insert(detail);
        });

        logger.info("积分清零任务执行完毕");
    }
}
