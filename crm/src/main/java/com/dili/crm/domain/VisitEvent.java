package com.dili.crm.domain;

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
 * This file was generated on 2017-11-21 16:39:42.
 */
@Table(name = "`visit_event`")
public interface VisitEvent extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`customer_visit_id`")
    @FieldDef(label="客户回访")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerVisitId();

    void setCustomerVisitId(Long customerVisitId);

    @Column(name = "`time`")
    @FieldDef(label="时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getTime();

    void setTime(Date time);

    @Column(name = "`mode`")
    @FieldDef(label="方式", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"dataDictionaryValueProvider\",\"queryParams\":{\"dd_id\":9}}")
    String getMode();

    void setMode(String mode);

    @Column(name = "`user_id`")
    @FieldDef(label="处理人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "`notes`")
    @FieldDef(label="事件描述", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);
}