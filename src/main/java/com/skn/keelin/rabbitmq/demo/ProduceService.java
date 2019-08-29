package com.skn.keelin.rabbitmq.demo;


import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * 生产服务 类名称：ProduceService 类描述： 创建人：skn 创建时间：2019年8月28日 下午4:44:12
 * 
 * @version
 */
@Slf4j
@Service
public class ProduceService {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	

	public void send(Map<String, Object> map) {
		String msgId = UUID.randomUUID().toString();
		//mail.setMsgId(msgId);
		//MsgLog msgLog = new MsgLog(msgId, mail, RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME);
		//msgLogMapper.insert(msgLog);// 消息入库
		//因为配置了  Jackson2JsonMessageConverter   所以消息在队列中是以json字符串的形式存在的
		CorrelationData correlationData = new CorrelationData(msgId);
		rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME,
				map, correlationData);// 发送消息
	}

}
