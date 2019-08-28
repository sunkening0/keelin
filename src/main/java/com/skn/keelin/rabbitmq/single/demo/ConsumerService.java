package com.skn.keelin.rabbitmq.single.demo;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConsumerService {
	@RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
	public void consume(Message message, Channel channel) throws IOException {
		//Mail mail = MessageHelper.msgToObj(message, Mail.class);
		log.info("收到消息: {}", message.getBody());
		/*String msgId = mail.getMsgId();
		MsgLog msgLog = msgLogService.selectByMsgId(msgId);
		if (null == msgLog || msgLog.getStatus().equals(Constant.MsgLogStatus.CONSUMED_SUCCESS)) {// 消费幂等性
			log.info("重复消费, msgId: {}", msgId);
			return;
		}
		MessageProperties properties = message.getMessageProperties();*/
		//long tag = properties.getDeliveryTag();
		//boolean success = mailUtil.send(mail);
		boolean success = true;
		if (success) {//消费成功  返回确认
			//todo   更新数据库状态
			//channel.basicAck(deliveryTag, multiple);  第二个参数multiple表示是否批量确认
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);// 消费确认
		} else {//消费异常  
			//channel.basicNack(deliveryTag, multiple, requeue);  第三个参数requene表示是否将消息重新放回队列  true:放回  false：丢弃
			//使用channel.basicReject(deliveryTag, requeue);一次只能拒绝一条消息
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
		}
	}
	
	/*channel.basicAck(deliveryTag, multiple);
	consumer处理成功后，通知broker删除队列中的消息，如果设置multiple=true，表示支持批量确认机制以减少网络流量。
	例如：有值为5,6,7,8 deliveryTag的投递
	如果此时channel.basicAck(8, true);则表示前面未确认的5,6,7投递也一起确认处理完毕。
	如果此时channel.basicAck(8, false);则仅表示deliveryTag=8的消息已经成功处理。
	channel.basicNack(deliveryTag, multiple, requeue);
	consumer处理失败后，例如：有值为5,6,7,8 deliveryTag的投递。
	如果channel.basicNack(8, true, true);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息重新放回队列中。
	如果channel.basicNack(8, true, false);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息直接丢弃。
	如果channel.basicNack(8, false, true);表示deliveryTag=8的消息处理失败且将该消息重新放回队列。
	如果channel.basicNack(8, false, false);表示deliveryTag=8的消息处理失败且将该消息直接丢弃。
	channel.basicReject(deliveryTag, requeue);
	相比channel.basicNack，除了没有multiple批量确认机制之外，其他语义完全一样。
	如果channel.basicReject(8, true);表示deliveryTag=8的消息处理失败且将该消息重新放回队列。
	如果channel.basicReject(8, false);表示deliveryTag=8的消息处理失败且将该消息直接丢弃。*/

}
