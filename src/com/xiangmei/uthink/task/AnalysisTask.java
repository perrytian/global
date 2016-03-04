package com.xiangmei.uthink.task;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiangmei.uthink.analysis.Output;
import com.xiangmei.uthink.analysis.SalesAnalysis;

public class AnalysisTask {
	
	private static Logger logger = LoggerFactory.getLogger(AnalysisTask.class); 
	SalesAnalysis salesAnalysis = new SalesAnalysis();
	
	public void start() throws Exception{
		logger.info("开始执行定时数据分析任务");
		analysis();
	}
	
	public void analysis(){
		salesAnalysis.readFolder();
		StringBuffer sbBuffer = salesAnalysis.Output();
		File file = new File("d:\\result.txt");
		Output.outputAlarm(file, sbBuffer, false);
	}
	
	public static void main(String[] args) {
		logger.info("开始统计");
		long start = System.currentTimeMillis();
		AnalysisTask analysisTask = new AnalysisTask();
		analysisTask.analysis();
		logger.info("统计用时:"+(System.currentTimeMillis()-start)+"ms");
	}
}
