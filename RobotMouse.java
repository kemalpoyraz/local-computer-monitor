import java.awt.Robot;
import java.awt.event.InputEvent;

public class RobotMouse {

	public RobotMouse() throws Exception {
		
		Robot robot = new Robot();

		robot.mouseMove(20, 40);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	public static void main(String[] args) {
		
		try {
			new RobotMouse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
