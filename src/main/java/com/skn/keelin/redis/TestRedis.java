package com.skn.keelin.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRedis {

	@Autowired
	RedisUtil redisUtil;
	@GetMapping("/redis")
	public String test(){
		redisUtil.set("sunkening", "123123");
		return "success";
	}
}
