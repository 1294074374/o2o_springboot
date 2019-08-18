package com.imooc.o2o.config.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

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
	 * 
	 * @return
	 */
	@Bean(name = "jobDeatilFacotry")
	public MethodInvokingJobDetailFactoryBean createDetail() {
		MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
		jobDetailFactoryBean.setName("prouct_sell_daily_job");
		jobDetailFactoryBean.setGroup("job_prouct_sell_daily_group");
		jobDetailFactoryBean.setConcurrent(false);
		jobDetailFactoryBean.setTargetObject(productSellDailyService);
		jobDetailFactoryBean.setTargetMethod("dailyCalculate");
		return jobDetailFactoryBean;
	}

	/*
	 * 创建cronTirggerFactory并返回
	 */
	@Bean("productSellDailyTriggerFactory")
	public CronTriggerFactoryBean createCronTriggerFactory() {
		CronTriggerFactoryBean tiggerFactory = new CronTriggerFactoryBean();
		tiggerFactory.setName("prouct_sell_daily_job");
		tiggerFactory.setGroup("job_prouct_sell_daily_group");
		tiggerFactory.setJobDetail(jobDetailFactory.getObject());
		// cron表达式 每天凌晨执行一次
		tiggerFactory.setCronExpression("0 0 0 * * ? ");
		return tiggerFactory;
	}

	/**
	 * 创建调度工厂并返回
	 * 
	 * @return
	 */
	@Bean("schedulerFactory")
	public SchedulerFactoryBean createSchedulerFactory() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setTriggers(productSellDailyTriggerFactory.getObject());
		return schedulerFactory;
	}
}
