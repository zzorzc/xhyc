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
		/* 1.������������ */
		Class.forName("com.mysql.jdbc.Driver");
		/* 2.������ݿ������ */
		this.conn = DriverManager.getConnection(URL, USER, PASSWORD);
		/* 2.ͨ�����ݿ�����Ӳ������ݿ⣬ʵ����ɾ�Ĳ� */
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

		/* �ж��ֽ����Ƿ�Ϊ�� */
		if (null != request.getParameter("file").getBytes("ISO-8859-1"))
		{
			filePath = new String(request.getParameter("file").getBytes("ISO-8859-1"), "gb2312");
		}else
		{
			return;
		}
		
		PrintWriter out = response.getWriter();
		out.println("filePath��"+filePath+"<br>");
		try {
			conDb(out);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//Poi Read
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
			
			/* ���������� */
			HSSFWorkbook workBook = new HSSFWorkbook(fs);
			/* ���Excel�й�������� */
			int sheetNum = workBook.getNumberOfSheets();
			out.println("�����������"+sheetNum+"<br>");
			/* ѭ������Sheet */
			String name, lastName = "";
			String desc, lastDesc = "";
			int length = 0;
			int width = 0;
			int count = 0;
			
			for (int i = 0; i < sheetNum; i++)
			{
				HSSFSheet sheet = workBook.getSheetAt(i);
				/* ѭ������row */
				sheet.getMargin(HSSFSheet.TopMargin);
				int fisrtRowNum = sheet.getFirstRowNum();
				int lastRowNum = sheet.getLastRowNum();

				if (fisrtRowNum == lastRowNum)
				{
					/* �ձ� */
					continue;
				}

				/* ��ȡ���һ�е�date */
				HSSFRow lastRow = sheet.getRow(lastRowNum);
				HSSFCell cell = lastRow.getCell(0);
				java.util.Date javaDate = new java.util.Date();
				if (null != cell && HSSFDateUtil.isCellDateFormatted(cell)) {
					/* �����date������ ����ȡ��cell��dateֵ */
					javaDate = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
					
				}
				out.println("�����ڣ� " + javaDate.toString() + "<br>");
				/* ���������� */
				for (int j = fisrtRowNum; j < lastRowNum; j++) {
					HSSFRow row = sheet.getRow(j);
					if (null != row) {
						/* ����һ������ */
						name = row.getCell(0).getStringCellValue();
						desc = row.getCell(1).getStringCellValue();
						length = (int) row.getCell(2).getNumericCellValue();
						width = (int) row.getCell(3).getNumericCellValue();
						count = (int) row.getCell(4).getNumericCellValue();

						/* length/width/count����Ϊ�� */
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

						/* дDB */
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
        //out.print("<script>alert('�������');</script>");
        out.flush();
        out.close();
	}


}
