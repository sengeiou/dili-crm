package com.dili.points.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 积分监听组件
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
public class PointsListener {

	@RabbitListener(queues = "#{rabbitConfiguration.TOPIC_QUEUE}")
	public void processBootTask(String customer) {
		System.out.println("PointsListener:"+customer);
	}
}
