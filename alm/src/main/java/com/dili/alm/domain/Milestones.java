package com.dili.alm.domain;

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
 * This file was generated on 2017-10-20 11:02:17.
 */
@Table(name = "`milestones`")
public interface Milestones extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`code`")
    @FieldDef(label="项目发布编号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`project_id`")
    @FieldDef(label="项目id")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"projectProvider\"}")
    Long getProjectId();

    void setProjectId(Long projectId);

    @Column(name = "`parent_id`")
    @FieldDef(label="上级id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getParentId();

    void setParentId(Long parentId);

    @Column(name = "`git`")
    @FieldDef(label="git地址", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getGit();

    void setGit(String git);

    @Column(name = "`doc_url`")
    @FieldDef(label="redmine文档地址", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDocUrl();

    void setDocUrl(String docUrl);

    @Column(name = "`version`")
    @FieldDef(label="版本号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getVersion();

    void setVersion(String version);

    @Column(name = "`market`")
    @FieldDef(label="所属市场", maxLength = 40)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"queryParams\":{\"yn\":1,\"dd_id\":1},\"valueField\":\"value\",\"orderByClause\":\"order_number asc\",\"table\":\"data_dictionary_value\",\"textField\":\"code\"}")
    String getMarket();

    void setMarket(String market);

    @Column(name = "`project_phase`")
    @FieldDef(label="项目阶段", maxLength = 40)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"queryParams\":{\"yn\":1,\"dd_id\":2},\"valueField\":\"value\",\"orderByClause\":\"order_number asc\",\"table\":\"data_dictionary_value\",\"textField\":\"code\"}")
    String getProjectPhase();

    void setProjectPhase(String projectPhase);

    @Column(name = "`notes`")
    @FieldDef(label="备注", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`publish_member_id`")
    @FieldDef(label="发布人id")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"memberProvider\"}")
    Long getPublishMemberId();

    void setPublishMemberId(Long publishMemberId);

    @Column(name = "`modify_member_id`")
    @FieldDef(label="修改人id")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"memberProvider\"}")
    Long getModifyMemberId();

    void setModifyMemberId(Long modifyMemberId);

    @Column(name = "`created`")
    @FieldDef(label="发布时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    @Column(name = "`release_time`")
    @FieldDef(label="上线时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getReleaseTime();

    void setReleaseTime(Date releaseTime);

    @Column(name = "`email_notice`")
    @FieldDef(label="是否上线通知")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"通知\",\"value\":0},{\"text\":\"不通知\",\"value\":1}],\"provider\":\"emailNoticeProvider\"}")
    Integer getEmailNotice();

    void setEmailNotice(Integer emailNotice);

    @Column(name = "`host`")
    @FieldDef(label="主机", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getHost();

    void setHost(String host);

    @Column(name = "`port`")
    @FieldDef(label="端口")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPort();

    void setPort(Integer port);

    @Column(name = "`visit_url`")
    @FieldDef(label="访问地址", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getVisitUrl();

    void setVisitUrl(String visitUrl);
}