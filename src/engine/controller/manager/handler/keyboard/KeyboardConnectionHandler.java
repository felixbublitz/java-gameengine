package engine.controller.manager.handler.keyboard;

import javax.swing.JFrame;

import engine.controller.Controller;
import engine.controller.manager.handler.ConnectionHandler;

public class KeyboardConnectionHandler extends ConnectionHandler {

	int initKeyboards = 0;
	JFrame frame;
	private static final int KEYBOARDS = 1;

	public KeyboardConnectionHandler(JFrame frame) {
		super();
		this.controllerType = Controller.CONTROLLER_KEYBOARD;
		this.frame = frame;
	}

	@Override
	protected boolean getDeviceConnection() {
		if (initKeyboards < KEYBOARDS) {
			initKeyboards += 1;
			return true;
		}
		return false;
	}

	@Override
	protected Runnable createInputHandler(Controller controller) {
		return new KeyboardInputHandler(controller, inputHandlerInterface, frame);
	}

	@Override
	protected Runnable createOutputHandler(Controller controller) {
		return null;
	}

	@Override
	protected void initDetection() {

	}

}
