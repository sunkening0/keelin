package com.skn.keelin.rabbitmq.single;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.skn.keelin.rabbitmq.single.demo.ProduceService;
import com.skn.keelin.rabbitmq.single.fanout.FanoutProducer;
import com.skn.keelin.rabbitmq.single.topic.TopicProducer;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class TicketController {

	@Autowired
	private FanoutProducer fanoutProducer;
	@Autowired
	private TopicProducer topicProducer;
	@Autowired
	private ProduceService produceService;

	@GetMapping("/getTicket")
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String getTicket(Integer userID, String phone) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userID", userID);
		jsonObject.put("phone", phone);
		fanoutProducer.send(jsonObject.toJSONString());
		return "发送消息成功！";
	}
	
	@GetMapping("/getTicket1")
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String getTicket1(Integer userID, String phone) {
		/*JSONObject jsonObject = new JSONObject();
		jsonObject.put("userID", userID);
		jsonObject.put("phone", phone);*/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userID", userID);
		map.put("phone", phone);
		//topicProducer.send(jsonObject.toJSONString());
		topicProducer.send(map);
		return "发送消息成功！";
	}
	
	@GetMapping("/getTicket2")
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String getTicket2(Integer userID, String phone) {
		/*JSONObject jsonObject = new JSONObject();
		jsonObject.put("userID", userID);
		jsonObject.put("phone", phone);*/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userID", userID);
		map.put("phone", phone);
		//topicProducer.send(jsonObject.toJSONString());
		produceService.send(map);
		return "发送消息成功！";
	}

}