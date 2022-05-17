import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UrlConnectionTest2 {
	public static void main(String[] args) {
		URL				url = null;
		BufferedReader	input = null;
		String			line = "";
		String			address = "https://codechobo.tistory.com/attachment/cfile27.uf@24425246572B1CD924E809.pdf";

		try {
			url = new URL(address);
			input = new BufferedReader(new InputStreamReader(url.openStream()));

			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	
}
