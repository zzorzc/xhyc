package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.UserService;

public class UserRegister extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6282795429582008422L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		/* 接收客户端信息 */
		String username = request.getParameter("r_username");
		if (null == username)
		{
			return;
		}
		username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
		String password = request.getParameter("r_password");
		System.out.println(username + "--" + password);

		/* 新建服务对象 */
		UserService us = new UserService();
		
		/* 校验注册 */
		if (us.register(username, password)){
			System.out.print("Succss");
			request.getSession().setAttribute("username", username);
		}else{
			System.out.print("Failed");
		}
		
		/* 通告客户端结果 */
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print("用户名：" + username);
        out.print("密码：" + password);
        out.flush();
        out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
