package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDAO {

	/* ���ݿ����ӳ��� */
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "1qaz!QAZ";
	public static final String URL = "jdbc:mysql://127.0.0.1:3306/xhyc";
	
	/* ��̬��Ա��֧�ֵ�̬ģʽ */
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
	
	/* �������� */
	public void initDB(){
		try {
			Class.forName(DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* �������ݿ� */
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
	
	/* �ر����ݿ⣬�رն����ͷž�� */
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
	
	/* ��ѯ���� */
	public ResultSet excuteQuery(String sql){
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/* ��ɾ�Ĳ���*/
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
