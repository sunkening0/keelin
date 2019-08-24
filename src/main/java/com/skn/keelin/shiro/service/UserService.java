package com.skn.keelin.shiro.service;

import org.springframework.stereotype.Service;

import com.skn.keelin.shiro.entity.User;

@Service
public class UserService {
	
	public User getUser(String username){
		if("skn".equals(username)){
			User user = new User();
			user.setId(1);
			user.setUsername("skn");
			user.setPassword("d24d72990c6cede945338bffa76ff493");  //md5加盐   skn12300
			user.setAddress("重庆");
			return user;
		}else{
			return null;
		}
		
	}
	
	public User selectByPhone(String phone){
		if("skn".equals(phone)){
			User user = new User();
			user.setId(1);
			user.setUsername("skn");
			user.setPassword("d24d72990c6cede945338bffa76ff493");
			user.setAddress("重庆");
			return user;
		}else{
			return null;
		}
	}
}
