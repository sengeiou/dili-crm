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
 * This file was generated on 2018-03-20 11:29:31.
 */
@Table(name = "`points_rule`")
public interface PointsRule extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="规则ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="规则名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`code`")
    @FieldDef(label="规则编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`customer_type`")
    @FieldDef(label="客户类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"dataDictionaryValueProvider\",\"queryParams\":{\"dd_id\":4}}")
    String getCustomerType();

    void setCustomerType(String customerType);

    @Column(name = "`business_type`")
    @FieldDef(label="交易类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBusinessType();

    void setBusinessType(Integer businessType);

    @Column(name = "`computing_standard`")
    @FieldDef(label="计算标准")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getComputingStandard();

    void setComputingStandard(Integer computingStandard);

    @Column(name = "`computing_parameter`")
    @FieldDef(label="计算参数")
    @EditMode(editor = FieldEditor.Number, required = false)
    Float getComputingParameter();

    void setComputingParameter(Float computingParameter);

    @Column(name = "`created_id`")
    @FieldDef(label="创建人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatedId();

    void setCreatedId(Long createdId);

    @Column(name = "`modified_id`")
    @FieldDef(label="修改人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getModifiedId();

    void setModifiedId(Long modifiedId);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);
    @Column(name = "`yn`")
    @FieldDef(label="是否可用")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getYn();

    void setYn(Integer yn);
}
