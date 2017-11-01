package com.dili.alm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.alm.constant.AlmConstants;
import com.dili.alm.dao.MilestonesMapper;
import com.dili.alm.domain.Files;
import com.dili.alm.domain.Milestones;
import com.dili.alm.domain.dto.MilestonesDto;
import com.dili.alm.service.FilesService;
import com.dili.alm.service.MilestonesService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.quartz.domain.QuartzConstants;
import com.dili.ss.quartz.domain.ScheduleJob;
import com.dili.ss.quartz.service.ScheduleJobService;
import com.dili.ss.util.CronDateUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-20 11:02:17.
 */
@Service
public class MilestonesServiceImpl extends BaseServiceImpl<Milestones, Long> implements MilestonesService {

    @Autowired
    FilesService filesService;

    @Autowired
    ScheduleJobService scheduleJobService;

    public MilestonesMapper getActualDao() {
        return (MilestonesMapper)getDao();
    }

    @Override
    public int insertSelective(Milestones milestones) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        milestones.setPublishMemberId(userTicket.getId());
        milestones.setCreated(new Date());
        int rowCnt = super.insertSelective(milestones);
        //如果要通知，则生成调度信息
        if(milestones.getEmailNotice().equals(1)){
            ScheduleJob scheduleJob = DTOUtils.newDTO(ScheduleJob.class);
            scheduleJob.setJobStatus(QuartzConstants.JobStatus.NORMAL.getCode());
            scheduleJob.setIsConcurrent(QuartzConstants.Concurrent.Async.getCode());
            scheduleJob.setJobGroup("milestones");
            scheduleJob.setJobName(milestones.getCode());
            scheduleJob.setDescription("里程碑发布通知, code:"+milestones.getCode()+", version:"+milestones.getVersion() + ", market:" + milestones.getMarket());
            scheduleJob.setSpringId("emailNoticeJob");
            scheduleJob.setStartDelay(0);
            scheduleJob.setMethodName("scan");
            scheduleJob.setCronExpression(CronDateUtils.getCron(new Date(System.currentTimeMillis()+10000)));
            scheduleJob.setJobData(JSONObject.toJSONStringWithDateFormat(milestones, "yyyy-MM-dd HH:mm:ss"));
            scheduleJobService.insertSelective(scheduleJob);
        }
        return rowCnt;
    }

    @Override
    public int updateSelective(Milestones milestones) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        milestones.setModifyMemberId(userTicket.getId());
        milestones.setModified(new Date());
	    //如果要通知，刷新调度信息
	    if(milestones.getEmailNotice().equals(1)){
		    Milestones oriMilestones = get(milestones.getId());
		    milestones = DTOUtils.link(milestones, oriMilestones, Milestones.class);
		    ScheduleJob scheduleJob = DTOUtils.newDTO(ScheduleJob.class);
		    scheduleJob.setJobStatus(QuartzConstants.JobStatus.NORMAL.getCode());
		    scheduleJob.setIsConcurrent(QuartzConstants.Concurrent.Async.getCode());
		    scheduleJob.setJobGroup("milestones");
		    scheduleJob.setJobName(milestones.getCode());
		    scheduleJob.setDescription("里程碑修改通知, code:"+milestones.getCode()+", version:"+milestones.getVersion() + ", market:" + milestones.getMarket());
		    scheduleJob.setSpringId("emailNoticeJob");
		    scheduleJob.setStartDelay(0);
		    scheduleJob.setMethodName("scan");
		    scheduleJob.setCronExpression(CronDateUtils.getCron(new Date(System.currentTimeMillis()+10000)));
		    scheduleJob.setJobData(JSONObject.toJSONStringWithDateFormat(milestones, "yyyy-MM-dd HH:mm:ss"));
		    ScheduleJob scheduleJobCondition = DTOUtils.newDTO(ScheduleJob.class);
		    scheduleJobCondition.setJobGroup("milestones");
		    scheduleJobCondition.setJobName(milestones.getCode());
		    List<ScheduleJob> scheduleJobs = scheduleJobService.list(scheduleJobCondition);
		    //如果数据库没有调度信息，则新增调度器
		    if(ListUtils.emptyIfNull(scheduleJobs).isEmpty()){
			    scheduleJobService.insertSelective(scheduleJob);
		    }else {
			    scheduleJob.setId(scheduleJobs.get(0).getId());
			    scheduleJobService.updateSelective(scheduleJob);
		    }
	    }
        return super.updateSelective(milestones);
    }

    @Override
    public int delete(Long id) {
        Milestones milestones = DTOUtils.newDTO(Milestones.class);
        milestones.setParentId(id);
        List<Milestones> list = list(milestones);
        //没有子里程碑才能删除，并且删除所有里程碑下的文件
        if(list.isEmpty()) {
            Files files = DTOUtils.newDTO(Files.class);
            files.setMilestonesId(id);
            List<Files> filesList = filesService.list(files);
            for(Files file : filesList){
                filesService.delete(file);
            }
            //如果有一个文件，则删除文件目录
            if(!filesList.isEmpty()) {
                File dest = new File(filesList.get(0).getUrl());
                dest.delete();
            }
            return super.delete(id);
        }else{
            return 0;
        }
    }

    @Override
    public EasyuiPageOutput listEasyuiPageByExample(Milestones milestones, boolean useProvider) throws Exception {
        SessionContext sessionContext = SessionContext.getSessionContext();
        if(sessionContext == null) {
            throw new RuntimeException("未登录");
        }
        List<Map> dataauth = sessionContext.dataAuth(AlmConstants.DATA_AUTH_TYPE_PROJECT);
        List<Long> projectIds = new ArrayList<>(dataauth.size());
        dataauth.forEach( t -> {
            projectIds.add(Long.parseLong(t.get("dataId").toString()));
        });
        MilestonesDto milestonesDto = DTOUtils.as(milestones, MilestonesDto.class);
        if(projectIds.isEmpty()){
            return new EasyuiPageOutput(0, null);
        }
        milestonesDto.setProjectIds(projectIds);
        return super.listEasyuiPageByExample(milestonesDto, useProvider);
    }
}