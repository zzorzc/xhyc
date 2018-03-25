package com.poi.readServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class PoiRead extends HttpServlet{
	private String filePath;
	private Connection conn;
	private PreparedStatement ptmt;
	
	/**/
	private static final String URL="jdbc:mysql://127.0.0.1:3306/xhyc";
	private static final String USER="root";
	private static final String PASSWORD="1qaz!QAZ";
	private static final String INSERTSQL="" +
			"insert into glass_order(order_date, customer_name, glass_desc, length, width, count, finishCnt) " +
			                 "values(?,?,?,?,?,?,?)";
	public void conDb(PrintWriter out) throws Exception
	{
		/* 1.加载驱动程序 */
		Class.forName("com.mysql.jdbc.Driver");
		/* 2.获得数据库的连接 */
		this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
		/* 2.通过数据库的连接操作数据库，实现增删改查 */
		//Statement statement = conn.createStatement();
		/* ResultSet rs = statement.executeQuery("SELECT * FROM glass_order");
		while (rs.next()){
			out.println(" "+ rs.getString("order_date")+" "+ rs.getString("customer_name"));
		}*/
		ptmt = conn.prepareStatement(INSERTSQL);
	}
	
	public void insertDb(java.util.Date javaDate, String name, String desc, 
						int length, int width, int count) throws  Exception
	{
		ptmt.setDate(1, new Date(javaDate.getTime()));
		ptmt.setString(2, name);
		ptmt.setString(3, desc);
		ptmt.setInt(4, length);
		ptmt.setInt(5, width);
		ptmt.setInt(6, count);
		ptmt.setInt(7, 0);
		ptmt.execute();
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=gb2312");

		/* 判断字节流是否为空 */
		if (null != request.getParameter("file").getBytes("ISO-8859-1"))
		{
			filePath = new String(request.getParameter("file").getBytes("ISO-8859-1"), "gb2312");
		}else
		{
			return;
		}
		
		PrintWriter out = response.getWriter();
		out.println("filePath："+filePath+"<br>");
		try {
			conDb(out);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//Poi Read
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
			
			/* 创建工作簿 */
			HSSFWorkbook workBook = new HSSFWorkbook(fs);
			/* 获得Excel中工作表个数 */
			int sheetNum = workBook.getNumberOfSheets();
			out.println("工作表个数："+sheetNum+"<br>");
			/* 循环遍历Sheet */
			String name, lastName = "";
			String desc, lastDesc = "";
			int length = 0;
			int width = 0;
			int count = 0;
			
			for (int i = 0; i < sheetNum; i++)
			{
				HSSFSheet sheet = workBook.getSheetAt(i);
				/* 循环遍历row */
				sheet.getMargin(HSSFSheet.TopMargin);
				int fisrtRowNum = sheet.getFirstRowNum();
				int lastRowNum = sheet.getLastRowNum();

				if (fisrtRowNum == lastRowNum)
				{
					/* 空表单 */
					continue;
				}

				/* 获取最后一行的date */
				HSSFRow lastRow = sheet.getRow(lastRowNum);
				HSSFCell cell = lastRow.getCell(0);
				java.util.Date javaDate = new java.util.Date();
				if (null != cell && HSSFDateUtil.isCellDateFormatted(cell)) {
					/* 如果是date类型则 ，获取该cell的date值 */
					javaDate = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
					
				}
				out.println("表单日期： " + javaDate.toString() + "<br>");
				/* 遍历所有行 */
				for (int j = fisrtRowNum; j < lastRowNum; j++) {
					HSSFRow row = sheet.getRow(j);
					if (null != row) {
						/* 解析一行数据 */
						name = row.getCell(0).getStringCellValue();
						desc = row.getCell(1).getStringCellValue();
						length = (int) row.getCell(2).getNumericCellValue();
						width = (int) row.getCell(3).getNumericCellValue();
						count = (int) row.getCell(4).getNumericCellValue();

						/* length/width/count不可为空 */
						if (0 == length || 0 == width || 0 == count) {
							length = 0;
							width = 0;
							count = 0;
							continue;
						}

						if (name.equals("")) {
							name = lastName;
						}

						if (desc.equals("")) {
							desc = lastDesc;
						}

						/* 写DB */
						out.println("" + name + " " + desc + " " + length + " " + width + " " + count+"<br>");
						insertDb(javaDate, name, desc, length, width, count);
						lastName = name;
						lastDesc = desc;
						name = "";
						desc = "";
						length = 0;
						width = 0;
						count = 0;
					}
				}
				workBook.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        //out.print("<script>alert('解析完毕');</script>");
        out.flush();
        out.close();
	}


}
