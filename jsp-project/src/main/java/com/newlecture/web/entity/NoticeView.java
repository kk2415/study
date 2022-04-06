package com.newlecture.web.entity;

import java.util.Date;

public class NoticeView extends Notice {

	private int cmtCount;
	
	public NoticeView() {
	}
	
	public NoticeView(int id, String title, String writerId, String files, Date regdate, int hit, boolean pub, int cmtCount) {
		super(id, title, writerId, "", files, regdate, hit, pub);
		this.cmtCount = cmtCount;
	}

	public int getCmtCount() {
		return cmtCount;
	}

	public void setCmtCount(int cmtCount) {
		this.cmtCount = cmtCount;
	}
}
