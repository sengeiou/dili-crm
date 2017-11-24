package com.dili.crm.component;

import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.dili.ss.rocketmq.RocketMQProducer;
import com.dili.ss.rocketmq.exception.RocketMqException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

/**
 * Created by asiamaster on 2017/11/7 0007.
 */
//@Component
public class TopicProducer {

	@Autowired
	private RocketMQProducer rocketMQProducer;

	public void send(final String topic, final String tag, final String message){
		try {
			Message mesg = new Message(topic, tag, message.getBytes(RemotingHelper.DEFAULT_CHARSET));
			rocketMQProducer.sendMsg(mesg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (RocketMqException e) {
			System.out.println(String.format("发送MQ异常！TOPIC=%s,内容=%s", topic, message));
		}
	}
}
