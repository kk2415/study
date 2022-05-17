public class ClassicSingleton {
	// 참조변수 객체를 static으로 선언!
	private static ClassicSingleton uniqueInstance;

	//생성자가 private
	private ClassicSingleton() {}

	public static ClassicSingleton getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ClassicSingleton();
		}
		return uniqueInstance;
	}
}