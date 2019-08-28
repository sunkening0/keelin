package com.skn.keelin.rabbitmq.single;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布/订阅模式(交换机类型fanout广播)
* 类名称：RabbitmqConf   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月28日 上午10:58:27   
* @version
 */
@Configuration
public class RabbitmqConf {

	//队列名1
	public static String FANOUT_SMS_QUEUE1="fanout_sms_queue1"; 
	//队列名2
	public static String FANOUT_SMS_QUEUE2="fanout_sms_queue2"; 
	

	
	//创建队列1
    @Bean
    public Queue fanoutSmsQueue1(){
        return new Queue(FANOUT_SMS_QUEUE1) ;
    }
    
    //创建队列2
    @Bean
    public Queue fanoutSmsQueue2(){
        return new Queue(FANOUT_SMS_QUEUE2) ;
    }

    //创建交换机（广播模式）
    @Bean
    public FanoutExchange fanoutExchange1(){
        return new FanoutExchange("fanoutExchange1");
    }
    
    
    
    //队列与交换机进行绑定
    @Bean
    Binding bindingSms1(){    	
        return BindingBuilder.bind(fanoutSmsQueue1()).to(fanoutExchange1());
    }
    
    @Bean
    Binding bindingSms2(){
        return BindingBuilder.bind(fanoutSmsQueue2()).to(fanoutExchange1());
    }
    
    
    
  //创建交换机（topic模式）
	public static String TOPIC_QUEUE1="topic_queue1"; 
	public static String TOPIC1="topic1"; 
    @Bean
    public TopicExchange topicExchange1(){
        return new TopicExchange("topicExchange1");
    }
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1) ;
    }
    @Bean
    Binding bindingTopic1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange1()).with(TOPIC1);
    }
  
}
