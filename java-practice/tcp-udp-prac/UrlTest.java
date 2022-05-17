import java.net.URL;

public class UrlTest {
	public static void main(String[] args) throws Exception {
		URL url = new URL("https://www.naver.com/");

		System.out.println("url.getAuthority() : " + url.getAuthority());
		System.out.println("url.getContent() : " + url.getContent());
		System.out.println("url.getDefaultPort() : " + url.getDefaultPort());
		System.out.println("url.getPort() : " + url.getPort());
		System.out.println("url.getFile() : " + url.getFile());
		System.out.println("url.getHost() : " + url.getHost());
		System.out.println("url.getProtocol() : " + url.getProtocol());
	}
}
