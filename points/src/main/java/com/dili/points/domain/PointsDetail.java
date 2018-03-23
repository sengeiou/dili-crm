package com.dili.points.domain;

import com.dili.ss.domain.annotation.Like;
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
 * This file was generated on 2018-03-20 11:29:31.
 */
@Table(name = "`points_detail`")
public interface PointsDetail extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like(Like.BOTH)
    String getName();

    void setName(String name);
    
    @Column(name = "`certificate_number`")
    @FieldDef(label="客户证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`points`")
    @FieldDef(label="积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPoints();

    void setPoints(Integer points);

    @Column(name = "`balance`")
    @FieldDef(label="积分余额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBalance();

    void setBalance(Integer balance);

    @Column(name = "`generate_way`")
    @FieldDef(label="获得/使用方式")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getGenerateWay();

    void setGenerateWay(Integer generateWay);

    @Column(name = "`in_out`")
    @FieldDef(label="收支类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getInOut();

    void setInOut(Integer inOut);

    @Column(name = "`source_system`")
    @FieldDef(label="来源系统", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSourceSystem();

    void setSourceSystem(String sourceSystem);

    @Column(name = "`notes`")
    @FieldDef(label="备注", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`created`")
    @FieldDef(label="获得时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`order_code`")
    @FieldDef(label="订单编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrderCode();

    void setOrderCode(String orderCode);
}