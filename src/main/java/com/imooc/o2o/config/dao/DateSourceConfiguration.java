package com.imooc.o2o.config.dao;

import java.beans.PropertyVetoException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.imooc.o2o.util.DESUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 配置datesource 到ioc容器中
 * 
 * @author lenovo
 *
 */
@Configuration
//配置mybatic maper的扫描路径
@MapperScan("com.imooc.o2o.dao")
public class DateSourceConfiguration {
	@Value("${jdbc.driver}")
	private String jdbcDriver;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;

	/**
	 * 配置数据源 连接信息
	 * 
	 * @return
	 * @throws PropertyVetoException
	 */
	@Bean(name = "dataSource")
	public ComboPooledDataSource createDateSource() throws PropertyVetoException {
		// 生成ComboPooledDataSource实例
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		// 驱动
		dataSource.setDriverClass(jdbcDriver);
		// 数据库链接
		dataSource.setJdbcUrl(jdbcUrl);
		// 用户名
		dataSource.setUser(DESUtil.getDecryptString(jdbcUsername));
		// 密码
		dataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));
		// 配置数据池最大线程数
		dataSource.setMaxPoolSize(30);
		// 连接池最小线程数
		dataSource.setMinPoolSize(10);
		// 关闭连接后不能自动提交
		dataSource.setAutoCommitOnClose(false);
		// 连接超时时间
		dataSource.setCheckoutTimeout(10000);
		// 连接失败重试次数
		dataSource.setAcquireRetryAttempts(2);
		return dataSource;
	}

}
