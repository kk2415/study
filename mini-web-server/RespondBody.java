import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.*;

public class RespondBody {
	private OutputStream	bodyWriter;
	private HttpExchange	httpExchange;
	private FileInputStream	in;
	private File			file;
	private String			contents;
	private int				fileLength;
	private ByteBuffer		byteBuffer;
	fileFinder				fileFinder;

	RespondBody(final HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
		bodyWriter = httpExchange.getResponseBody();
		fileFinder = new fileFinder(httpExchange.getRequestURI());
		contents = null;
		fileLength = 0;
	}

	public int getFileLength() {
		return (fileLength);
	}

	public void fillContents() throws IOException {
		String str;
		String filePath;

		if (httpExchange.getRequestMethod().equals("POST")) {
			String requestBody;

			InputStreamReader inReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
			BufferedReader br = new BufferedReader(inReader);
			while ((requestBody = br.readLine()) != null) {
				contents += requestBody;
			}
			System.out.println(contents);
			System.out.println("requestBody");
		}

		filePath = fileFinder.getFilePath();
		file = new File(filePath);
		in = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		contents = "";
		while ((str = reader.readLine()) != null) {
			contents += str;
		}
	}

	public void encodingContents(String charset) {
		switch (charset) {
			case "UTF-8":
				byteBuffer = StandardCharsets.UTF_8.encode(contents);
			case "UTF-16":
				byteBuffer = StandardCharsets.UTF_16.encode(contents);
		}
		/*이 파일 길이를 헤더에 적어줘야함*/
		fileLength = byteBuffer.limit();
	}

	public void sendRespondBody() throws IOException {
		byte[] byteContents = new byte[fileLength];

		byteBuffer.get(byteContents, 0, fileLength);
		bodyWriter.write(byteContents);
	}

	public void close() throws IOException {
		bodyWriter.close(); // 반드시, Response Header를 보낸 후에 닫아야함
	}
}
