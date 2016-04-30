package engine.interfaces;

import engine.controller.Controller;

public interface ControllerManagerInterface {
	void onKeyPressed(Controller controller);
	void onInputReceived(Controller controller);
	void controllerConnected(Controller controller);
	void controllerDisconnected(Controller controller);
}
