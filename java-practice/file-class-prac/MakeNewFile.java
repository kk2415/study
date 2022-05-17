import java.io.File;
import java.io.IOException;

public class MakeNewFile {
	public static void main(String[] arg) {
		//1. 이미 존재하는 파일을 참조할 때
		File oldFile = new File("/home/kyunkim/", "test.txt");

		//2. 기존에 없는 파일을 새로 생성할 때
		File newFile = new File("/home/kyunkim/", "newTest.txt");
		try {
			newFile.createNewFile(); //새로운 파일이 생성된다.
		}
		catch (IOException e) {
			
		}
	}
}