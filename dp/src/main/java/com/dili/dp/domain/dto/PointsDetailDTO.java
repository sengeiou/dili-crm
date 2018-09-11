package com.dili.dp.domain.dto;

import com.dili.dp.domain.PointsDetail;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Table(name = "`points_detail`")
public interface PointsDetailDTO extends PointsDetail {
    //客户ID
    @Transient
    Long getCustomerId();

    void setCustomerId(Long customerId);

    //是否需要恢复(0否,1是)
    @Transient
    Integer getNeedRecover();

    void setNeedRecover(Integer needRecover);

    //是否异常(0否,1是)
    @Transient
    Integer getException();

    void setException(Integer exception);


    @Column(name = "`created`")
    @FieldDef(label = "查询条件-开始时间")
    @EditMode(editor = FieldEditor.Datetime)
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getCreatedStart();

    void setCreatedStart(Date createdStart);

    @Column(name = "`created`")
    @FieldDef(label = "查询条件-结束时间")
    @EditMode(editor = FieldEditor.Datetime)
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getCreatedEnd();

    void setCreatedEnd(Date createdEnd);


    //计算权限的类型(交易量 10 交易额 20 商品 30 支付方式:40)
    @Transient
    Integer getWeightType();

    void setWeightType(Integer weightType);

    @Operator(Operator.IN)
    @Column(name = "`firm_code`")
    List<String> getFirmCodes();

    void setFirmCodes(List<String> firmCodes);

    String getTradingFirmCode();
    void setTradingFirmCode(String tradingFirmCode);

    @Transient
    boolean isBuyer();

    void setBuyer(boolean isBuyer);

    @Transient
    Integer getActualPoints();

    void setActualPoints(Integer actualPoints);


}
