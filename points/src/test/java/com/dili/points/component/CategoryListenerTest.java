package com.dili.points.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.dili.PointsApplication;
import com.dili.points.domain.Category;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@SpringBootTest(classes = PointsApplication.class)
public class CategoryListenerTest {
	 @InjectMocks @Autowired CategoryListener categoryListener;
	 @Test
	 public void test() throws JsonProcessingException {
		 Category category=DTOUtils.newDTO(Category.class);
		 category.setCategoryId("1");
		 category.setParentCategoryId(null);
		 category.setSourceSystem("settlement");
		 category.setName("测试品类2");
		 DTO map= DTOUtils.go(category);
		 map.put("action", "update");
		 ObjectMapper objMapper=new ObjectMapper();
		 String categoryJson=objMapper.writeValueAsString(map);
		 this.categoryListener.processBootTask(categoryJson);
		 
		 
	 }
}
