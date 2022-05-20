import java.util.*;
import java.lang.*;
import java.io.*;

public class RemoteControlTest {
	public static void main(String[] args) {
		SimpleRemoteControl remote = new SimpleRemoteControl();
		GarageDoor garagedoor = new GarageDoor();
		GarageDoorOpenCommand command = new GarageDoorOpenCommand(garagedoor);

		remote.setCommand(command);
		remote.buttonWasPressed();
	}
}