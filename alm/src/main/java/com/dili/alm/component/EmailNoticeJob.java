package com.dili.alm.component;

import com.dili.ss.quartz.domain.ScheduleJob;
import com.dili.ss.quartz.domain.ScheduleMessage;
import com.dili.ss.quartz.service.ScheduleJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * Created by asiamaster on 2017/10/24 0024.
 */
@Component
public class EmailNoticeJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(EmailNoticeJob.class);

	@Autowired
	ScheduleJobService scheduleJobService;

	@Autowired
	private JavaMailSender mailSender;

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
	 * 发布email
	 *
	 * @param scheduleMessage
	 */
	public void scan(ScheduleMessage scheduleMessage) {
		System.out.println("发email:" + scheduleMessage.getJobData());
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom("wangmi@diligrp.com");
//		message.setTo("asiamastor@vip.qq.com");
//		message.setSubject("主题：简单邮件");
//		message.setText("测试邮件内容:"+scheduleMessage.getJobData());
//		mailSender.send(message);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = null;
		try {
			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom("wangmi@diligrp.com");
			helper.setTo("asiamastor@vip.qq.com");
			helper.setSubject("主题：有附件");
			helper.setText("有附件的邮件");
			String prefix = "fileupload/milestones/1";
			FileSystemResource file = new FileSystemResource(new File("front.jpg"));
			helper.addAttachment("附件-1.jpg", file);
			helper.addAttachment("附件-2.jpg", file);
		} catch (MessagingException e) {
			e.printStackTrace();
			return;
		}
		mailSender.send(mimeMessage);
	}

}
