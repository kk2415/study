import java.io.*;

public class FileClassTest1 
{
	public static void main(String[] arg) 
	{
		File f = new File("/home/kyunkim/test.txt");
		String fileName = f.getName();
		int pos = fileName.lastIndexOf(".");

		System.out.println("경로를 제외한 파일이름 - " + f.getName());
		System.out.println("확장자를 제외한 파일이름 - " + fileName.substring(0, pos));
		System.out.println("확장자 - " + fileName.substring(pos + 1));

		System.out.println("경로를 포함한 파일이름 - " + f.getPath());
		System.out.println("파일의 절대경로 - " + f.getAbsolutePath());
		try 
		{
			System.out.println("파일의 정규경로 - " + f.getCanonicalPath());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("File.pathSeparator - " + File.pathSeparator);
		System.out.println("File.pathSeparatorChar - " + File.pathSeparatorChar);

		System.out.println("File.Separator - " + File.separator);
		System.out.println("File.SeparatorChar - " + File.separatorChar);
		System.out.println();
		System.out.println("user.dir = " + System.getProperty("user.dir"));
		System.out.println("java.class.path = " + System.getProperty("java.class.path"));
	}
}
