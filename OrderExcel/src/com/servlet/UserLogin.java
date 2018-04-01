package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.UserService;


public class UserLogin extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8500813110544674161L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/* 接收客户端信息 */
		String username = request.getParameter("username");
		if (null == username)
		{
			return;
		}
		username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
		String password = request.getParameter("password");
		System.out.println("\r\n"+ username + "--" + password);

		/* 新建服务对象 */
		UserService us = new UserService();
		
		/* 校验登陆 */
		if (us.login(username, password)){
			System.out.print("Succss");
			request.getSession().setAttribute("username", username);
			PrintWriter out = response.getWriter();
			out.print(username);
	        out.flush();
	        out.close();
	        
			/* 通告客户端结果 */
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
		}else{
			System.out.print("Failed");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
