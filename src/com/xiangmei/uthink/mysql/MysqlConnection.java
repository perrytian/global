package com.xiangmei.uthink.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * 数据库操作类
 * @author tianpengyu
 *
 */
public class MysqlConnection {
	
	//local mysql
	private static String dbName = "test";
	private static String host = "127.0.0.1";
	private static String port = "3306";
	private static String username = "root";
	private static String password = "123456";
	
	//JDBC url
	private static String url = "jdbc:mysql://"+host+":"+port+"/"+dbName;
	
	/**
	 * 
	 * 获取MySQL数据库连接
	 * @author tianpengyu
	 * @param request 请求
	 * 
	 */
	public static Connection getConnection(){
		Connection conn = null;
		try {
			//加载mysql驱动
			Class.forName("com.mysql.jdbc.Driver");
			//获取数据库连接
			conn = DriverManager.getConnection(url,username,password);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 释放JDBC资源
	 * @author tianpengyu
	 * @param conn 数据库连接
	 * @param ps 
	 * @param rs 记录集
	 */
	public static void releaseResources(Connection conn,Statement statement,ResultSet rs){
		try {
			if(null != rs){
				rs.close();
			}else if (null != statement) {
				statement.close();
			}else if(null != conn){
				conn.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
