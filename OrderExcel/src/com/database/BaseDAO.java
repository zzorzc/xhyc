package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDAO {

	/* 数据库连接常量 */
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "1qaz!QAZ";
	public static final String URL = "jdbc:mysql://127.0.0.1:3306/xhyc";
	
	/* 静态成员，支持单态模式 */
	private static BaseDAO baseDao = null;
	private Connection conn = null;
	private Statement stmt = null;
	
	private BaseDAO() {
	}
	
	public static BaseDAO createInst(){
		if (null == baseDao){
			baseDao = new BaseDAO();
			baseDao.initDB();
		}
		return baseDao;
	}
	
	/* 加载驱动 */
	public void initDB(){
		try {
			Class.forName(DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 连接数据库 */
	public void connectDB(){
		System.out.println("Connecting to database...");
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Connect to database successful.");
	}
	
	/* 关闭数据库，关闭对象，释放句柄 */
	public void closeDB(){
		System.out.println("Close connection to database...");
		try{
			stmt.close();
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Close connection successful.");
	}
	
	/* 查询操作 */
	public ResultSet excuteQuery(String sql){
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/* 增删改操作*/
	public int excuteUpdate(String sql){
		int ret = 0;
		try {
			ret = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
