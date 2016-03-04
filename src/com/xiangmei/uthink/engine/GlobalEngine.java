package com.xiangmei.uthink.engine;

import java.io.File;

import com.xiangmei.uthink.analysis.Consume;
import com.xiangmei.uthink.analysis.Output;

public class GlobalEngine {

	
	public static void main(String[] args) {
		Consume consume = new Consume();
		consume.saleRedis();
		StringBuffer sb = consume.memberConsumeInfo();
		File file = new File("d:\\GlobalConsume.txt");
		Output.outputAlarm(file, sb, false);
		consume.freeJedis();
	}
}
