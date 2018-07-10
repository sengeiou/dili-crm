package com.dili.crm;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableAdminServer
public class CrmAdminServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
            SpringApplication.run(CrmAdminServerApplication.class, args);
    }


}
