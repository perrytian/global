package com.xiangmei.uthink.analysis;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.xiangmei.uthink.mysql.MysqlService;
import com.xiangmei.uthink.object.SaleRecord;
import com.xiangmei.uthink.redis.RedisService;

public class Consume {
	
	
	private static Logger logger = Logger.getLogger(Consume.class); 
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private RedisService redisService = null; 
	private MysqlService mysqlService = null;
	private DecimalFormat df = new DecimalFormat("#.00");
	public static String keyConsume = "global:month:201602:consume";
	
	public Consume() {
		redisService = new RedisService();
		mysqlService = new MysqlService();
	}
	
	
	public void freeJedis(){
		redisService.freeJedis();
	}
	
	
	public void saleRedis(){
		List<SaleRecord> saleList = mysqlService.getSalesRecorde();
		for (int i = 0; i < saleList.size(); i++) {
			SaleRecord saleRecord = saleList.get(i);
			redisService.saleRedis(saleRecord.getMember(), saleRecord.getPaid());
		}
	}
	
	public StringBuffer memberConsumeInfo(){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("环球果仓2月份会员消费统计").append("\n");
			sb.append("统计时间:").append(sdf.format(new Date())).append("\n");
			sb.append("统计说明:").append("每一行数据按照序号、会员卡号、消费总额、姓名和联系方式排序，中间以逗号分割。").append("\n");
			int i = 1;
			Set<String> comsume = redisService.memberConsume();
			for (String member : comsume) {
				JSONObject memberJson = mysqlService.getMemberInfo(member);
				sb.append(i).append(",")
				.append(member).append(",")
				.append(df.format(redisService.zscore(keyConsume, member))).append("元,")
				.append(memberJson.get("name")).append(",")
				.append(memberJson.get("mobile")).append("\n");
			}
			
		} catch (Exception e) {
			logger.error("member consume exception",e);
		}

		return sb;
		
	}
	
}
