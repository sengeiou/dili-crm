package com.dili.crm.component;

import com.dili.crm.service.ICardETLService;
import com.dili.ss.quartz.domain.ScheduleMessage;
import com.dili.ss.quartz.service.ScheduleJobService;
import com.dili.ss.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 客户抽取调度器
 * Created by asiamaster on 2017/11/24 0024.
 */
@Component
public class ExtractCustomerJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(ExtractCustomerJob.class);

	@Autowired
	ScheduleJobService scheduleJobService;

	@Autowired
	CommonService commonService;
	//@Autowired	ICardETLService service;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//		if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
//			List<ScheduleJob> scheduleJobs = scheduleJobService.list(null);
//			for (ScheduleJob job : scheduleJobs) {
//				scheduleJobService.addJob(job, true);
//			}
//		}
	}

	/**
	 * 抽取客户数据
	 *
	 * @param scheduleMessage
	 */
	public void scan(ScheduleMessage scheduleMessage) {
		System.out.println("抽取客户");
//    	while(true) {
//    		boolean v=service.transIncrementData(null, 1000);
//    		if(!v) {
//    			break;
//    		}
//    	}
	}

}
