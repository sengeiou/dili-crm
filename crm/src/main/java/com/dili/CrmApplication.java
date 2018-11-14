package com.dili;

import com.dili.ss.datasource.aop.DynamicRoutingDataSourceRegister;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 由MyBatis Generator工具自动生成
 */
//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableHystrix
//@EnableHystrixDashboard
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
//@EnableScheduling
@EnableTransactionManagement
//@EnableAutoConfiguration(exclude = {ThymeleafAutoConfiguration.class, VelocityAutoConfiguration.class})
@MapperScan(basePackages = {"com.dili.crm.dao", "com.dili.ss.dao", "com.dili.ss.quartz.dao"})
//@ImportResource(locations = "classpath:applicationContext.xml")
@ComponentScan(basePackages={"com.dili.ss","com.dili.crm","com.dili.uap.sdk"})
@RestfulScan({"com.dili.crm.rpc", "com.dili.uap.sdk.rpc"})
@Import({DynamicRoutingDataSourceRegister.class})// 注册动态多数据源
//@EnableEncryptableProperties
//@PropertySource(name="EncryptedProperties", value = "classpath:security.properties")
//@EncryptablePropertySource(name = "EncryptedProperties", value = "classpath:security.properties")
//@ServletComponentScan
/**
 * 除了内嵌容器的部署模式，Spring Boot也支持将应用部署至已有的Tomcat容器, 或JBoss, WebLogic等传统Java EE应用服务器。
 * 以Maven为例，首先需要将<packaging>从jar改成war，然后取消spring-boot-maven-plugin，然后修改Application.java
 * 继承SpringBootServletInitializer
 */
public class CrmApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

}
