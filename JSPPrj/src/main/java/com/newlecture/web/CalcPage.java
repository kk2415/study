package com.newlecture.web;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/calcpage")
public class CalcPage extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String exp = "0";
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals("exp")) {
					exp = c.getValue();
					break ;
				}
			}	
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = response.getWriter();
		
		writer.write("<!DOCTYPE html>");
		writer.write("<html>");
		writer.write("<head>");
		writer.write("<meta charset=\"UTF-8\">");
		writer.write("<title>Insert title here</title>");
		writer.write("<style>");
		writer.write("input {");
		writer.write("width:50px;");
		writer.write("height:50px;");
		writer.write("}");
		writer.write(".output{");
		writer.write("height: 50px;");
		writer.write("background: #e9e9e9;");
		writer.write("font-size: 24px;");
		writer.write("font-weight: bold;");
		writer.write("text-align: right;");
		writer.write("padding: 0px 5px;");
		writer.write("}");
		writer.write("</style>");
		writer.write("</head>");
		writer.write("<body>");
		writer.write("<form action=\"calc3\" method=\"post\">");
		writer.write("<table>");
		writer.write("<tr>");
		writer.printf("<td class=\"output\" colspan=\"4\">%s</td>", exp);
		writer.write("</tr>");
		writer.write("<tr>");
		writer.write("<td><input type=\"submit\" name=\"operator\" value=\"CE\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"operator\" value=\"C\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"operator\" value=\"BS\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"operator\" value=\"/\" /></td>");
		writer.write("</tr>");
		writer.write("<tr>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"7\" /></td>"); 
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"8\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"9\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"operator\" value=\"*\" /></td>");
		writer.write("</tr>");
		writer.write("<tr>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"4\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"5\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"6\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"operator\" value=\"-\" /></td>");
		writer.write("</tr>");
		writer.write("<tr>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"1\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"2\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"3\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"operator\" value=\"+\" /></td>");
		writer.write("</tr>");
		writer.write("<tr>");
		writer.write("<td><input type=\"submit\" name=\"value\" value=\"0\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"dot\" value=\".\" /></td>");
		writer.write("<td><input type=\"submit\" name=\"operator\" value=\"=\" /></td>");
		writer.write("</tr>");
		writer.write("</table>");
		writer.write("</form>");
		writer.write("</body>");
		writer.write("</html>");
	}
}
