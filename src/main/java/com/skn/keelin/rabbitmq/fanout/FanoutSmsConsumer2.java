package com.skn.keelin.rabbitmq.fanout;



import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FanoutSmsConsumer2 {

	@RabbitListener(queues = "fanout_sms_queue2")
	@RabbitHandler
	public void process(Message massage) throws UnsupportedEncodingException {
		String id = massage.getMessageProperties().getMessageId();
		String msg =new String( massage.getBody(),"UTF-8");
		log.info(id+">>>>>>>>>>"+msg);
		JSONObject jsonObject = JSONObject.parseObject(msg);
		Integer filmID = jsonObject.getInteger("userID");
		String nums = jsonObject.getString("phone");
		log.info("消费队列2==filmID="+filmID+">>>>>>>>>>>>>>>>>>>>>>>> nums="+nums);
	}
}