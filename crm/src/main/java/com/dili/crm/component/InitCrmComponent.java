package com.dili.crm.component;

import com.dili.crm.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * CRM系统初始化组件
 * Created by asiamaster on 2017/11/7 0007.
 */
@Component
public class InitCrmComponent implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	CacheService cacheService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		cacheService.refreshDepartment();
		cacheService.refreshCity();
	}
}
