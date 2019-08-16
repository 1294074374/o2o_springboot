package com.imooc.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolWriper {
	/** Redis 连接池对象 */
	private JedisPool jedisPool;

	/**
	 * 
	 * @param poolConfig 连接池对象
	 * @param host       主机
	 * @param port       端口
	 */
	public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host, final int port) {
		try {
			jedisPool = new JedisPool(poolConfig, host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Redis连接池对象
	 * 
	 * @return
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	/**
	 * 注入Redis连接池对象
	 * 
	 * @param jedisPool
	 */
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}
