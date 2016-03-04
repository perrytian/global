package com.xiangmei.uthink.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;


public class RedisClient {
	private static Logger logger = Logger.getLogger(RedisClient.class); 
	private Jedis jedis ;
	
	public RedisClient() {
		jedis = new Jedis("192.168.1.3",6379);
	}
	
	public void freeJedis() {
		if (jedis!=null) {
			jedis.close();
			jedis = null;
		}
	}

	/**
	 * 测试连接
	 * @return
	 */
	public String ping(){
		String pong = null;
		try {
			pong = jedis.ping();
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return pong;
		
	}
	
	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	public boolean exists(String key){
		boolean res = false;
		try {
			res = jedis.exists(key);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return res;
		
	}
	
	/**
	 * 判断hash结构key和field是否存在
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hexists(String key,String field){
		boolean res = false;
		try {
			res = jedis.hexists(key, field);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return res;
		
	}
	
	
	/**
	 * get操作
	 * @param key
	 * @return
	 */
	public String get(String key){
		String value = null;
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return value;
	}
	
	/**
	 * set操作
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key,String value){
		String res = null;
		try {
			res = jedis.set(key, value);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return res;
	}
	
	/**
	 * hset操作
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public long hset(String key,String field,String value){
		long res = 0;
		try {
			res = jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return res;
	}
	
	
	/**
	 * hget操作
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key,String field){
		String res = null;
		try {
			res = jedis.hget(key, field);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return res;
	}
	
	/**
	 * 获取hash结构key的所有field和value
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key){
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return map;
	}
	
	/**
	 * zadd操作
	 * @param key
	 * @param member
	 * @param score
	 * @return
	 */
	public long zadd(String key,String member,String score){
		long res = 0;
		try {
			res = jedis.zadd(key, Double.valueOf(score), member);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return res;
	}
	
	public double zscore(String key,String member){
		double res = 0;
		try {
			res = jedis.zscore(key, member);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return res;
	}
	
	public Set<String> zrevrange(String key,long start,long end){
		Set<String> set = null; 
		try {
			set = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return set;
	}
	
	public double incrByFloat(String key,String value){
		double res = 0;
		try {
			res = jedis.incrByFloat(key, Float.valueOf(value));
		} catch (Exception e) {
			logger.error("jedis操作失败", e);
		}
		return res;
	}
	
	public double hincrByFloat(String key,String field,String value){
		double res = 0;
		try {
			res = jedis.hincrByFloat(key, field, Float.valueOf(value));
		} catch (Exception e) {
			logger.error("jedis操作失败key["+key+"]field["+field+"]value["+value+"]", e);
		}
		return res;
	}
	
	public double zincrby(String key,String member,String score){
		double res = 0;
		try {
			res = jedis.zincrby(key, Double.valueOf(score), member);
		} catch (Exception e) {
			logger.error("jedis操作失败key["+key+"]member["+member+"]score["+score+"]", e);
		}
		return res;
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		RedisClient client = new RedisClient();
		//String res=client.set("tiantest", "nihao");
		String conn = client.ping();
		System.out.println("-->>"+conn);
		client.freeJedis();
		
	}
}
