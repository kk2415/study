import java.io.*;
import java.nio.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class ServerStarter {
	public static void main(String[] args) {
		WebServer server = new WebServer(8080);
		server.start();
	}
}
