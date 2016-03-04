package com.xiangmei.uthink.redis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiangmei.uthink.analysis.Consume;

public class RedisService {
	
	private static Logger logger = Logger.getLogger(RedisService.class);
	private SimpleDateFormat sdfMinute = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
	private RedisClient client = null;
	public static String BUSINESS_VOLUME_KEY = "turnover:uthink";
	public static String USER_AMOUNT_KEY = "user:amount:uthink";
	
	public RedisService() {
		client = new RedisClient();
	}
	
	public void freeJedis(){
		if (client!=null) {
			client.freeJedis();
		}
	}
	
	/**
	 * 营业额统计
	 * @param saleTime
	 * @param retailPrice
	 */
	public void businessVolume(String saleTime,String retailPrice){
		try {
			//logger.info("营业额统计参数saleTime["+saleTime+"]retailPrice["+retailPrice+"]");
			double res = client.hincrByFloat(BUSINESS_VOLUME_KEY, dateFormat(saleTime), retailPrice);
			//logger.info("营业额统计结果:"+res);
		} catch (Exception e) {
			logger.error("营业额统计异常",e);
		}
	}
	/**
	 * 获取某一天的营业额
	 * @param date
	 * @return
	 */
	public String getBusinessVolume(String field){
		String res = null;
		try {
			//logger.info("获取某一天的营业额参数field["+field+"]");
			res = client.hget(BUSINESS_VOLUME_KEY, field);
			//logger.info("获取某一天的营业额结果:"+res);
		} catch (Exception e) {
			logger.error("获取某一天的营业额异常",e);
		}
		return res;
	}
	
	/**
	 * 用户消费金额按天统计
	 * @param saleTime
	 * @param retailPrice
	 * @param membershipID
	 */
	public void userAmountDay(String saleTime,String retailPrice,String membershipID){
		try {
			//logger.info("用户消费金额按天统计saleTime["+saleTime+"]retailPrice["+retailPrice+"]membershipID["+membershipID+"]");
			String key = "member:"+membershipID+":amount:uthink";
			double res = client.hincrByFloat(key, dateFormat(saleTime), retailPrice);
			//logger.info("用户消费金额按天统计结果:"+res+" key:"+key);
		} catch (Exception e) {
			logger.error("用户消费金额按天统计异常",e);
		}
	}
	
	/**
	 * 获取用户按天统计的消费额
	 * @param memberID
	 * @return
	 */
	public JSONObject getUserAmountDay(String membershipID){
		JSONObject jsonObject = new JSONObject();
		try {
			//logger.info("获取用户按天统计的消费额memberID["+membershipID+"]");
			String key = "member:"+membershipID+":amount:uthink";
			Map<String, String> map = client.hgetAll(key);
			jsonObject = (JSONObject) JSON.toJSON(map);
			//logger.info("获取用户按天统计的消费额结果json:"+jsonObject.toJSONString());
		} catch (Exception e) {
			logger.error("获取用户按天统计的消费额异常",e);
		}
		return jsonObject;
	}
	/**
	 * 用户消费金额排序统计
	 * @param retailPrice
	 * @param membershipID
	 */
	public void userAmountSort(String retailPrice,String membershipID){
		try {
			//logger.info("用户消费金额排序统计retailPrice["+retailPrice+"]membershipID["+membershipID+"]");
			String key = "member:"+membershipID+":amount:uthink";
			double res = client.zincrby(USER_AMOUNT_KEY, membershipID, retailPrice);
			//logger.info("用户消费金额排序统计结果:"+res+" key:"+key);
		} catch (Exception e) {
			logger.error("用户消费金额排序统计异常",e);
		}
	}
	
	/**
	 * 获得用户消费金额排序
	 * @return
	 */
	public Set<String> getUserAmountSort(){
		Set<String> set = new HashSet<String>();
		try {
			set = client.zrevrange(USER_AMOUNT_KEY, 0, -1);
			//logger.info("获得用户消费金额排序结果set:"+set.toString());
		} catch (Exception e) {
			logger.error("获得用户消费金额排序异常",e);
		}
		return set;
	}
	
	
	/**
	 * 用户消费水果类型统计
	 * @param itemNumber
	 * @param salesVolume
	 * @param membershipID
	 */
	public void userConsumeType(String itemNumber,String salesVolume,String membershipID){
		try {
			//logger.info("用户消费水果类型统计itemNumber["+itemNumber+"]salesVolume["+salesVolume+"]"+"]membershipID["+membershipID+"]");
			String key = "member:"+membershipID+":type:uthink";
			double res = client.zincrby(key, itemNumber, salesVolume);
			//logger.info("用户消费水果类型统计结果:"+res+" key:"+key);
		} catch (Exception e) {
			logger.error("用户消费水果类型统计异常",e);
		}
	}
	
	/**
	 * 获取用户的消费类型统计
	 * @param membershipID
	 * @return
	 */
	public Set<String> getUserConsumeType(String membershipID){
		Set<String> set = new HashSet<String>();
		try {
			String key = "member:"+membershipID+":type:uthink";
			set = client.zrevrange(key, 0, -1);
			//logger.info("获取用户的消费类型统计结果set:"+set.toString());
		} catch (Exception e) {
			logger.error("获取用户的消费类型统计异常",e);
		}
		return set;
	}
	
	/**
	 * 时间格式转换
	 * @param saleTime
	 * @return
	 */
	public String dateFormat(String saleTime){
		String dateStr = null;
		try {
			Date date = sdfMinute.parse(saleTime);
			dateStr = sdfDate.format(date);
			//logger.info("日期转换："+dateStr);
		} catch (Exception e) {
			logger.error("日期格式转换错误"+saleTime,e);
		}
		return dateStr;
	}
	
	
	public double zscore(String key,String member){
		double res = 0;
		try {
			res = client.zscore(key, member);
		} catch (Exception e) {
			logger.error("获取成员数值错误",e);
		}
		return res;
	}
	
	
	public void saleRedis(String member,String paid){
		try {
			client.zincrby(Consume.keyConsume, member, paid);
		} catch (Exception e) {
			logger.error("saleRedis exception:",e);
		}
		
	}
	
	public Set<String> memberConsume(){
		Set<String> set = null;
		try {
			set = client.zrevrange(Consume.keyConsume, 0, -1);
		} catch (Exception e) {
			logger.error("memberConsume exception:",e);
		}
		return set;
	}
	
	public static void main(String[] args) {
		RedisService redisService = new RedisService();
		System.out.println(redisService.dateFormat("2015/11/15 11:17"));
	}
}
