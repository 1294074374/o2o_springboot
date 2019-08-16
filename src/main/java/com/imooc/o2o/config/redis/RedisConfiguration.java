package com.imooc.o2o.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.imooc.o2o.cache.JedisPoolWriper;
import com.imooc.o2o.cache.JedisUtil;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
	@Value("${redis.hostname}")
	private String hostname;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.pool.maxActive}")
	private int maxTotal;
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	@Value("${redis.pool.maxWait}")
	private long maxWaitMillis;
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;

	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	@Autowired
	private JedisPoolWriper jedisWritePool;
	@Autowired
	private JedisUtil jedisUtil;

	/**
	 * 创建redis连接池的设置
	 * 
	 * @return
	 */
	@Bean(name = "jedisPoolConfig")
	public JedisPoolConfig createJedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		// 控制一个pool可分配多少个jedis实例
		jedisPoolConfig.setMaxTotal(maxTotal);
		// 连接池中最多可空闲maxIdle个连接，这里取值为 20
		jedisPoolConfig.setMaxIdle(maxIdle);
		// 最大等待时间 当前没有可用链接时
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		// 在获取连接的时候检查有效性
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		return jedisPoolConfig;
	}

	/**
	 * 创建链接池对象
	 * 
	 * @return
	 */
	@Bean(name = "jedisWritePool")
	public JedisPoolWriper createJedisPoolWriper() {
		JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
		return jedisPoolWriper;
	}

	/**
	 * 创建redis工具类
	 * 
	 * @return
	 */
	@Bean(name = "jedisUtil")
	public JedisUtil createJedisUtil() {
		JedisUtil jedisUtil = new JedisUtil();
		jedisUtil.setJedisPool(jedisWritePool);
		return jedisUtil;
	}

	/**
	 * redis的key操作
	 * 
	 * @return
	 */
	@Bean(name = "jedisKeys")
	public JedisUtil.Keys createJedisKeys() {
		JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
		return jedisKeys;
	}
	
	/**
	 * redis的String操作
	 * 
	 * @return
	 */
	@Bean(name = "jedisStrings")
	public JedisUtil.Strings createJedisStrings() {
		JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
		return jedisStrings;
	}
}
