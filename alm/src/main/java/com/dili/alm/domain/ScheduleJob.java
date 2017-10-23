package com.dili.alm.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;


/**
 * 由MyBatis Generator工具自动生成
 *
 * This file was generated on 2017-07-27 15:53:17.
 */
@Table(name = "`schedule_job`")
public class ScheduleJob extends BaseDomain {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     */
    @Column(name = "`modified`")
    private Date modified;

    /**
     * job名称
     */
    @Column(name = "`job_name`")
    private String jobName;

    /**
     * job分组
     */
    @Column(name = "`job_group`")
    private String jobGroup;

    /**
     * 工作状态##是否启动任务. ##{table:"data_dictionary_value", valueField:"value", textField:"code", queryParams:{yn:1, dd_id:1}, orderByClause: "sort asc"}
     */
    @Column(name = "`job_status`")
    private Integer jobStatus;

    /**
     * job数据##json
     */
    @Column(name = "`job_data`")
    private String jobData;

    @Column(name = "`cron_expression`")
    private String cronExpression;

    /**
     * 调度间隔##简单调度，默认以秒为单位
     */
    @Column(name = "`repeat_interval`")
    private Integer repeatInterval;

    /**
     * 延迟调度时间##启动调度器后，多少秒开始执行调度
     */
    @Column(name = "`start_delay`")
    private Integer startDelay;

    /**
     * 调度器描述
     */
    @Column(name = "`description`")
    private String description;

    /**
     * bean类##任务执行时调用类的全名，用于反射
     */
    @Column(name = "`bean_class`")
    private String beanClass;

    /**
     * springBeanId##spring的beanId，直接从spring中获取
     */
    @Column(name = "`spring_id`")
    private String springId;

    /**
     * 远程url##支持远程调用restful url
     */
    @Column(name = "`url`")
    private String url;

    /**
     * 并发/同步##{data:[{value:"1", text:"并发"},{value:"0", text:"同步"}]}
     */
    @Column(name = "`is_concurrent`")
    private Integer isConcurrent;

    /**
     * 方法名##bean_class和spring_id需要配置方法名
     */
    @Column(name = "`method_name`")
    private String methodName;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    @FieldDef(label="主键")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return created - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreated() {
        return created;
    }

    /**
     * 设置创建时间
     *
     * @param created 创建时间
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * 获取修改时间
     *
     * @return modified - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getModified() {
        return modified;
    }

    /**
     * 设置修改时间
     *
     * @param modified 修改时间
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
     * 获取job名称
     *
     * @return job_name - job名称
     */
    @FieldDef(label="job名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = true)
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置job名称
     *
     * @param jobName job名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 获取job分组
     *
     * @return job_group - job分组
     */
    @FieldDef(label="job分组", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = true)
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * 设置job分组
     *
     * @param jobGroup job分组
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * 获取工作状态##是否启动任务. ##{table:"data_dictionary_value", valueField:"value", textField:"code", queryParams:{yn:1, dd_id:1}, orderByClause: "sort asc"}
     *
     * @return job_status - 工作状态##是否启动任务. ##{table:"data_dictionary_value", valueField:"value", textField:"code", queryParams:{yn:1, dd_id:1}, orderByClause: "sort asc"}
     */
    @FieldDef(label="工作状态")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"queryParams\":{\"yn\":1,\"dd_id\":1},\"valueField\":\"value\",\"orderByClause\":\"sort asc\",\"table\":\"data_dictionary_value\",\"textField\":\"code\"}")
    public Integer getJobStatus() {
        return jobStatus;
    }

    /**
     * 设置工作状态##是否启动任务. ##{table:"data_dictionary_value", valueField:"value", textField:"code", queryParams:{yn:1, dd_id:1}, orderByClause: "sort asc"}
     *
     * @param jobStatus 工作状态##是否启动任务. ##{table:"data_dictionary_value", valueField:"value", textField:"code", queryParams:{yn:1, dd_id:1}, orderByClause: "sort asc"}
     */
    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    /**
     * 获取job数据##json
     *
     * @return job_data - job数据##json
     */
    @FieldDef(label="job数据", maxLength = 1000)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getJobData() {
        return jobData;
    }

