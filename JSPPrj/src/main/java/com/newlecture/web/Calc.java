package com.newlecture.web;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/calc")
public class Calc extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		int xValue = 0;
		int yValue = 0;
		String xStr = request.getParameter("x");
		String yStr = request.getParameter("y");
		String op = request.getParameter("operator");
		
		if (xStr != null && !xStr.equals("")) {
			xValue = Integer.parseInt(xStr);
		}
		if (yStr != null && !yStr.equals("")) {
			yValue = Integer.parseInt(yStr);
		}
		
		int sum = 0;
		if (op.equals("+"))
			sum = xValue + yValue;
		else
			sum = xValue - yValue;
		out.print("°á°ú : " + sum);
	}
}
