public class StarBucksCoffee {
	public static void main(String[] args)
	{
		// Beverage beverage = new Expresso();
		// System.out.println(beverage.description + " $" + beverage.cost());

		Beverage beverage2 = new DarkRoast();
		System.out.println("new DarkRoast() :" + beverage2.hashCode());
		beverage2 = new Mocha(beverage2);
		System.out.println("new Mocha(beverage2) :" + beverage2.hashCode());
		beverage2 = new Mocha(beverage2);
		System.out.println("new Mocha(beverage2) :" + beverage2.hashCode());
		beverage2 = new Whip(beverage2);
		System.out.println(beverage2.getDescription() + " $" + beverage2.cost());
	}	
}
