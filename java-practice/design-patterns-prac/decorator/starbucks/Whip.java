import java.text.BreakIterator;

public class Whip extends CondimentDecorator {
	Beverage beverage;

	public Whip(Beverage beverage) {
		this.beverage = beverage;
	}

	public String getDescription() {
		return beverage.getDescription() + ", 휘핑 크림";
	}

	public double cost() {
		return .30 + beverage.cost();
	}
}
