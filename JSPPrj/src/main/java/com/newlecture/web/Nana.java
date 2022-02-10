package com.newlecture.web;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/hi")
public class Nana extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String urlParm;
		int cnt = 0;
		urlParm = request.getParameter("cnt");
		if (urlParm != null && !urlParm.equals("")) {
			cnt = Integer.parseInt(urlParm);
		}
		
		for(int i = 0; i < cnt; i++) {
			out.print(i + " hello <br />");
		}
	}
}
