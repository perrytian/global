package com.xiangmei.uthink.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiangmei.uthink.object.Customer;
import com.xiangmei.uthink.object.SaleRecord;
import com.xiangmei.uthink.object.SalesDetail;

public class MysqlService {
	
	private static Logger logger = Logger.getLogger(MysqlService.class);  
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
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
	
	
	public List<SaleRecord> getSalesRecorde(){
		
		List<SaleRecord> saleList = new ArrayList<>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			String saleRecordeSql = "SELECT * FROM SALES_RECORDE";
			//String saleRecordeSql = "select * from sales_recorde where id = '1'";
			conn = MysqlPoolManager.getInstance().getConnection("mysql"); 
			stat = conn.createStatement();
			rs = stat.executeQuery(saleRecordeSql);
			while(rs.next()){
/*				String id = rs.getString("ID");
				String runningMember = rs.getString("RUNNING_MEMBER");
				Date dateTime = rs.getDate("DATE_TIME");
				String saleType = rs.getString("SALE_TYPE");
				String cashier = rs.getString("CASHIER");
				String member = rs.getString("MEMBER");
				String shoppingGuide = rs.getString("SHOPPING_GUIDE");
				String productInfo = rs.getString("PRODUCT_INFO");
				String productNumber = rs.getString("PRODUCT_NUMBER");
				String totalPrice = rs.getString("TOTAL_PRICE");
				String paid = rs.getString("PAID");
				String valueCard = rs.getString("VALUE_CARD");
				String cash = rs.getString("CASH");
				String unionPay = rs.getString("UNIONPAY");
				System.out.println("salerecorde-->id:"+id+
						" runningMember:"+runningMember+
						" dateTime:"+dateTime+
						" saleType:"+saleType+
						" cashier:"+cashier+
						" member:"+member+
						" shoppingGuide:"+shoppingGuide+
						" productInfo:"+productInfo+
						" productNumber:"+productNumber+
						" totalPrice:"+totalPrice+
						" paid:"+paid+
						" valueCard:"+valueCard+
						" cash:"+cash+
						" unionPay:"+unionPay
						);
				System.out.println(parseId(member));
				System.out.println(paid);
				
				getMemberInfo(parseId(member));*/
				String member = rs.getString("MEMBER");
				String paid = rs.getString("PAID");
				SaleRecord saleRecord = new SaleRecord();
				saleRecord.setMember(parseId(member));
				saleRecord.setPaid(paid);
				saleList.add(saleRecord);
				
			}
			
		} catch (Exception e) {
			logger.info("获取销售记录异常",e);
		} finally {
			try {
				MysqlPoolManager.getInstance().freeConnection("mysql", conn);
				
				if(null != rs){
					rs.close();
				}else if (null != stat) {
					stat.close();
				}else if(null != conn){
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error("释放数据库连接出错",e2);
			}
		}
		
		return saleList;
	}
	
	public JSONObject getMemberInfo(String member){
		
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			String memberSql = "select NAME,MOBILE from USER where USER_ID  = '"+member+"'";
			conn = MysqlPoolManager.getInstance().getConnection("mysql"); 
			stat = conn.createStatement();
			rs = stat.executeQuery(memberSql);
			while(rs.next()){
				String name = rs.getString("NAME");
				String mobile = rs.getString("MOBILE");
				jsonObject.put("name", name);
				jsonObject.put("mobile", mobile);
				
			}
			
		} catch (Exception e) {
			logger.info("获取会员信息异常",e);
		} finally {
			try {
                MysqlPoolManager.getInstance().freeConnection("mysql", conn);
				
				if(null != rs){
					rs.close();
				}else if (null != stat) {
					stat.close();
				}else if(null != conn){
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error("释放数据库连接出错",e2);
			}
		}
		
		return jsonObject;
	}
	
	
	private static String parseId(String member) {
		Pattern pattern = Pattern.compile("（(.*?)）"); // 中文括号
		Matcher matcher = pattern.matcher(member);
		if (matcher.find()) {
			// System.out.println(matcher.group(1));
			return matcher.group(1);
		}

		return null;
	}
	
