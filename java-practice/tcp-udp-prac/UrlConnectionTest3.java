import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UrlConnectionTest3 {
	public static void main(String[] args) {
		URL					url = null;
		InputStream			in = null;
		FileOutputStream	out= null;
		String				address = "https://github.com/castello/javajungsuk3/blob/master/java_jungsuk3e_src_20170601.zip";
		int					ch = 0;

		try {
			url = new URL(address);
			in = url.openStream();
			out = new FileOutputStream("java_jungsuk3e_src_20170601.zip");

			while ((ch = in.read()) != -1) {
				out.write(ch);
			}
			in.close();
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	
}
