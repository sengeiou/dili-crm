package com.dili.alm.component;

import com.dili.ss.quartz.domain.QuartzConstants;
import com.dili.ss.quartz.domain.ScheduleJob;
import com.dili.ss.quartz.domain.ScheduleMessage;
import com.dili.ss.quartz.service.ScheduleJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by asiamaster on 2017/10/24 0024.
 */
@Component
@ConditionalOnProperty(name = "quartz.enabled")
public class QuartzRecycleJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(QuartzRecycleJob.class);

	@Autowired
	ScheduleJobService scheduleJobService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//		if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
//		}
	}

	/**
	 * 发布email
	 *
	 * @param scheduleMessage
	 */
	public void scan(ScheduleMessage scheduleMessage) {
		try {
			List<ScheduleJob> schedulingJobs = scheduleJobService.getAllJob();
			for(ScheduleJob scheduleJob : schedulingJobs){
				//过滤掉自己
				if(scheduleJob.getJobName().equals("quartzRecycleJob") && scheduleJob.getJobGroup().equals("system")){
					continue;
				}
				//无，完成和错误状态的都删除
				if(scheduleJob.getJobStatus().equals(QuartzConstants.JobStatus.NONE) ||
						scheduleJob.getJobStatus().equals(QuartzConstants.JobStatus.COMPLETE) ||
						scheduleJob.getJobStatus().equals(QuartzConstants.JobStatus.ERROR) ){
					scheduleJobService.delete(scheduleJob.getId(), true);
				}else{//正常，阻塞和暂停状态只更新数据库状态，不调度
					scheduleJobService.updateSelective(scheduleJob);
				}
			}
			//删除没在调度器中的数据
			List<ScheduleJob> dbScheduleJobs = scheduleJobService.list(null);
			for(ScheduleJob scheduleJob : dbScheduleJobs){
				if(!containsScheduleJob(schedulingJobs, scheduleJob)){
					scheduleJobService.delete(scheduleJob.getId(), false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("QuartzRecycleJob异常:"+e.getMessage());
		}
	}

	private boolean containsScheduleJob(List<ScheduleJob> scheduleJobs, ScheduleJob scheduleJob){
		for(ScheduleJob job : scheduleJobs){
			if(job.getId().equals(scheduleJob.getId())){
				return true;
			}
		}
		return false;
	}
}