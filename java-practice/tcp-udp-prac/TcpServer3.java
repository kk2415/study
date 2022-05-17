import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpServer3 implements Runnable
{
	ServerSocket serverSocket;
	Thread[] threadArr;
	public static void main(String[] args)
	{
		TcpServer3 server = new TcpServer3(5);
		server.start();
	}

	public TcpServer3(int num)
	{
		try
		{
			serverSocket = new ServerSocket(7777);
			System.out.println(getTime() + "서버가 준비되었습니다.");

			threadArr = new Thread[num];
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void start()
	{
		for (int i = 0; i < threadArr.length; i++)
		{
			threadArr[i] = new Thread(this);
			threadArr[i].start();
		}
	}

	public void run()
	{
		while (true)
		{
			try
			{
				System.out.println(getTime() + "가 연결요청을 기다립니다.");

				Socket socket = serverSocket.accept();
				System.out.println(getTime() + socket.getInetAddress() + "로부터 연결요청이 들어왔습니다.");

				OutputStream out = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(out);

				dos.writeUTF("[Notice] Test Message1 from Server.");
				System.out.println(getTime() + "데이터를 전송했습니다.");
				
				dos.close();
				socket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	static String getTime()
	{
		String name = Thread.currentThread().getName();
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
		return f.format(new Date()) + name;
	} 
}