package com.dili.crm.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-11-21 17:27:41.
 */
@Table(name = "`customer_visit`")
public interface CustomerVisit extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`user_id`")
    @FieldDef(label="回访人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "`code`")
    @FieldDef(label="回访编号")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();
    void setCode(String code);

    @Column(name = "`customer_id`")
    @FieldDef(label="回访对象")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`subject`")
    @FieldDef(label="主题", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSubject();

    void setSubject(String subject);

    @Column(name = "`mode`")
    @FieldDef(label="回访方式", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"dataDictionaryValueProvider\",\"queryParams\":{\"dd_id\":9}}")
    String getMode();

    void setMode(String mode);

    @Column(name = "`priority`")
    @FieldDef(label="优先级", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"dataDictionaryValueProvider\",\"queryParams\":{\"dd_id\":10}}")
    String getPriority();

    void setPriority(String priority);

    @Column(name = "`finish_time`")
    @FieldDef(label="完成时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getFinishTime();

    void setFinishTime(Date finishTime);

    @Column(name = "`state`")
    @FieldDef(label="回访状态")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"新建\",\"value\":1},{\"text\":\"进行中\",\"value\":2},{\"text\":\"完成\",\"value\":3}],\"provider\":\"visitStateProvider\"}")
    Integer getState();

    void setState(Integer state);

    @Column(name = "`notes`")
    @FieldDef(label="描述", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

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

    @Column(name = "`modified_id`")
    @FieldDef(label="修改人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getModifiedId();

    void setModifiedId(Long modifiedId);

    @Column(name = "`created_id`")
    @FieldDef(label="回访创建者")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatedId();

    void setCreatedId(Long createdId);
}