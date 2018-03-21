package com.dili.points.component;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.points.domain.Category;
import com.dili.points.service.CategoryService;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;

/**
 * 积分监听组件
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
public class PointsListener {
	private static final Logger logger=LoggerFactory.getLogger(PointsListener.class);
	@Autowired private CategoryService categoryService;
	@RabbitListener(queues = "#{rabbitConfiguration.TOPIC_QUEUE}")
	public void processBootTask(String categoryJson) {
		System.out.println("PointsListener:"+categoryJson);
		Category category=DTOUtils.newDTO(Category.class);
		if(this.convertAndValidate(category)) {
			Category example=DTOUtils.newDTO(Category.class);
			example.setCategoryId(category.getCategoryId());
			example.setSourceSystem(category.getSourceSystem());
			Optional<Category>firstOne=this.categoryService.listByExample(example).stream().findFirst();
			
			Category categoryDto=firstOne.orElse(category);
			categoryService.saveOrUpdate(categoryDto);	
		}else {
			logger.error("验证品类信息出错:{}",categoryJson);
		}
		
	}
	private boolean convertAndValidate(Category category) {
		
		
		return true;
	}
	
}
