package com.skn.keelin.rabbitmq.demo;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitConfig {
	@Autowired
	private CachingConnectionFactory connectionFactory;

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter());
		// 消息是否成功发送到Exchange
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (ack) {
				log.info("消息成功发送到Exchange");
				String msgId = correlationData.getId();
				// todo  更新数据库  消息投递状态
			} else {
				log.info("消息发送到Exchange失败, {}, cause: {}", correlationData, cause);
			}
		});
		// 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息,
		// 而不会触发回调
		rabbitTemplate.setMandatory(true);
		// 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
		rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
			log.info("消息从Exchange路由到Queue失败: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}",
					exchange, routingKey, replyCode, replyText, message);
		});
		return rabbitTemplate;
	}

	/**
	 * 作用：消息序列化  将对象直接转为json   rabbitmq默认的消息序列化方式是SimpleMessageConverter
	 * MessageConverter中有两个方法  toMessage用于将消息序列化后发送到rabbitmq服务器  fromMessage用于消费端取消息并反序列化
	 * @return
	 */
	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	// 发送邮件
	public static final String MAIL_QUEUE_NAME = "mail.queue";
	public static final String MAIL_EXCHANGE_NAME = "mail.exchange";
	public static final String MAIL_ROUTING_KEY_NAME = "mail.routing.key";

	@Bean
	public Queue mailQueue() {
		return new Queue(MAIL_QUEUE_NAME, true);
	}

	@Bean
	public DirectExchange mailExchange() {
		return new DirectExchange(MAIL_EXCHANGE_NAME, true, false);
	}

	@Bean
	public Binding mailBinding() {
		return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAIL_ROUTING_KEY_NAME);
	}

}
