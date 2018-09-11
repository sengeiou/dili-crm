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
 * This file was generated on 2018-03-20 11:29:31.
 */
@Table(name = "`rule_condition`")
public interface RuleCondition extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="规则条件ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`point_rule_id`")
    @FieldDef(label="所属规则id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPointRuleId();

    void setPointRuleId(Long pointRuleId);

    @Column(name = "`weight_type`")
    @FieldDef(label="权重类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getWeightType();

    void setWeightType(Integer weightType);

    @Column(name = "`condition_type`")
    @FieldDef(label="条件类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getConditionType();

    void setConditionType(Integer conditionType);

    @Column(name = "`value`")
    @FieldDef(label="条件值", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getValue();

    void setValue(String value);

    @Column(name = "`start_value`")
    @FieldDef(label="条件起始值", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getStartValue();

    void setStartValue(String startValue);

    @Column(name = "`end_value`")
    @FieldDef(label="条件结束值", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getEndValue();

    void setEndValue(String endValue);

    @Column(name = "`weight`")
    @FieldDef(label="权重")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getWeight();

    void setWeight(String weight);

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
}