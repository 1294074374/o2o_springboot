package com.imooc.o2o.config.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.imooc.o2o.service.ProductSellDailyService;

@Configuration
public class QuartzConfiguration {
	@Autowired
	private ProductSellDailyService productSellDailyService;
	@Autowired
	private MethodInvokingJobDetailFactoryBean jobDetailFactory;
	@Autowired
	private CronTriggerFactoryBean productSellDailyTriggerFactory;
	
	/**
	 * 创建jobDeatilFacotry 并返回
	 * @return
	 */
	@Bean(name="jobDeatilFacotry")
	public MethodInvokingJobDetailFactoryBean createDetail() {
		MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
		
		return null;
	}
}
