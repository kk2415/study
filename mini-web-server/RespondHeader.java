import com.sun.net.httpserver.*;
import com.sun.net.httpserver.Headers;

public class RespondHeader {
	HttpExchange	httpExchange;
	Headers			header;
	Headers			requestHeader;
	fileFinder		fileFinder;
	long			contentLength;
	int				statusCode;

	public RespondHeader(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
		fileFinder = new fileFinder(httpExchange.getRequestURI());
		header = httpExchange.getResponseHeaders();

//		System.out.println(httpExchange.getRequestMethod());
//		requestHeader = httpExchange.getRequestHeaders();
//		List<String> list = requestHeader.get("Connection");
//		System.out.println(list.get(0));
//		List<String> list = requestHeader.
//		System.out.println(list.get(0));
//		System.out.println(requestHeader.getFirst("Connection"));
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public void setStatusCode() {
		if (fileFinder.fileNotFound) {
			this.statusCode = 404;
		}
		else {
			this.statusCode = 200;
		}
	}

	public void add() {
		header.add("Content-Type", "text/html;charset=UTF-8");
		header.add("Content-Length", String.valueOf(contentLength));
	}

	public void sendResponseHeaders() throws java.io.IOException {
		setStatusCode();
		httpExchange.sendResponseHeaders(statusCode, contentLength);
	}
}
