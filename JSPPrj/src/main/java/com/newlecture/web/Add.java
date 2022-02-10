package com.newlecture.web;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/add")
public class Add extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		int xValue = 0;
		int yValue = 0;
		String xStr = request.getParameter("x");
		String yStr = request.getParameter("y");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if (xStr != null && !xStr.equals("")) {
			xValue = Integer.parseInt(xStr);
		}
		if (yStr != null && !yStr.equals("")) {
			yValue = Integer.parseInt(yStr);
		}
		
		int sum = xValue + yValue;
		out.print("°á°ú : " + sum);
	}
}
