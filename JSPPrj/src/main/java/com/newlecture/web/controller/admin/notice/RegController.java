package com.newlecture.web.controller.admin.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import org.xml.sax.InputSource;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig(
		fileSizeThreshold = 1024*1024,
		maxFileSize = 1024*1024*50,
		maxRequestSize = 1024*1024*50*5
)
@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String isOpen = request.getParameter("open");
		boolean pub = false;

		if (isOpen != null) {
			pub = true;
		}
		
		String realPath = request.getServletContext().getRealPath("/upload");
		File file = new File(realPath);
		if (!file.exists())
			file.mkdirs();
		
		StringBuilder builder = new StringBuilder();
		Collection<Part> parts = request.getParts();
		for (Part part : parts) {
			if (!part.getName().equals("file"))
				continue ;
			if (part.getSize() == 0)
				continue ;
				
			System.out.println("hello");
			String fileName = part.getSubmittedFileName();
			String filePath = realPath + File.separator + fileName;
			
			InputStream fis = part.getInputStream();
			FileOutputStream fos = new FileOutputStream(filePath);
			
			byte[] byteArr = new byte[1024];
			int size = 0;
			while (true) {
				size = fis.read(byteArr);
				if (size == -1)
					break ;
				fos.write(byteArr, 0, size);
			}
			
			builder.append(fileName);
			builder.append(",");
			
			fos.close();
			fis.close();
		}
		if (builder.length() != 0)
			builder.delete(builder.length() - 1, builder.length());
		
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setPub(pub);
		notice.setWriterId("newlect");
		notice.setFiles(builder.toString());
		
		NoticeService service = new NoticeService();
		service.insertNotice(notice); 
		
		response.sendRedirect("list");
	}
}