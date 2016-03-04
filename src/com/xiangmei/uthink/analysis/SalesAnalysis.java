package com.xiangmei.uthink.analysis;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.xiangmei.uthink.decoder.Decoder;
import com.xiangmei.uthink.decoder.impl.SalesDecoder;
import com.xiangmei.uthink.mysql.MysqlService;
import com.xiangmei.uthink.redis.RedisService;
import com.xiangmei.uthink.util.MathUtil;

public class SalesAnalysis {

	private static Logger logger = Logger.getLogger(SalesAnalysis.class); 
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private RedisService redisService = null; 
	//private String SALES_PATH = "/root/uthink/sales";
	private String SALES_PATH = "D:\\uthink";
	public SalesAnalysis() {
		redisService = new RedisService(); 
	}
	
	public void freeJedis(){
		redisService.freeJedis();
	}
	/**
	 * 读文件夹
	 */
	public void readFolder() {
		try {
			File file = new File(SALES_PATH);
			File[] filesList = file.listFiles();
			// 打印文件的所有行
			for (int i = 0; i < filesList.length; i++) {
				if (filesList[i].isFile()) {
					logger.info("文 件：" + filesList[i]);
					readFile(filesList[i]);
				}
				if (filesList[i].isDirectory()) {
					logger.info("文件夹：" + filesList[i]);
				}
			}

		} catch (Exception e) {
			logger.error("读文件夹异常",e);
		}
	}
	
	/**
	 * 读文件
	 * @param file
	 * @throws IOException
	 */
	public void readFile(File file) throws IOException{
		LineIterator it = FileUtils.lineIterator(file,"UTF-8");
		try{
			while(it.hasNext()){
				String line = it.nextLine();
				decodeData(line);
			}
			
		}catch(Exception e){
			logger.error("读取文件数据出错:",e);
		}finally{
			LineIterator.closeQuietly(it);
		}
	}
	
	/**
	 * 解析每一行数据，格式判断
	 * @param line
	 */
	public void decodeData(String line) {
		Decoder decoder = new SalesDecoder();
		try {
			decoder.setData(line);
			if (decoder.isValidity()) {
				analyseData(decoder);
			}
			
		} catch (Exception e) {
			logger.error("解析销售数据出错:", e);
		}
	}
	
	/**
	 * 分析每一行数据
	 * @param decoder
	 */
	public void analyseData(Decoder decoder){
		
		try {
			String saleTime = decoder.getSALE_TIME();//销售时间
			String membershipID = decoder.getMENBERSHIP_ID();//会员卡号
		    String itemNumber = decoder.getITEM_NUMBER();//货号
		    String salesVolume = decoder.getSALES_VOLUME();//数量
		    String retailPrice = decoder.getRETAIL_PRICE();//零售价
/*		    logger.info("销售数据:membershipID["+membershipID+
		    		"]salesItem["+itemNumber+
		    		"]salesVolume["+salesVolume+
		    		"]price["+retailPrice);*/
			if (null != membershipID && !membershipID.isEmpty()) {//会员卡结算的数据进行统计

				//每个用户按天统计消费金额
				redisService.userAmountDay(saleTime, retailPrice, membershipID);
				//用户消费金额排序统计
				redisService.userAmountSort(retailPrice, membershipID);
				//用户消费类型统计
				redisService.userConsumeType(itemNumber, salesVolume, membershipID);
			} else {
				//logger.info("非会员卡结算数据");
			}
			
			//销售额统计
			redisService.businessVolume(saleTime, retailPrice);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public StringBuffer Output(){
		StringBuffer sb = new StringBuffer();
		JSONObject jsonItem = MysqlService.ItemNumberName();
		try {
			Date date = new Date();
			String today = sdf.format(date);
			sb = new StringBuffer();
			sb.append("********************悠仙客销售数据分析********************\n");
			sb.append("统计时间:").append(today).append("\n");
			sb.append("统计说明:").append("用户消费金额按照从大到小排序,在排序的基础上再分析用户的购买类型").append("\n");
			Set<String> amountSortSet = redisService.getUserAmountSort();
			for (String membershipID : amountSortSet) {
				sb.append("\n");
				sb.append("会员卡号[").append(membershipID).append("] 消费金额[").append(MathUtil.round((redisService.zscore(redisService.USER_AMOUNT_KEY, membershipID)), 2, BigDecimal.ROUND_HALF_UP)).append("元]\n");
				Set<String> consumeType = redisService.getUserConsumeType(membershipID);
				for (String item : consumeType) {
					sb.append("货号[").append(item).append("] "
							+ " 名称[").append(jsonItem.get(item+"name")).append("] "
									+ " 消费数量[").append(MathUtil.round((redisService.zscore("member:"+membershipID+":type:uthink", item)), 2, BigDecimal.ROUND_HALF_UP)).append(jsonItem.get(item+"specification")).append("]\n");
				}
			}
			
			//logger.info(sb.toString());
		} catch (Exception e) {
			logger.info("统计结果输出异常",e);
		}
		return sb;
	}
}
