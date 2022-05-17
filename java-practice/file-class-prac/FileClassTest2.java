import java.io.File;

public class FileClassTest2 
{
	public static void main(String[] arg) 
	{
		if (arg.length != 1) 
		{
			System.out.println("USAGE : java FileClassTest2 DIRECTORY");
			System.exit(0);
		}
		File dir = new File(arg[0]);

		if (!dir.exists() || !dir.isDirectory())
		{
			System.out.println("유효하지 않은 디렉토리입니다.");
			System.exit(0);
		}

		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++)
		{
			String fileName = files[i].getName();
			System.out.println(files[i].isDirectory() ? "[" + fileName + "]" : fileName);
		}
	}
}
