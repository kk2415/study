import java.net.URL;
import java.net.URLConnection;

public class UrlConnectionTest {
	public static void main(String[] args) {
		URL		url = null;
		String	address = "https://www.naver.com/";

		try {
			url = new URL(address);
			URLConnection	conn = url.openConnection();
			System.out.println("conn.toString() : " + conn);
			System.out.println("conn.getAllowUserInteraction() : " + conn.getAllowUserInteraction());
			System.out.println("conn.getConnectTimeout() : " + conn.getConnectTimeout());
			System.out.println("conn.getContent() : " + conn.getContent());
			System.out.println("conn.getContentEncoding() : " + conn.getContentEncoding());
			System.out.println("conn.getContentLength() : " + conn.getContentLength());
			System.out.println("conn.getContentType() : " + conn.getContentType());
			System.out.println("conn.getDate() : " + conn.getDate());
			System.out.println("conn.getDefaultAllowUserInteraction() : " + conn.getDefaultAllowUserInteraction());
			System.out.println("conn.getDefaultUseCaches() : " + conn.getDefaultUseCaches());
			System.out.println("conn.getDoInput() : " + conn.getDoInput());
			System.out.println("conn.getDoOutput() : " + conn.getDoOutput());
			System.out.println("conn.getExpiration() : " + conn.getExpiration());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	
}
