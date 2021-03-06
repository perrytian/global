package com.xiangmei.uthink.timer;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.xiangmei.uthink.task.BirthdayTask;

public class QuarztJob implements Job {

	private static Logger logger = Logger.getLogger(QuarztJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("开始执行QuarztJob调用定时任务");
		try {
			new BirthdayTask().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}