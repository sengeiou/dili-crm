package com.dili.crm.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
public class InitCrmComponent {
	@Autowired
	private TopicProducer topicProducer;

	@PostConstruct
	public void init(){
		topicProducer.send("testTopic", "tag","hello 客户!");
		System.out.println("发送消息成功");
	}

}
