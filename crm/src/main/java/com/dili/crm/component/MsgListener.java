package com.dili.crm.component;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.dili.ss.rocketmq.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
public class MsgListener implements RocketMQListener {

	protected static final Logger log = LoggerFactory.getLogger(MsgListener.class);
	@Override
	public String getTopic() {
		return "testTopic";
	}

	@Override
	public String getTags() {
		return "tag";
	}

	@Override
	public void operate(MessageExt messageExt) {
		if (messageExt == null) {
			log.warn("listener AttributeMessageHandler content is null");
			return;
		}
		try {
			byte[] bytesMessage = messageExt.getBody();
			String json = new String(bytesMessage,"UTF-8");
			log.info("==============================================");
			log.info("监听收到数据:"+json);
			log.info("==============================================");
		} catch (Exception e) {
			log.error("listener AttributeMessageHandler Exception:%s",
					e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
