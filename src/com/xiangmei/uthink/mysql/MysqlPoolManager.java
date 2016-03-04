package com.xiangmei.uthink.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author 
 */
@SuppressWarnings("rawtypes")
public class MysqlPoolManager {

    private static final Logger logger = Logger.getLogger(MysqlPoolManager.class);
    // static private MysqlConnectionManger instance = new
    // MysqlConnectionManger();// 唯一数据库连接池管理实例类
    static private MysqlPoolManager instance;

    private Hashtable pools = new Hashtable();// 连接池

    /**
     * 实例化管理类
     */
    private MysqlPoolManager() {
        this.init();
    }

    /**
     * 得到唯一实例管理类
     * 
     * @return
     */
    public static MysqlPoolManager getInstance() {
        if (instance == null) {
            instance = new MysqlPoolManager();
        }
        return instance;
    }

    /**
     * 释放连接
     * 
     * @param name
     * @param con
     */
    // public void freeConnection(String name, Connection con) {
    // DBConnectionPool pool = (DBConnectionPool) pools.get(name);// 根据关键名字得到连接池
    // if (pool != null)
    // pool.freeConnection(con);// 释放连接
    // }

    public void freeConnection(String name, Connection con) {
        try {
        	if(con!=null){
                con.close();
        	}
            logger.info("归还连接");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            logger.info("freeConnection" + e, e);
            e.printStackTrace();
        }// 释放连接

    }

    /**
     * 得到一个连接根据连接池的名字name
     * 
     * @param name
     * @return
     */
    public Connection getConnection(String name) throws Exception {
        HikariDataSource connectionPool = null;
        Connection con = null;
        connectionPool = (HikariDataSource) pools.get(name);// 从名字中获取连接池
        
        try {
        	int count = 1;
            while ((con = connectionPool.getConnection()) == null) {
            	count++;
            	
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    // TODO: handle exception
                    logger.error("getConnection 得不到数据库连接，等待出错 " + e, e);
                }
                
                if(count>4){
                	break;
                }
            }
            
            if(con == null){
            	throw new Exception("尝试获取链接失败,尝试获取5次");
            }else{
            	 logger.info("得到连接。。。");
            	 return con;
            }
        } catch (SQLException e) {
            logger.error("getConnection SQLException " + e, e);
            throw e;
        }// 从选定的连接池中获得连接
    }

    /**
     * 释放所有连接
     */
    public synchronized void release() {
        Enumeration allpools = pools.elements();
        while (allpools.hasMoreElements()) {
            HikariDataSource pool = (HikariDataSource) allpools.nextElement();
            if (pool != null)
                pool.shutdown();
        }
        pools.clear();
    }

    /**
     * 创建连接池
     * 
     * @param props
     */
    
	private void createPools() {
		logger.info("MysqlPoolManager:创建mysql数据连接池");
		long start_time = System.currentTimeMillis();
		try {

			HikariConfig config = new HikariConfig();
			config.setMaximumPoolSize(50);
			String name = "mysql";
			config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
			config.addDataSourceProperty("serverName", "192.168.1.1");
			config.addDataSourceProperty("port", "3306");
			config.addDataSourceProperty("databaseName", "test");
			config.addDataSourceProperty("user", "test");
			config.addDataSourceProperty("password", "test");
			String connectionTestQuery = "select 1 from user";
			config.addDataSourceProperty("useUnicode", "true");
			config.addDataSourceProperty("characterEncoding", "utf8");

			config.setConnectionTestQuery(connectionTestQuery);
			HikariDataSource ds = new HikariDataSource(config);

			pools.put(name, ds);
			logger.info("MysqlPoolManager:mysql数据连接池创建完成");

		} catch (Exception e) {
			logger.error("创建连接池失败!", e);
		}
	}

    /**
     * 初始化连接池的参数
     */
    private void init() {
        logger.info("创建数据库连接池。。。");
        createPools();
        logger.info("创建数据库连接池完毕。。。");
    }

    public static void main(String[] args) {
    	
    	System.out.println("begin");

         Connection conn = null;
         ResultSet rs = null;

         String dbname = "acmp";
         String NO ="2012001";
         try {
                  
            conn = MysqlPoolManager.getInstance().getConnection(dbname);             
            StringBuffer sb1 = new StringBuffer();
            sb1.append("SELECT name FROM student WHERE NO ='2012001'");
			String tsql = sb1 .toString();
            logger.info("tsql1 " + tsql);

            Statement st = null;
            st = conn.createStatement();
            long t_start_time1 = System.currentTimeMillis();
            rs = st.executeQuery(tsql);
            while (rs.next()) {
            String name = rs.getString("name");
            logger.info("姓名:"+name+",编号为 " + NO);
                }


        } catch (SQLException e) {
            logger.error("getUserLevel" + e, e);
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
            try {
                if (null != rs) {
                    rs.close();
                }
                MysqlPoolManager.getInstance().freeConnection(dbname, conn);
            } catch (Exception e2) {
                //logger.error(e2);
                e2.printStackTrace();
            }
        }
        
        System.out.println("end");


    }


    
    
}