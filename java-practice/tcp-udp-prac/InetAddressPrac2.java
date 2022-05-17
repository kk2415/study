import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InetAddressPrac 
{
	public static void main(String[] args)
	{
		InetAddress ip = null;		
		InetAddress[] ipArr = null;
		
		try 
		{
			ip = InetAddress.getByName("www.naver.com");
			System.out.println("ip.getHostName() : " + ip.getHostName());
			System.out.println("ip.getHostAddress() : " + ip.getHostAddress());
			System.out.println("ip.toString() : " + ip.toString());

			byte[] ipAddr = ip.getAddress();
			System.out.println("Arrays.toString(ipAddr) : " + Arrays.toString(ipAddr));
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}

		try
		{
			ip = InetAddress.getLocalHost();
			System.out.println("ip.getHostName() : " + ip.getHostName());
			System.out.println("ip.getHostAddress() : " + ip.getHostAddress());
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			ipArr = InetAddress.getAllByName("www.naver.com");

			for (int i = 0; i < ipArr.length; i++)
			{
				System.out.println("ipArr[" + i + "] : " + ipArr[i]);
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}