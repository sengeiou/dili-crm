package com.dili.crm.component;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;


public class AsyncProducer {
	public static void main1(String[] args) throws MQClientException, InterruptedException, UnsupportedEncodingException {

		DefaultMQProducer producer = new DefaultMQProducer("CID_JODIE_1");
		producer.setNamesrvAddr("localhost:9876");
		producer.setInstanceName("Producer");
		producer.setVipChannelEnabled(false);
		producer.setRetryTimesWhenSendAsyncFailed(0);
		producer.start();
		for (int i = 0; i < 1; i++) {
			try {
				final int index = i;
				Message msg = new Message("Jodie_topic_1023",// topic
						"TagA",// tag
						"OrderID188",// key
						("Hello MetaQ").getBytes(RemotingHelper.DEFAULT_CHARSET));// body
				producer.send(msg, new SendCallback() {
					@Override
					public void onSuccess(SendResult sendResult) {
						System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
					}

					@Override
					public void onException(Throwable e) {
						System.out.printf("%-10d Exception %s %n", index, e);
						e.printStackTrace();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

//            TimeUnit.MILLISECONDS.sleep(1);
		}

		producer.shutdown();
	}
}