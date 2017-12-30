package com.nj.nfhy.util.redisUtils;

import org.springframework.stereotype.Component;

@Component
public class RedisUtilImpl extends RedisBaseImpl{
	public static final String Msg_domain = RedisBaseImpl.TKBASE+"nfhy_";
	//天时间,默认redis时效为7天
	public static final int sec = 1000*60*60*24*7;
	
	
	public void set(String key,String value){
		key = Msg_domain+key;
		jedis.set(key, value);
		jedis.expire(key, sec);
	}
	
	public String get(String key){
		key = Msg_domain+key;
		return jedis.get(key);
	}
	
	public void set(String key,String value,int second){
		key = Msg_domain+key;
		jedis.set(key, value);
		jedis.expire(key, second);
	}
	
	public boolean deleteKey(String key){
		key = Msg_domain+key;
		return jedis.del(key);
	}

}
