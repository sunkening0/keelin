package com.skn.keelin.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
* @ClassName: RedisConfig
* @Description: redis配置类
* @author skn
* @date 2019年8月7日
*
 */
@Configuration
public class RedisConfig {

	@Bean
	@SuppressWarnings("all")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

		// key采用String的序列化方式
		redisTemplate.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		// valuevalue采用jackson序列化方式
		/*redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value采用jackson序列化方式
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);*/
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	
	@Bean(name="stringRedisTemplate")
    @ConditionalOnMissingBean(StringRedisTemplate.class)  //此注解的作用  如果容器中没有RedisTemplate 那就注入    有就不注入了
    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
	}
}
