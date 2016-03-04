package com.xiangmei.uthink.task;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiangmei.uthink.mysql.MysqlService;
import com.xiangmei.uthink.object.Customer;
import com.xiangmei.uthink.util.SmsUtil;


public class BirthdayTask {
	
	private static Logger logger = LoggerFactory.getLogger(BirthdayTask.class); 

	public void start() throws Exception{
		logger.info("开始执行SMS定时任务");
		sendBirthdayMessage();
	}
	
	public void sendBirthdayMessage(){
		try {
			List<Customer> list = MysqlService.birthdayInfo();
			for (int i = 0; i < list.size(); i++) {
				Customer cust = list.get(i);
				logger.info("发送生日短信息：" + i + " " + cust.getName() + " " + cust.getNumber() + " " + cust.getPhone() + " "
						+ cust.getBirthday());
				SmsUtil.sendSMS(cust.getPhone());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
