package com.newlecture.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;

public class NoticeService {
	
	public int removeNoticeAll(int[] ids) {
		return 0;
	}

	public int pubNoticeAll(int[] oids, int[] cids) {
		List<String> oidsList = new ArrayList<String>();
		List<String> cidsList = new ArrayList<String>();
		
		for (int i = 0; i < oids.length; i++) {
			oidsList.add(String.valueOf(oids[i]));
		}
		
		for (int i = 0; i < cids.length; i++) {
			cidsList.add(String.valueOf(cids[i]));
		}
		return pubNoticeAll(oidsList, cidsList);
	}
	
	public int pubNoticeAll(List<String> oids, List<String> cids) {
		String oidsCSV = String.join(",", oids);
		String cidsCSV = String.join(",", cids);
		
		return pubNoticeAll(oidsCSV, cidsCSV);
	}

	public int pubNoticeAll(String oidsCSV, String cidsCSV) {
		String openSql = String.format("UPDATE NOTICE SET PUB = 1 WHERE ID IN (%s)", oidsCSV);
		String closeSql = String.format("UPDATE NOTICE SET PUB = 0 WHERE ID IN (%s)", cidsCSV);
		
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";
		
		int result = 0;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			Statement openSt = con.createStatement();
			Statement closeSt = con.createStatement();
			int openResult = openSt.executeUpdate(openSql);
			int closeResult = closeSt.executeUpdate(closeSql);
			
			openSt.close();
			closeSt.close();
			con.close();
			
			result = openResult & closeResult;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public int insertNotice(Notice notice) {
		String sql = "insert into notice (title, content, writer_id, pub, files) values (?,?,?,?,?)";
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";
		int result = 0;
		
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, notice.getTitle());
			st.setString(2, notice.getContent());
			st.setString(3, notice.getWriterId());
			st.setBoolean(4, notice.getPub());
			st.setString(5, notice.getFiles());
			result = st.executeUpdate();
		
			st.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int deleteNotice(int id) {
		return 0;
	}
	
	public int deleteNoticeAll(int[] ids) {
		String params = "";
		for (int i = 0; i < ids.length; i++) {
			params += ids[i];
			if (i < ids.length - 1) {
				params += ",";
			}
		}
		
		String sql = "DELETE NOTICE WHERE ID IN (" + params + ")";
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";
		int result = 0;
		Notice notice = null;
		
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			Statement st = con.createStatement();
			result = st.executeUpdate(sql);
		
			st.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int updateNotice(Notice notice) {
		return 0;
	}
	
	public List<Notice> getNoticeNewestList() {
		return null;
	}
	
	public List<NoticeView> getNoticeList() {
		return getNoticeList("title", "", 1);
	}
	
	public List<NoticeView> getNoticeList(int page) {
		return getNoticeList("title", "", page);
	}
	
	public List<NoticeView> getNoticeList(String field, String query, int page) {
		int firstPostId = 1+(page-1)*10; //1, 11, 21, 31 -> a1+(n-1)*d
		int lastPostId = page * 10; //10, 20, 30
		List<NoticeView> list = new ArrayList<NoticeView>();
		
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";

		
		String sql = "select * from ("
				+ "select rownum num, N.* from ("
				+ "select * from notice_view where " + field + " like ? order by regdate desc) N "
				+ ") "
				+ "where num between ? and ?";
		
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%" + query + "%");
			st.setInt(2, firstPostId);
			st.setInt(3, lastPostId);
			ResultSet rs = st.executeQuery();

			int	id = 0;
			String title = null;
			String writerId = null;
			String content = null;
			String files = null;
			Date regdate = null;
			int hit = 0;
			int cmtCount = 0;
			boolean pub = false;

			while (rs.next()) {
				id = rs.getInt("id");
				title = rs.getString("title");
				writerId = rs.getString("writer_id");
				files = rs.getString("files");
				regdate = rs.getDate("regdate");
				hit = rs.getInt("hit");
				cmtCount = rs.getInt("CNT");
				pub = rs.getBoolean("pub");
//				content = rs.getString("content");	
				
				NoticeView notice = new NoticeView(id, title, writerId, files, regdate, hit, pub, cmtCount);				
				list.add(notice);
			}

			rs.close();
			st.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<NoticeView> getNoticePubList(String field, String query, int page) {
		int firstPostId = 1+(page-1)*10; //1, 11, 21, 31 -> a1+(n-1)*d
		int lastPostId = page * 10; //10, 20, 30
		List<NoticeView> list = new ArrayList<NoticeView>();
		
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";

		
		String sql = "select * from ("
				+ "select rownum num, N.* from ("
				+ "select * from notice_view where " + field + " like ? order by regdate desc) N "
				+ ") "
				+ "where pub = 1 and num between ? and ?";
		
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%" + query + "%");
			st.setInt(2, firstPostId);
			st.setInt(3, lastPostId);
			ResultSet rs = st.executeQuery();

			int	id = 0;
			String title = null;
			String writerId = null;
			String content = null;
			String files = null;
			Date regdate = null;
			int hit = 0;
			int cmtCount = 0;
			boolean pub = false;

			while (rs.next()) {
				id = rs.getInt("id");
				title = rs.getString("title");
				writerId = rs.getString("writer_id");
				files = rs.getString("files");
				regdate = rs.getDate("regdate");
				hit = rs.getInt("hit");
				cmtCount = rs.getInt("CNT");
				pub = rs.getBoolean("pub");
//				content = rs.getString("content");	
				
				NoticeView notice = new NoticeView(id, title, writerId, files, regdate, hit, pub, cmtCount);				
				list.add(notice);
			}

			rs.close();
			st.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public int getNoticeCount() {
		return getNoticeCount("title", "");
	}
	
	public int getNoticeCount(String field, String query) {
		String sql = "select count(id) count from ("
				+ "select rownum num, N.* from ("
				+ "select * from notice where " + field + " like ? order by regdate desc) N "
				+ ")";
		
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";
	
		int count = 0;
		
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%" + query + "%");
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count");
			}
		
			rs.close();
			st.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	public Notice getNotice(int id) {
		String sql = "select * from notice where id = ?";
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";
		
		Notice notice = null;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
		
			if (rs.next()) {
				int postId = rs.getInt("id");
				String title = rs.getString("title");
				String writerId = rs.getString("writer_id");
				String content = rs.getString("content");	
				String files = rs.getString("files");
				Date regdate = rs.getDate("regdate");
				int hit = rs.getInt("hit");
				boolean pub = rs.getBoolean("pub");
				
				notice = new Notice(postId, title, writerId, content, files, regdate, hit, pub);				
			}
			rs.close();
			st.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notice;
	}
	
	public Notice getNextNotice(int id) {
		String sql = "select N.* from "
				+ "(select * from notice where regdate > (select regdate from notice where id = ?) "
				+ "order by id) N "
				+ "where rownum = 1";
		
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";
		
		Notice notice = null;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
		
			if (rs.next()) {
				int postId = rs.getInt("id");
				String title = rs.getString("title");
				String writerId = rs.getString("writer_id");
				String content = rs.getString("content");	
				String files = rs.getString("files");
				Date regdate = rs.getDate("regdate");
				int hit = rs.getInt("hit");
				boolean pub = rs.getBoolean("pub");
				
				notice = new Notice(postId, title, writerId, content, files, regdate, hit, pub);			
			}
			rs.close();
			st.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notice;
	} 
	
	public Notice getPrevNotice(int id) {
		String sql = "select * from "
				+ "(select * from notice where regdate < (select regdate from notice where id = ?) "
				+ "order by id desc) "
				+ "where rownum = 1";
		
		String uri = "jdbc:oracle:thin:@localhost:1521/orcl";
		String uid = "newlec";
		String pwd = "061599";
		String driver = "oracle.jdbc.driver.OracleDriver";
		
		Notice notice = null;
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(uri, uid, pwd);
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
		
			if (rs.next()) {
				int postId = rs.getInt("id");
				String title = rs.getString("title");
				String writerId = rs.getString("writer_id");
				String content = rs.getString("content");	
				String files = rs.getString("files");
				Date regdate = rs.getDate("regdate");
				int hit = rs.getInt("hit");
				boolean pub = rs.getBoolean("pub");
				
				notice = new Notice(postId, title, writerId, content, files, regdate, hit, pub);		
			}
			rs.close();
			st.close();
			con.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notice;
	}
}
