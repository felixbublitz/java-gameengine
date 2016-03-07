package engine.interfaces;

import engine.controller.Controller;

public interface InputHandlerInterface {
	void receivedInput(Controller controller);

	void connectionClosed(Controller controller);
}
