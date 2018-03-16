package com.dili.points.servlet;

import com.dili.sysadmin.sdk.session.SessionFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by asiamaster on 2017/7/5 0005.
 */
@Configuration
public class FilterBootConfig {

	@Bean
	public FilterRegistrationBean filterRegistrationBean(@Qualifier("sessionFilter") SessionFilter sessionFilter){
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(sessionFilter);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
}
