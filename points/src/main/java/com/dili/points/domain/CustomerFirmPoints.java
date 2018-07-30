package com.dili.points.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`certificate_number`")
    @FieldDef(label="证件号码", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`trading_firm_code`")
    @FieldDef(label="交易市场编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getTradingFirmCode();

    void setTradingFirmCode(String tradingFirmCode);

    @Column(name = "`available`")
    @FieldDef(label="可用积分")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getAvailable();

    void setAvailable(Long available);
}