package com.skn.keelin.shiro.config.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 
* 类名称：RedisSessionDAO   
* 类描述：   
* 创建人：skn   
* 创建时间：2019年8月26日 上午10:27:39   
* @version
 */
public class RedisSessionDAO extends AbstractSessionDAO{
	
	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
	private String keyPrefix;
	private int expire = 0;
	
	private RedisTemplate redisTemplate;
	
	public RedisSessionDAO(RedisTemplate redisTemplate) {
		this.keyPrefix = "shiro_redis_session:";
		this.redisTemplate = redisTemplate;
	}
	
	public void update(Session session) throws UnknownSessionException {
		// TODO Auto-generated method stub
		this.saveSession(session);
	}
	
	private String getKey(Serializable sessionId) {
		String preKey = this.keyPrefix + sessionId;
		return preKey;
	}
	
	private void saveSession(Session session) throws UnknownSessionException {
		if ((session == null) || (session.getId() == null)) {
			logger.error("session or session id is null");
			return;
		}
		String key = getKey(session.getId());
		/*byte[] value = SerializeUtils.serialize(session);*/
		//session.setTimeout(expire * 1000);
		redisTemplate.opsForValue().set(key, session);//this.redisManager.set(key, value, this.redisManager.getExpire());
	}
	
	
	public void delete(Session session) {
		if ((session == null) || (session.getId() == null)) {
			logger.error("session or session id is null");
			return;
		}
		redisTemplate.delete(getKey(session.getId()));//this.redisManager.del(getByteKey(session.getId()));
	}
	
	
	public Collection getActiveSessions() {
		// TODO Auto-generated method stub
		Set sessions = new HashSet();
		Set<String> keys = redisTemplate.keys(this.keyPrefix + "*");
		if ((keys != null) && (keys.size() > 0)) {
			for (String key : keys) {
				Session s = (Session) redisTemplate.opsForValue().get(key);// (Session) SerializeUtils.deserialize(this.redisManager.get(key));
				sessions.add(s);
			}
		}
		return sessions;
	}
	
	
	protected Serializable doCreate(Session session) {
		// TODO Auto-generated method stub
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		saveSession(session);
		return sessionId;
	}
	
	
	protected Session doReadSession(Serializable sessionId) {
		// TODO Auto-generated method stub
		if (sessionId == null) {
			logger.error("session id is null");
			return null;
		}
		Session s = (Session) redisTemplate.opsForValue().get(getKey(sessionId)) ;// SerializeUtils.deserialize(this.redisManager.get(getByteKey(sessionId)));
		return s;
	}
}
