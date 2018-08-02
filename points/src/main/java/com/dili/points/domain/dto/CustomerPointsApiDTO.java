package com.dili.points.domain.dto;

import com.dili.points.domain.CustomerPoints;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;
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