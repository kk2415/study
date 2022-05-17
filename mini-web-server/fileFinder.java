import java.io.File;
import java.net.*;

/*
HttpExchange 객체가 리퀘스트 메시제에서 Uri를 보내면
파싱해서 클라이언트가 요청한 html 경로 리턴하는 클래스
*/
public class fileFinder {
	URI				uri = null;
	File			file = null;
	boolean			fileNotFound;
	String			curPath;

	public fileFinder(URI uri) {
		/*System.getProperty("user.dir") : 이 파일의 현재 경로를 리턴함*/
		curPath = System.getProperty("user.dir") + "\\src\\";
		this.uri = uri;
		fileNotFound = false;
		fileSeting();
	}

	public void fileSeting() {
//		System.out.println(uri.toString());
		file = new File(curPath + uri.toString());
		if (uri.toString().equals("/")) {
			file = new File(curPath + "index.html");
		}
		if (!isExist()) {
			file = new File(curPath + "404.html");
			fileNotFound = true;
		}
	}

	public long getFileLength() {
		return (file.length());
	}

	public String getFilePath() {
		return (file.getPath());
	}

	public boolean isExist() {
		if (file.exists()) {
			return (true);
		}
		else {
			return (false);
		}
	}
}
