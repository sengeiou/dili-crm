package com.dili.points.component;

import com.dili.PointsApplication;
import com.dili.points.domain.Order;
import com.dili.points.domain.OrderItem;
import com.dili.points.domain.PointsRule;
import com.dili.points.domain.RuleCondition;
import com.dili.points.service.RuleConditionService;
import com.dili.ss.dto.DTOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
@RunWith(SpringJUnit4ClassRunner.class)
// @SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@SpringBootTest(classes = PointsApplication.class)
public class RuleConditionServiceTest {
	 @InjectMocks @Autowired OrderListener orderListener;
	
	// @MockBean PointsRuleService pointsRuleService;
	// @Mock RuleConditionService ruleConditionService;
	   
	@Before
    public void setUp() throws Exception {
        // 初始化测试用例类中由Mockito的注解标注的所有模拟对象
        MockitoAnnotations.initMocks(this);
    }
	@Autowired RuleConditionService ruleConditionService;
	@Test
	public void dd() {
		RuleCondition rc=this.ruleConditionService.get(11L);
		String weight=rc.getWeight();
		System.out.println(weight);
		BigDecimal bc=new BigDecimal(weight);
		System.out.println(bc.toPlainString());
		
	}
}
