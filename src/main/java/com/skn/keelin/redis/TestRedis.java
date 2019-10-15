package com.skn.keelin.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skn.keelin.annotation.RepeatSubmitCheck;

@RestController
public class TestRedis {

	@Autowired
	RedisUtil redisUtil;
	
	@RepeatSubmitCheck
	@PostMapping("/redis")
	public String test(){
		redisUtil.set("hahhahah", "123123");
		return "success";
	}
}
