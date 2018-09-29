package com.dili.dp.domain;

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
 * This file was generated on 2018-09-26 11:59:52.
 */
@Table(name = "`customer_stats`")
public interface CustomerStats extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`firm_code`")
    @FieldDef(label="所属市场", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getFirmCode();

    void setFirmCode(String firmCode);

    @Column(name = "`date`")
    @FieldDef(label="日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    Date getDate();

    void setDate(Date date);

    @Column(name = "`customer_count`")
    @FieldDef(label="客户数")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getCustomerCount();

    void setCustomerCount(Integer customerCount);
}