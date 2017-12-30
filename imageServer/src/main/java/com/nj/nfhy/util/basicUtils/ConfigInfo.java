package com.nj.nfhy.util.basicUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***
 * properties配置类
 * @author 88386726
 *
 */
@Component("configInfo")
public class ConfigInfo {

//	@Value("${redis.ip}")
//	private String ip;

	@Value("#{configProperties['redis.ip']}")
	private String ip;

//	@Value("#{configProperties['redis.password']}")
//	private String password;

//	@Value("#{configProperties['redis.maxActive']}")
//	private int maxActive;
//
//	@Value("#{configProperties['redis.maxIdle']}")
//	private int maxIdle;
//
//	@Value("#{configProperties['redis.maxWaitMillis']}")
//	private int maxWaitMillis;
//
//	@Value("#{configProperties['redis.testOnBorrow']}")
//	private Boolean testOnBorrow;


	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

//	public String getPassword() {
//		return password;
//	}

//	public int getMaxActive() {
//		return maxActive;
//	}
//
//	public int getMaxdle() {
//		return maxIdle;
//	}
//
//	public int getMaxWaitMillis() {
//		return maxWaitMillis;
//	}
//
//	public Boolean getTestOnBorrow() {
//		return testOnBorrow;
//	}


}
