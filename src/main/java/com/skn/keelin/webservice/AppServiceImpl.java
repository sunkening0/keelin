package com.skn.keelin.webservice;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

//name暴露的服务名称, targetNamespace:命名空间,设置为接口的包名倒写(默认是本类包名倒写). endpointInterface接口地址
@WebService(name = "test" ,targetNamespace ="http://webservice.keelin.skn.com/" ,endpointInterface = "com.skn.keelin.webservice.AppService")
public class AppServiceImpl implements AppService {
	public String getUserName(String id) throws UnsupportedEncodingException {
		System.out.println("==========================="+id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "南风北巷");
        return jsonObject.toJSONString();
	}

	public Map<String, Object> getUser(String id) throws UnsupportedEncodingException {
		System.out.println("==========================="+id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("skn", "sunkening");
        return result;
	}

}
