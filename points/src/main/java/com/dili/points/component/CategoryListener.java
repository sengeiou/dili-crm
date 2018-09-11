package com.dili.points.component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dili.ss.util.AESUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.dili.points.converter.DtoMessageConverter;
import com.dili.points.domain.Category;
import com.dili.points.service.CategoryService;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;

/**
 * 积分监听组件 Created by asiamaster on 2017/11/7 0007.
 */
//@Component
//@ConditionalOnExpression("'${mq.enable}'=='true'")
public class CategoryListener {
	private static final Logger logger = LoggerFactory.getLogger(CategoryListener.class);
	@Autowired
	private CategoryService categoryService;
	@Value("${aesKey:}")
	private String aesKey;

	@RabbitListener(queues = "#{rabbitConfiguration.CATEGORY_TOPIC_QUEUE}")
	public void processBootTask(Message message) throws UnsupportedEncodingException {
		
		logger.info("收到消息: "+message);
		String data = new String(message.getBody(), "UTF-8");
		String categoryJson = AESUtil.decrypt(data, aesKey);
		// 将Json转换为map
		Map<String, Object> map = DtoMessageConverter.convertAsMap(categoryJson);
		if (map.isEmpty()) {
			logger.error("品类信息数据转换出错:" + categoryJson);
			return;
		}
		try {
			String type = StringUtils.trimToNull(String.valueOf(map.get("type")));
			if (type == null || type.equalsIgnoreCase("json")) {
				Map<String, Object> dataMap = (Map<String, Object>) map.get("data");

				// 取出action
				String action = String.valueOf(dataMap.remove("action"));

				// 将map转换为Category对象
				Category category = DTOUtils.proxy(new DTO(dataMap), Category.class);
				//if ("add".equals(action)) {
				//	categoryService.insertExact(category);
				//} else if ("update".equals(action)) {
					// 通过条件查询已有的Category
					Category example = DTOUtils.newDTO(Category.class);
					example.setCategoryId(category.getCategoryId());
					example.setSourceSystem(category.getSourceSystem());
					Optional<Category> firstOne = this.categoryService.listByExample(example).stream().findFirst();
					Category categoryDto = firstOne.orElse(category);
					// 将查询到的id设置到传递过来的对象,以进行更新
					category.setId(categoryDto.getId());
					categoryService.saveOrUpdate(category);
				//}

			} else {
				logger.error("数据类型不正确:"+categoryJson);
			}

		} catch (Exception e) {
			logger.error("处理品类信息" + categoryJson + "出错", e);
		}

	}

}
