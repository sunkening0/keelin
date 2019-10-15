package com.skn.keelin.shiro.config.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.SerializationUtils;

import com.skn.keelin.redis.RedisUtil;

import io.netty.util.internal.ObjectUtil;

public class ShiroRedisCache<K,V> implements Cache<K,V> {
	
	private RedisTemplate redisTemplate;
	private String prefix = "shiro_redis";
	
	public ShiroRedisCache(RedisTemplate redisTemplate,String prefix ){
		this.redisTemplate = redisTemplate;
		this.prefix  = prefix;
	}

	public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

	public String getPrefix() {
        return prefix+":";
    }
	
    public ShiroRedisCache(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }


    
    public V get(K k) throws CacheException {
        if (k == null) {
            return null;
        }
        byte[] bytes = getBytesKey(k);
        return (V)redisTemplate.opsForValue().get(bytes.toString());

    }

    
    public V put(K k, V v) throws CacheException {
        if (k== null || v == null) {
            return null;
        }

        byte[] bytes = getBytesKey(k);
        redisTemplate.opsForValue().set(bytes, v);
        return v;
    }

    
    public V remove(K k) throws CacheException {
        if(k==null){
            return null;
        }
        byte[] bytes =getBytesKey(k);
        V v = (V)redisTemplate.opsForValue().get(bytes);
        redisTemplate.delete(bytes);
        return v;
    }

    
    public void clear() throws CacheException {
        redisTemplate.getConnectionFactory().getConnection().flushDb();

    }

    
    public int size() {
        return redisTemplate.getConnectionFactory().getConnection().dbSize().intValue();
    }

    
    public Set<K> keys() {
        byte[] bytes = (getPrefix()+"*").getBytes();
        Set<byte[]> keys = redisTemplate.keys(bytes);
        Set<K> sets = new HashSet<K>();
        for (byte[] key:keys) {
            sets.add((K)key);
        }
        return sets;
    }

    
    public Collection<V> values() {
        Set<K> keys = keys();
        List<V> values = new ArrayList<V>(keys.size());
        for(K k :keys){
            values.add(get(k));
        }
        return values;
    }

    private byte[] getBytesKey(K key){
        if(key instanceof String){
            String prekey = this.getPrefix() + key;
            return prekey.getBytes();
        }else {
            return SerializationUtils.serialize(key);
        }
    	/*if(key instanceof String){
    		return (String)key;
    	}
    	return null;*/
    }

}