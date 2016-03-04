package com.xiangmei.uthink.analysis;

import java.io.File;
import java.io.FileWriter;

public class Output {

	public static void outputAlarm(File file, StringBuffer sb,boolean iscontinue) {

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file, iscontinue);
			writer.write(sb.toString());
			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
