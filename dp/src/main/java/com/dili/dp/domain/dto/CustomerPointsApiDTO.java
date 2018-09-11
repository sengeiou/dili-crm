package com.dili.dp.domain.dto;

import com.dili.dp.domain.CustomerPoints;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-03-20 11:29:30.
 */
@Table(name = "`customer_points`")
public interface CustomerPointsApiDTO extends CustomerPoints {
    @Id
    @Column(name = "`customer_id`")
    @Operator(Operator.IN)
    List<Long> getCustomerIds();
    void setCustomerIds(List<Long> customerIds);

}