    /**
     * 设置job数据##json
     *
     * @param jobData job数据##json
     */
    public void setJobData(String jobData) {
        this.jobData = jobData;
    }

    /**
     * @return cron_expression
     */
    @FieldDef(label="cronExpression", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * @param cronExpression
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 获取调度间隔##简单调度，默认以秒为单位
     *
     * @return repeat_interval - 调度间隔##简单调度，默认以秒为单位
     */
    @FieldDef(label="调度间隔")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    /**
     * 设置调度间隔##简单调度，默认以秒为单位
     *
     * @param repeatInterval 调度间隔##简单调度，默认以秒为单位
     */
    public void setRepeatInterval(Integer repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    /**
     * 获取延迟调度时间##启动调度器后，多少秒开始执行调度
     *
     * @return start_delay - 延迟调度时间##启动调度器后，多少秒开始执行调度
     */
    @FieldDef(label="延迟调度时间")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getStartDelay() {
        return startDelay;
    }

    /**
     * 设置延迟调度时间##启动调度器后，多少秒开始执行调度
     *
     * @param startDelay 延迟调度时间##启动调度器后，多少秒开始执行调度
     */
    public void setStartDelay(Integer startDelay) {
        this.startDelay = startDelay;
    }

    /**
     * 获取调度器描述
     *
     * @return description - 调度器描述
     */
    @FieldDef(label="调度器描述", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDescription() {
        return description;
    }

    /**
     * 设置调度器描述
     *
     * @param description 调度器描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取bean类##任务执行时调用类的全名，用于反射
     *
     * @return bean_class - bean类##任务执行时调用类的全名，用于反射
     */
    @FieldDef(label="bean类", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBeanClass() {
        return beanClass;
    }

    /**
     * 设置bean类##任务执行时调用类的全名，用于反射
     *
     * @param beanClass bean类##任务执行时调用类的全名，用于反射
     */
    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 获取springBeanId##spring的beanId，直接从spring中获取
     *
     * @return spring_id - springBeanId##spring的beanId，直接从spring中获取
     */
    @FieldDef(label="springBeanId", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSpringId() {
        return springId;
    }

    /**
     * 设置springBeanId##spring的beanId，直接从spring中获取
     *
     * @param springId springBeanId##spring的beanId，直接从spring中获取
     */
    public void setSpringId(String springId) {
        this.springId = springId;
    }

    /**
     * 获取远程url##支持远程调用restful url
     *
     * @return url - 远程url##支持远程调用restful url
     */
    @FieldDef(label="远程url", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getUrl() {
        return url;
    }

    /**
     * 设置远程url##支持远程调用restful url
     *
     * @param url 远程url##支持远程调用restful url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取并发/同步##{data:[{value:"1", text:"并发"},{value:"0", text:"同步"}]}
     *
     * @return is_concurrent - 并发/同步##{data:[{value:"1", text:"并发"},{value:"0", text:"同步"}]}
     */
    @FieldDef(label="并发/同步")
    @EditMode(editor = FieldEditor.Combo, required = true, params="{\"data\":[{\"text\":\"并发\",\"value\":\"1\"},{\"text\":\"同步\",\"value\":\"0\"}]}")
    public Integer getIsConcurrent() {
        return isConcurrent;
    }

    /**
     * 设置并发/同步##{data:[{value:"1", text:"并发"},{value:"0", text:"同步"}]}
     *
     * @param isConcurrent 并发/同步##{data:[{value:"1", text:"并发"},{value:"0", text:"同步"}]}
     */
    public void setIsConcurrent(Integer isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    /**
     * 获取方法名##bean_class和spring_id需要配置方法名
     *
     * @return method_name - 方法名##bean_class和spring_id需要配置方法名
     */
    @FieldDef(label="方法名", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置方法名##bean_class和spring_id需要配置方法名
     *
     * @param methodName 方法名##bean_class和spring_id需要配置方法名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}