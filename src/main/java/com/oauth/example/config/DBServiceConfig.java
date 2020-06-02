package com.oauth.example.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages="com.oauth.example")
@PropertySource("file:E:/apache-tomcat-7.0.34-windows-x64/apache-tomcat-7.0.34/conf/test-service.properties")
public class DBServiceConfig {
	
	private String validationQry = "SELECT 1 from dual";
	
	 @Autowired
	 private Environment environment;
	 
	 @Bean
	 public DataSource getDataSource() {
			DriverManagerDataSource bds = new DriverManagerDataSource();
			bds.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
			bds.setUrl(environment.getRequiredProperty("spring.datasource.url"));
			bds.setUsername(environment.getRequiredProperty("spring.datasource.username"));
			bds.setPassword(environment.getRequiredProperty("spring.datasource.password"));
			return bds;

		 
	 }
	

   
}
