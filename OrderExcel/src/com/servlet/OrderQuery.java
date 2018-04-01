package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.ResultSetMetaData;
import com.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OrderQuery extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5706668543008817175L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/* 根据订单日期/客户姓名/尺寸查询数据库返回数据 */
		String orderdate = request.getParameter("od");
		String condition = request.getParameter("cd");

		condition = new String(condition.getBytes("ISO-8859-1"), "UTF-8");
		System.out.println("\r\n Date: " + orderdate + ", condition: " + condition);
		
		/* 解析条件 */
		String name = "";
		int len = 0;
		int wid = 0;
		if (null != condition && condition.length() != 0) {
			String[] strArray = condition.split(" ");
			for (int i = 0; i < strArray.length && i < 3; i++){
				String str = strArray[i];
				if (isChinese(str)){
					name = str;
				}
				if (isNumeric(str)){
					if (len == 0){
						len = Integer.parseInt(str);
					}else{
						wid = Integer.parseInt(str);
					}			
				}	
			}
		}
		
		ResultSet rs = new UserService().query(java.sql.Date.valueOf(orderdate), 
				                               name, len, wid);
		
		try {
			JSONArray array = resultSet2JsonArray(rs);

			response.setContentType("application/json; charset=utf-8");/*客户端json截断*/
			response.setCharacterEncoding("UTF-8");/*中文乱码*/
						
			PrintWriter out = response.getWriter();
			System.out.println(array.toString());
			out.println(array.toString());
	        out.flush();
	        out.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONArray resultSet2JsonArray(ResultSet rs) throws SQLException{
		JSONArray array = new JSONArray();
		
		ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
		int colCount = metaData.getColumnCount();
		
		//遍历rs
		while (rs.next()){
			JSONObject jsonObj = new JSONObject();
			
			for (int i = 1;i <= colCount;i++){
				String colName = metaData.getColumnLabel(i);
				String value = rs.getString(colName);
				jsonObj.put(colName, value);
			}
			
			array.add(jsonObj);
		}
			
		return array;
	}
	
	/* 判断一个字符串是否含有中文 */ 
	public static boolean isChinese(String str) {
	    if (str == null) {
	    	return false;
	    }

	    for (char c : str.toCharArray()) {
	        if (c >= 0x4E00 &&  c <= 0x9FA5) {
	        	return true;
	        } 
	    }
	    
	    return false;
	}
	
	/* 判断一个字符串是否是数字 */
	public static boolean isNumeric(String str){  
		for (int i = 0; i < str.length(); i++) {
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