	private static String parseProduct(String procuctInfo) {
		String result = null;
		try {
			if (null!=procuctInfo) {
				result = procuctInfo.substring(procuctInfo.lastIndexOf("(")+1,procuctInfo.lastIndexOf(")"));
			}else{
				logger.info("procuctInfo为空:"+procuctInfo);
			}
				
			
		} catch (Exception e) {
			logger.error("解析产品异常:"+result);
		}
		return result;

	}
	
	
	public List<SalesDetail> getSalesRecordeDetail(){
		List<SalesDetail> saleList = new ArrayList<>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			String saleRecordeSql = "SELECT * FROM sales_recorde_detail_03";
			//String saleRecordeSql = "select * from sales_recorde where id = '1'";
			conn = MysqlPoolManager.getInstance().getConnection("mysql"); 
			stat = conn.createStatement();
			rs = stat.executeQuery(saleRecordeSql);
			
			String temDate = null;
			while(rs.next()){
				
				String dateTime = rs.getString("DATE_TIME");

				String dateString;
				if(null==dateTime||"null".equals(dateTime)||dateTime.isEmpty()){
					dateString =temDate;
				}else{
					dateString = sdfDate.format(sdf.parse(dateTime));
					temDate = dateString;
				}
				String productInfo = rs.getString("PRODUCT_INFO");
				if("-".equals(productInfo) || "抹零".equals(productInfo)){
					continue;
				}
				String productCode = parseProduct(productInfo);
				if (null==productCode) {
					continue;
				}
				String productNumber = rs.getString("PRODUCT_NUMBER");
				SalesDetail volume = new SalesDetail();
				volume.setDateString(dateString);
				volume.setProduct(productCode);
				volume.setVolume(Float.parseFloat(productNumber));
				//System.out.println("销售详情:Product["+productCode+"]Volume["+productNumber+"]Date["+dateString+"]");
				saleList.add(volume);
			}
			
			System.out.println("saleList大小:"+saleList.size());
		} catch (Exception e) {
			logger.error("获取销售记录异常",e);
		} finally {
			try {
				MysqlPoolManager.getInstance().freeConnection("mysql", conn);
				
				if(null != rs){
					rs.close();
				}else if (null != stat) {
					stat.close();
				}else if(null != conn){
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error("释放数据库连接出错",e2);
			}
		}
		
		return saleList;
	}
	
	
	public void salesVolumeMysql(List<SalesDetail> salesVolume){
		
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = MysqlPoolManager.getInstance().getConnection("mysql"); 
			stat = conn.createStatement();
			for (int i = 0; i < salesVolume.size(); i++) {
				
				SalesDetail volume = salesVolume.get(i);
				String productExistSql = "select * from sales_volume where PRODUCT = '"+volume.getProduct()+"' AND SALES_DATE = '"+volume.getDateString()+"' ";
				//System.out.println("sql:"+productExistSql);
				rs = stat.executeQuery(productExistSql);
				if (rs.next()) {
					double addVolume = rs.getDouble("SALES_VOLUME")+volume.getVolume();
					String updateSql = "update sales_volume set SALES_VOLUME = '"+addVolume+"' WHERE PRODUCT = '"+volume.getProduct()+"' AND SALES_DATE = '"+volume.getDateString()+"'";
					int updateResult = stat.executeUpdate(updateSql);
					//System.out.println("update result:"+updateResult+"PRODUCT["+volume.getProduct()+"]VOLUME["+volume.getVolume()+"]DATE["+volume.getDateString()+"]");
				}else {
					String pruductInsert = "Insert into sales_volume(PRODUCT,SALES_VOLUME,SALES_DATE) VALUES('"+volume.getProduct()+"','"+volume.getVolume()+"','"+volume.getDateString()+"')";
					int insertResult =stat.executeUpdate(pruductInsert);
					//System.out.println("insert result:"+insertResult+"PRODUCT["+volume.getProduct()+"]VOLUME["+volume.getVolume()+"]DATE["+volume.getDateString()+"]");
				}
			}
			
		} catch (Exception e) {
			logger.info("获取销售记录异常",e);
		} finally {
			try {
				MysqlPoolManager.getInstance().freeConnection("mysql", conn);
				
				if(null != rs){
					rs.close();
				}else if (null != stat) {
					stat.close();
				}else if(null != conn){
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error("释放数据库连接出错",e2);
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		List<Customer> list = MysqlService.birthdayInfo();
		for (int i = 0; i < list.size(); i++) {
			Customer cust = list.get(i);
			logger.info(i+" "+cust.getName()+" "+cust.getNumber()+" "+cust.getPhone()+" "+cust.getBirthday());
		}
		
	}
	
}
