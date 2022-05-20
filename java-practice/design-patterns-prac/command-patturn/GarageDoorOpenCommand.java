public class GarageDoorOpenCommand implements Command {
	GarageDoor Garagedoor;

	public GarageDoorOpenCommand(GarageDoor Garagedoor) {
		this.Garagedoor = Garagedoor;
	}

	public void excute() {
		Garagedoor.up();
	}
}