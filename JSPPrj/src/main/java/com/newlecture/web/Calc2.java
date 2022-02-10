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

@WebServlet("/calc2")
public class Calc2 extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		ServletContext application = request.getServletContext();
		HttpSession session = request.getSession();
		
		String v_ = request.getParameter("v");
		String op = request.getParameter("operator");
		int sum = 0;
		int v = 0;
		
		if (!v_.equals("")) {
			v = Integer.parseInt(v_);
		}
		
		if (op.equals("=")) {
			//int prev = (Integer)application.getAttribute("value");
			//int prev = (Integer)session.getAttribute("value");
			//String operator = (String)application.getAttribute("op");
			int cur = v;
			int prev = 0;
			String operator = "";
			Cookie[] cookies = request.getCookies();
			
			for (Cookie c : cookies) {
				if (c.getName().equals("value")) {
					prev = Integer.parseInt(c.getValue());
					break ;
				}
			}
			for (Cookie c : cookies) {
				if (c.getName().equals("op")) {
					operator = c.getValue();
					break ;
				}
			}
			
			if (operator.equals("+"))
				sum = prev + cur;
			else
				sum = prev - cur;	
			out.print("°á°ú : " + sum);
		}
		else {
//			application.setAttribute("value", v);
//			application.setAttribute("op", op);	
//			session.setAttribute("value", v);
//			session.setAttribute("op", op);
			
			Cookie valueCookie = new Cookie("value", String.valueOf(v));
			Cookie opCookie = new Cookie("op", op);
			valueCookie.setMaxAge(24*60*60);
			valueCookie.setPath("/calc2");
			opCookie.setPath("/calc2");
			response.addCookie(valueCookie);
			response.addCookie(opCookie);
			response.sendRedirect("./calc2.html");
		}
	}
}
