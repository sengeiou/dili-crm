package com.dili.alm.domain;

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
 * This file was generated on 2017-10-18 17:22:54.
 */
@Table(name = "`project`")
public interface Project extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`parent_id`")
    @FieldDef(label="上级项目id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getParentId();

    void setParentId(Long parentId);

    @Column(name = "`name`")
    @FieldDef(label="项目名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like(Like.BOTH)
    String getName();

    void setName(String name);

    @Column(name = "`type`")
    @FieldDef(label="项目类型", maxLength = 10)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"queryParams\":{\"yn\":1,\"dd_id\":3},\"valueField\":\"value\",\"orderByClause\":\"order_number asc\",\"table\":\"data_dictionary_value\",\"textField\":\"code\"}")
    String getType();

    void setType(String type);

    @Column(name = "`project_manager`")
    @FieldDef(label="开发负责人")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"memberProvider\"}")
    Long getProjectManager();

    void setProjectManager(Long projectManager);

    @Column(name = "`test_manager`")
    @FieldDef(label="测试负责人")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"memberProvider\"}")
    Long getTestManager();

    void setTestManager(Long testManager);

    @Column(name = "`product_manager`")
    @FieldDef(label="产品负责人")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"memberProvider\"}")
    Long getProductManager();

    void setProductManager(Long productManager);

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