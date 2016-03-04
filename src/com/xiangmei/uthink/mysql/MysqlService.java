package com.xiangmei.uthink.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiangmei.uthink.object.Customer;

public class MysqlService {
	
	private static Logger logger = LoggerFactory.getLogger(MysqlService.class);  
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static List<Customer> customersInfo(){
		List<Customer> customerList = new ArrayList<Customer>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			String customerSql = "select * from customer";
			conn = MysqlConnection.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(customerSql);
			while(rs.next()){
				Customer customer = new Customer();
				customer.setBirthday(rs.getString("BIRTHDAY"));
				customer.setEmail(rs.getString("EMAIL"));
				customer.setEndTime(rs.getString("END_TIME"));
				customer.setIndbTime(rs.getString("INDB_TIME"));
				customer.setName(rs.getString("NAME"));
				customer.setNumber(rs.getString("CARD_NUMBER"));
				customer.setPhone(rs.getString("PHONE"));
				customer.setSex(rs.getString("SEX"));
				customer.setStartTime(rs.getString("START_TIME"));
				customer.setState(rs.getString("USER_STATE"));
				customer.setType(rs.getString("MEMBERSHIP_TYPE"));
				customer.setUpdatePerson(rs.getString("UPDATE_PERSON"));
				customer.setUpdateTime(rs.getString("UPDATE_TIME"));
				customerList.add(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询客户信息出错",e);
		} finally {
			try {
				MysqlConnection.releaseResources(conn, stat, rs);
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error("释放数据库连接出错",e2);
			}
		}
		
		return customerList;
	}
	
	
	public static List<Customer> birthdayInfo(){
		List<Customer> customerList = new ArrayList<Customer>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			String customerSql = "SELECT NAME,NUMBER,PHONE,BIRTHDAY FROM user where MONTH(birthday) = MONTH(curdate()) and DAY(birthday) = DAY(curdate())";
			conn = MysqlConnection.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(customerSql);
			while(rs.next()){
				Customer customer = new Customer();
				customer.setName(rs.getString("NAME"));
				customer.setNumber(rs.getString("CARD_NUMBER"));
				customer.setPhone(rs.getString("PHONE"));
				customer.setBirthday(rs.getString("BIRTHDAY"));
				customerList.add(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询客户信息出错",e);
		} finally {
			try {
				MysqlConnection.releaseResources(conn, stat, rs);
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error("释放数据库连接出错",e2);
			}
		}
		
		return customerList;
	}
	
	public static JSONObject ItemNumberName(){
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			String customerSql = "SELECT ITEM_NUMBER,ITEM_NAME,SPECIFICATION FROM item";
			conn = MysqlConnection.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(customerSql);
			while(rs.next()){
				String itemNumber = rs.getString("ITEM_NUMBER");
				jsonObject.put(itemNumber+"name", rs.getString("ITEM_NAME"));
				jsonObject.put(itemNumber+"specification", rs.getString("SPECIFICATION"));
			}
			
		} catch (Exception e) {
			logger.info("获取货号名称异常",e);
		} finally {
			try {
				MysqlConnection.releaseResources(conn, stat, rs);
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error("释放数据库连接出错",e2);
			}
		}
		return jsonObject;
	}
	
	public static void main(String[] args) {
		List<Customer> list = MysqlService.birthdayInfo();
		for (int i = 0; i < list.size(); i++) {
			Customer cust = list.get(i);
			logger.info(i+" "+cust.getName()+" "+cust.getNumber()+" "+cust.getPhone()+" "+cust.getBirthday());
		}
	}
	
}
