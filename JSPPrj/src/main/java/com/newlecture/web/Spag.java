package com.newlecture.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/spag")
public class Spag extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		int num = 0;
		String num_ = request.getParameter("n");
		String result;
			
		if (num_ != null && !num_.equals("")) {
			num = Integer.parseInt(num_);
		}
		if (num % 2 == 0)
			result = "Â¦¼ö";
		else
			result = "È¦¼ö";
		
		String[] names = {"newlec", "dragon"};
		
		List list = new ArrayList();
		list.add("12312");
		list.add("aaa");
		
		Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("id", 1);
		notice.put("title", "ELÀº ÁÁ¾Æ¿ä");
		
		request.setAttribute("result", result);
		request.setAttribute("names", names);
		request.setAttribute("notice", notice);
		request.setAttribute("list", list);
		RequestDispatcher dispatcher = request.getRequestDispatcher("spag.jsp");
		dispatcher.forward(request, response);
	}
}
