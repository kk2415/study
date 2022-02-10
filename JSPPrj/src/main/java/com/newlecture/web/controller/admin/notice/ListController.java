package com.newlecture.web.controller.admin.notice;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/board/notice/list")
public class ListController extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		NoticeService service = new NoticeService();
		String[] openIds = request.getParameterValues("open-id");
		String[] delIds = request.getParameterValues("del-id");
		String cmd = request.getParameter("cmd");
		String allIds_ = request.getParameter("ids");
		String[] allIds = allIds_.trim().split(" ");
		
		switch(cmd) {
		case "ÀÏ°ý°ø°³":
			List<String> listOpenIds = new ArrayList(Arrays.asList(openIds));
			List<String> listAllIds = new ArrayList(Arrays.asList(allIds));
			List<String> listCloseIds = new ArrayList();
			
			listAllIds.removeAll(listOpenIds);
			listCloseIds = List.copyOf(listAllIds);
			
			
			service.pubNoticeAll(listOpenIds, listCloseIds);
			break;
		case "ÀÏ°ý»èÁ¦":
			int[] ids = new int[delIds.length];
			for (int i = 0; i < delIds.length; i++) {
				ids[i] = Integer.parseInt(delIds[i]);
			}
			int result = service.deleteNoticeAll(ids);
			break;
		}
		response.sendRedirect("list");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		NoticeService service = new NoticeService();
		String field_ = request.getParameter("f");
		String query_ = request.getParameter("q");
		String page_ = request.getParameter("p");
		
		int page = 1;
		String field = "title";
		String query = "";
		
		if (page_ != null && !page_.equals("")) {
			page = Integer.parseInt(page_);
		}
		if (field_ != null && !field_.equals("")) {
			field = field_;
		}
		if (query_ != null && !query_.equals("")) {
			query = query_;
		}
		
		List<NoticeView> list = service.getNoticeList(field, query, page);
		
		int count = service.getNoticeCount(field, query);
		
		request.setAttribute("list", list);
		request.setAttribute("count", count);
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/list.jsp").forward(request, response);
	}
}

