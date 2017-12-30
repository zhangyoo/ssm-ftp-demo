/**
 * 
 */
package com.nj.nfhy.util.redisUtils;

import com.nj.nfhy.util.basicUtils.PropertyUtil;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * 非切片连接池，只支持单个redis服务端
 * @author 88386726 20171115
 *
 */
@Component("redisPool")
public class RedisPool {

	private JedisPoolConfig config = new JedisPoolConfig();
	private JedisPool pool;
	private Jedis redis;
	/**
	 * 
	 */
	public RedisPool() {
		super();
		PropertyUtil propertyUtil = new PropertyUtil();
		Properties properties = propertyUtil.getProperties();
		String ip = properties.getProperty("redis.ip");
		String password = properties.getProperty("redis.password");
		int maxActive = Integer.valueOf(properties.getProperty("redis.password"));
		int maxIdle = Integer.valueOf(properties.getProperty("redis.maxIdle"));
		int maxWaitMillis= Integer.valueOf(properties.getProperty("redis.maxWaitMillis"));
		Boolean testOnBorrow = Boolean.valueOf(properties.getProperty("redis.testOnBorrow"));
		//ip格式：127.0.0.1:6379
		String h[] = ip.split(":");
		// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  　　
		//如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
		config.setMaxActive(maxActive);
		//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
		config.setMaxIdle(maxIdle);
		//表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出 　　　 
		config.setMaxWait(maxWaitMillis);
		//表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
		config.setTestOnBorrow(testOnBorrow);
		pool =  new JedisPool(config, h[0].trim(), Integer.parseInt(h[1]), 1000, password);
	}

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

	public Jedis getRedis() {
		Jedis jedis = null;
		int count = 0;
		do {
			try {
				jedis = this.getPool().getResource();
			} catch (Exception e) {
				getPool().returnBrokenResource(jedis);
				e.printStackTrace();
			}
			count++;
		} while (jedis == null && count < 200);
		return jedis;
	}

	public void setRedis(Jedis redis) {
		this.redis = redis;
	}

}
