package com.xiangmei.uthink.analysis;

import java.util.List;

import org.apache.log4j.Logger;

import com.xiangmei.uthink.mysql.MysqlService;
import com.xiangmei.uthink.object.SalesDetail;

public class SalesVolume {

	private static Logger logger = Logger.getLogger(SalesVolume.class);
	public static void main(String[] args) {
		MysqlService mysqlService = new MysqlService();
		List<SalesDetail> saledetail = mysqlService.getSalesRecordeDetail();
		mysqlService.salesVolumeMysql(saledetail);
	}
}
