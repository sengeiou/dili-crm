package com.dili.points.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.validator.AddView;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-07-30 16:20:03.
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
    @NotNull(message = "客户ID不能为空")
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`certificate_number`")
    @FieldDef(label="证件号码", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = true)
    @NotBlank(message = "客户证件号不能为空")
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`trading_firm_code`")
    @FieldDef(label="交易市场编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    @NotBlank(message = "交易市场不能为空")
    String getTradingFirmCode();

    void setTradingFirmCode(String tradingFirmCode);

    @Column(name = "`available`")
    @FieldDef(label="可用积分")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getAvailable();

    void setAvailable(Integer available);

    @Column(name = "`buyer_points`")
    @FieldDef(label="买方积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBuyerPoints();

    void setBuyerPoints(Integer buyerPoints);

    @Column(name = "`seller_points`")
    @FieldDef(label="卖方积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSellerPoints();

    void setSellerPoints(Integer sellerPoints);

    @Column(name = "`day_points`")
    @FieldDef(label="当天积分总和")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getDayPoints();

    void setDayPoints(Integer dayPoints);
    @Column(name = "`reset_time`")
    @FieldDef(label="客户单日上限积分重置时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getResetTime();

    void setResetTime(Date resetTime);
}