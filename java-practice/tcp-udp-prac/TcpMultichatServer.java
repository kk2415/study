import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.catalog.Catalog;

public class TcpMultichatServer {
	HashMap client;

	TcpMultichatServer()
	{
		client = new HashMap();
		Collections.synchronizedMap(client);
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try
		{
			serverSocket = new ServerSocket(7777);
			System.out.println("서버가 시작되었습니다.");

			while (true)
			{
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	void sendToAll(String msg)
	{
		Iterator it = client.keySet().iterator();

		while (it.hasNext())
		{
			try
			{
				DataOutputStream out = (DataOutputStream)client.get(it.next());
				out.writeUTF(msg);
			}
			catch (IOException e)
			{

			}
		}
	}

	public static void main(String[] args) {
		new TcpMultichatServer().start();
	}

	class ServerReceiver extends Thread 
	{
		Socket socket;
		DataInputStream in;
		DataOutputStream out;

		ServerReceiver(Socket socket)
		{
			this.socket = socket;
			try
			{
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		public void run()
		{
			String name = "";

			try
			{
				name = in.readUTF();
				sendToAll("#" + name + "님이 들어오셨습니다.");

				client.put(name, out);
				System.out.println("현재 서버접속자 수는 " + client.size() + "입니다.");
				while (in != null)
				{
					sendToAll(in.readUTF());
				}
			}
			catch (IOException e)
			{

			}
			finally
			{
				sendToAll("#" + name + "님이 나가셨습니다.");
				client.remove(name);
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "접속을 종료하였습니다.");
				System.out.println("현재 서버접속자 수는 " + client.size() + "입니다.");
			}
		}
	}
}
