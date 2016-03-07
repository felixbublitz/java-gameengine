package engine.interfaces;

import engine.controller.Controller;

public interface ControllerManagerInterface {
	void getPressedKey(Controller controller);

	void getUserInput(Controller controller);

	void controllerConnected(Controller controller);

	void controllerDisconnected(Controller controller);
}
