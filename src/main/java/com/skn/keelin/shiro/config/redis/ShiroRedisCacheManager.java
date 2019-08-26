package com.skn.keelin.shiro.config.redis;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import com.skn.keelin.redis.RedisUtil;

import io.netty.util.internal.ObjectUtil;

/**
 * 使用Redis作为缓存需要shiro重写cache、cacheManager、SessionDAO
* 类名称：ShiroRedisCacheManager   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月26日 上午10:43:14   
* @version
 */
public class ShiroRedisCacheManager extends AbstractCacheManager {

    private RedisTemplate redisTemplate;

    public ShiroRedisCacheManager(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    //为了个性化配置redis存储时的key，我们选择了加前缀的方式，所以写了一个带名字及redis操作的构造函数的Cache类
    @Override
    protected Cache createCache(String name) throws CacheException {
        return new ShiroRedisCache(redisTemplate,name);
    }
    


}
