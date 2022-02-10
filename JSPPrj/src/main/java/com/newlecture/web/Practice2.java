package com.newlecture.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@WebServlet("/practice2")
public class Practice2 extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String value = request.getParameter("value");
		String operator = request.getParameter("operator");
		String dot = request.getParameter("dot");
		Cookie expCookie = new Cookie("exp", "");
		
		Cookie cookie[] = request.getCookies();
		String exp = "";
		
		if (cookie != null) {
			for (Cookie c : cookie) {
				if (c.getName().equals("exp")) {
					exp = c.getValue();
					break ;
				}
			}
		}
		
		if (operator != null && operator.equals("=")) {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			try {
				exp = String.valueOf(engine.eval(exp));
			} catch (ScriptException e) {
				e.printStackTrace();
			}
		}
		else {
			exp += (value == null)?"":value;
			exp += (operator == null)?"":operator;
			exp += (dot == null)?"":dot;
		}
		expCookie.setValue(exp);
		if (operator != null && operator.equals("C"))
			expCookie.setMaxAge(0);
		response.addCookie(expCookie);
		response.sendRedirect("/practice");
	}
}
