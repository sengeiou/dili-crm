package com.dili.points.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-08-06 10:33:13.
 */
@Table(name = "`customer_firm_points`")
public interface CustomerFirmPoints extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`customer_id`")
    @FieldDef(label="客户id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`certificate_number`")
    @FieldDef(label="证件号码", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`yn`")
    @FieldDef(label="是否可用")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getYn();

    void setYn(Integer yn);

    @Column(name = "`trading_firm_code`")
    @FieldDef(label="交易市场编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getTradingFirmCode();

    void setTradingFirmCode(String tradingFirmCode);

    @Column(name = "`available`")
    @FieldDef(label="可用积分")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getAvailable();

    void setAvailable(Integer available);

    @Column(name = "`frozen`")
    @FieldDef(label="冻结积分")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getFrozen();

    void setFrozen(Integer frozen);

    @Column(name = "`total`")
    @FieldDef(label="总积分")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getTotal();

    void setTotal(Integer total);

    @Column(name = "`modified_id`")
    @FieldDef(label="修改人")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getModifiedId();

    void setModifiedId(Long modifiedId);

    @Column(name = "`modified`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`buyer_points`")
    @FieldDef(label="购买积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBuyerPoints();

    void setBuyerPoints(Integer buyerPoints);

    @Column(name = "`day_points`")
    @FieldDef(label="当日积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getDayPoints();

    void setDayPoints(Integer dayPoints);

    @Column(name = "`reset_time`")
    @FieldDef(label="重置时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getResetTime();

    void setResetTime(Date resetTime);

    @Column(name = "`seller_points`")
    @FieldDef(label="销售积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSellerPoints();

    void setSellerPoints(Integer sellerPoints);
}