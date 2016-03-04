package com.xiangmei.uthink.engine;

import org.apache.log4j.Logger;

import com.xiangmei.uthink.timer.QuartzManager;

public class UthinkEngine {

	private static Logger logger = Logger.getLogger(UthinkEngine.class); 
	public static void main(String[] args) {
		logger.info("添加SMS任务，每天九点执行");
		QuartzManager.addJob("BirthdaySMS", "com.xiangmei.uthink.timer.QuarztJob", "0 0 9 * * ? *");
		logger.info("添加销售分析任务,每天晚上23点执行");
		QuartzManager.addJob("SalesAnalysis", "com.xiangmei.uthink.timer.QuarztJob", "0 0 23 * * ? *");
	}
}
