package com.skn.keelin.rabbitmq.fanout;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 生产者
* 类名称：FanoutProducer   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月28日 上午11:01:14   
* @version
 */
@Slf4j
@Component
public class FanoutProducer {
	@Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg){
    	log.info("生成者发布消息："+msg);
        rabbitTemplate.convertAndSend("fanoutExchange1","",msg);
    }
}
