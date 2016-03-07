package engine.interfaces;

import engine.controller.Controller;

public interface ConnectionHandlerInterface {
	Controller connectController();

	void controllerConnectionReady(Controller controller);
}
