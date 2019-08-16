package com.imooc.o2o.config.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
//EnableTransactionManagement注解开启事务支持 在service方法上添加注解Transaction便可
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {
	@Autowired
	//注入DataSourceConfigration 里面的dataSource 通过createDataSource获取
	private DataSource dataSource;
	
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}
