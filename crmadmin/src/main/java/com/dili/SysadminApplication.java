package com.dili;

import com.dili.ss.retrofitful.annotation.RestfulScan;
//import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 由MyBatis Generator工具自动生成
 */
// =====================  Spring Cloud  =====================
//@EnableHystrixDashboard
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableDiscoveryClient
//@EnableFeignClients
//@EnableHystrix

// =====================  Spring Boot  =====================
// @EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
@EnableTransactionManagement
//@EnableAutoConfiguration(exclude = { ThymeleafAutoConfiguration.class, VelocityAutoConfiguration.class })
// @ImportResource(locations = "classpath:applicationContext.xml")
@ComponentScan(basePackages = { "com.dili.ss", "com.dili.sysadmin" })
// @EnableEncryptableProperties
//@EncryptablePropertySource(name = "EncryptedProperties", value = "classpath:conf/security.properties")
// @ServletComponentScan

// =====================  Other  =====================
@RestfulScan({"com.dili.sysadmin.rpc", "com.dili.sysadmin.sdk.rpc"})
@MapperScan(basePackages = {"com.dili.sysadmin.dao", "com.dili.ss.dao"})
// @EnableScheduling
/**
 * 除了内嵌容器的部署模式，Spring Boot也支持将应用部署至已有的Tomcat容器, 或JBoss, WebLogic等传统Java EE应用服务器。
 * 以Maven为例，首先需要将<packaging>从jar改成war，然后取消spring-boot-maven-plugin，然后修改Application.java
 * 继承SpringBootServletInitializer
 */
public class SysadminApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SysadminApplication.class, args);
	}

}
