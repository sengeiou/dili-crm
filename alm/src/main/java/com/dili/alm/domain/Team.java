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
 * This file was generated on 2017-10-24 14:31:10.
 */
@Table(name = "`team`")
public interface Team extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`project_id`")
    @FieldDef(label="所属项目id")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"projectProvider\"}")
    Long getProjectId();

    void setProjectId(Long projectId);

    @Column(name = "`member_id`")
    @FieldDef(label="所属成员id")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"memberProvider\"}")
    Long getMemberId();

    void setMemberId(Long memberId);

    @Column(name = "`type`")
    @FieldDef(label="团队类型", maxLength = 10)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"产品\",\"value\":1},{\"text\":\"开发\",\"value\":2},{\"text\":\"测试\",\"value\":3}],\"provider\":\"teamTypeProvider\"}")
    String getType();

    void setType(String type);

    @Column(name = "`member_state`")
    @FieldDef(label="状态")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"离开\",\"value\":0},{\"text\":\"加入\",\"value\":1}],\"provider\":\"MemberStateProvider\"}")
    Integer getMemberState();

    void setMemberState(Integer memberState);

    @Column(name = "`join_time`")
    @FieldDef(label="加入时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getJoinTime();

    void setJoinTime(Date joinTime);

    @Column(name = "`leave_time`")
    @FieldDef(label="离开时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getLeaveTime();

    void setLeaveTime(Date leaveTime);
}