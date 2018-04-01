package com.service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.database.BaseDAO;

public class UserService {
	public Boolean login(String username, String password){
		/* 查询数据库，校验该用户 */
		String sql = "SELECT * FROM user WHERE username = '" + username + "' AND password ='" + password + "'";
		BaseDAO bd = BaseDAO.createInst();
		bd.connectDB();
		
		try {
			ResultSet rs = bd.excuteQuery(sql);
			if (rs.next()){
				bd.closeDB();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		bd.closeDB();	
		return false;
	}
	
	public Boolean register(String username, String password){
		
		String sql = "INSERT INTO user values('" + username + "','" + password + "')";
		BaseDAO bd = BaseDAO.createInst();
		bd.connectDB();
		
		int ret = bd.excuteUpdate(sql);
		bd.closeDB();
		return (ret != 0 ? true : false);
	}
	
	public ResultSet query(Date date, String name, int len, int width)
	{
		String sql = "SELECT * FROM glass_order WHERE ";
		if (date != null){
			sql = sql + "(order_date='" + date + "')";
		}
		if (name != null && name.length() != 0){
			sql = sql + "AND(customer_name='" + name + "')";
		}
		if (len != 0){
			sql = sql + "AND(length=" + len + ")";
		}
		if (width != 0){
			sql = sql + "AND(width=" + width + ")";
		}
		sql = sql + ";";
		System.out.println(sql);
		BaseDAO bd = BaseDAO.createInst();
		bd.connectDB();
	
		return bd.excuteQuery(sql);
	}
	
	/*
	 * return : 0 --- success, 1 --- fail
	 * */
	public int update(int id, int finishCnt){
		String sql = "UPDATE glass_order SET finishCnt='"+finishCnt+"' WHERE id='"+id+"';";
		System.out.println(sql);
		BaseDAO bd = BaseDAO.createInst();
		bd.connectDB();
		
		return ((bd.excuteUpdate(sql)!=0) ? 0 : 1);
	}
}
