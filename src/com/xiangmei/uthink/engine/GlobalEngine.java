package com.xiangmei.uthink.engine;

import java.io.File;

import org.apache.log4j.Logger;

import com.xiangmei.uthink.analysis.Consume;
import com.xiangmei.uthink.analysis.Output;

public class GlobalEngine {

	private static final Logger logger = Logger.getLogger(GlobalEngine.class); 
	public static void main(String[] args) {
		try {
			System.out.println("start");
			long start = System.currentTimeMillis();
			Consume consume = new Consume();
			consume.saleRedis();
			StringBuffer sb = consume.memberConsumeInfo();
			File file = new File("d:\\GlobalConsume.txt");
			Output.outputAlarm(file, sb, false);
			consume.freeJedis();
			System.out.println("end -->> "+(System.currentTimeMillis()-start)+"ms");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
