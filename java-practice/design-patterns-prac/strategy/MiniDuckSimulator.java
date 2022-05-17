public class MiniDuckSimulator {
	public static void main(String[] args)
	{
		MallardDuck mallardDuck = new MallardDuck();
		ModelDuck modelDuck = new ModelDuck();

		mallardDuck.performQuack();
		mallardDuck.performFly();

		modelDuck.performFly();
		modelDuck.performQuack();
		modelDuck.setFlyBehavior(new FlyWithWings());
		modelDuck.performFly();
	}
}
