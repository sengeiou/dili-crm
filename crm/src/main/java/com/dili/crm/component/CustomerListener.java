package com.dili.crm.component;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.User;
import com.dili.crm.service.CacheService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 客户消费组件
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
public class CustomerListener {

	@RabbitListener(queues = "#{rabbitConfiguration.TOPIC_QUEUE}")
	public void processBootTask(String customer) {
		System.out.println("CustomerListener:"+customer);
	}
}
