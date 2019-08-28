package com.skn.keelin.rabbitmq.single.topic;

import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TopicProducer {
	@Autowired
    private RabbitTemplate rabbitTemplate;

    /*public void send(String msg){
    	log.info("生成者发布消息："+msg);
    	CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("topicExchange1","topic1",msg,correlationData);
    }*/
	
	public void send(Map<String, Object> map){
    	log.info("生成者发布消息："+map);
    	CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("topicExchange1","topic1",map,correlationData);
    }
}
