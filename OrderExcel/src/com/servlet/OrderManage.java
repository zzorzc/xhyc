package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.UserService;

import net.sf.json.JSONObject;

public class OrderManage extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9172904385570672357L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//"id="+id+"&finishCnt="+finishCnt
	    int id = Integer.valueOf(request.getParameter("id"));
	    int finishCnt = Integer.valueOf(request.getParameter("finishCnt"));
	    
	    int ret = new UserService().update(id, finishCnt);
	    JSONObject jsonObj = new JSONObject();
	    jsonObj.put("RetCode", ret);
	    
		response.setContentType("application/json; charset=utf-8");/*客户端json截断*/
		response.setCharacterEncoding("UTF-8");/*中文乱码*/
					
		PrintWriter out = response.getWriter();
		System.out.println(jsonObj.toString());
		out.println(jsonObj.toString());
        out.flush();
        out.close();
	}
}
