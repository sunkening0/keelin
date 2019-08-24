package com.skn.keelin.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * springboot + logback
* 类名称：LogTest   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月24日 下午7:53:01   
* @version
 */
public class LogTest {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(LogTest.class);
		logger.info("info...");
		logger.error("error...");
	}
}
