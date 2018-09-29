package com.dili.crm.domain.dto;

import com.dili.crm.domain.CustomerStats;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.dto.IMybatisForceParams;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name = "`customer_stats`")
public interface CustomerStatsDto extends CustomerStats, IMybatisForceParams {

    @Column(name = "`date`")
    @FieldDef(label="开始日期")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getStartDate();

    void setStartDate(Date startDate);

    @Column(name = "`date`")
    @FieldDef(label="结束日期")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getEndDate();

    void setEndDate(Date endDate);

    @Column(name = "`firm_code`")
    @Operator(Operator.IN)
    List<String> getFirmCodes();
    void setFirmCodes(List<String> firmCodes);
}
