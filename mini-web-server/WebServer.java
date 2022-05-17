import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class WebServer {
	private HttpServer server = null;

	WebServer(int portNum) {
		creatServer(portNum);
	}

	private void creatServer(int portNum) {
		try {
			server = HttpServer.create(new InetSocketAddress(portNum), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.createContext("/", new Handler());
		server.setExecutor(null);
	}

	public void	start() {
		server.start();
	}

	class Handler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			RespondHeader header = new RespondHeader(exchange);
			RespondBody body = new RespondBody(exchange);

			header.add();
			body.fillContents();
			body.encodingContents("UTF-8");
			header.setContentLength(body.getFileLength());
			header.sendResponseHeaders();
			body.sendRespondBody();
			body.close();
		}
	}
}
