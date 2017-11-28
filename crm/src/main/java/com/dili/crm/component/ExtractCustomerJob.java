package com.dili.crm.component;

import com.dili.ss.quartz.domain.ScheduleJob;
import com.dili.ss.quartz.domain.ScheduleMessage;
import com.dili.ss.quartz.service.ScheduleJobService;
import com.dili.ss.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

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

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
			List<ScheduleJob> scheduleJobs = scheduleJobService.list(null);
			for (ScheduleJob job : scheduleJobs) {
				scheduleJobService.addJob(job, true);
			}
		}
	}

	/**
	 * 抽取客户数据
	 *
	 * @param scheduleMessage
	 */
	public void scan(ScheduleMessage scheduleMessage) {
//		log.info("客户数据抽取");
//		List<Customer> customers = commonService.selectDto("select * from customer", Customer.class);
//		System.out.println("抓取到"+customers.size()+"条客户数据");
	}

}
