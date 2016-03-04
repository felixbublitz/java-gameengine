package engine.controller.manager.handler.keyboard;

import javax.swing.JFrame;

import engine.controller.Controller;
import engine.controller.manager.handler.ConnectionHandler;
import engine.interfaces.ConnectionHandlerInterface;
import engine.interfaces.InputHandlerInterface;

public class KeyboardConnectionHandler extends ConnectionHandler {

	int initKeyboards = 0;
	JFrame frame;
	private static final int KEYBOARDS = 1;

	public KeyboardConnectionHandler(ConnectionHandlerInterface connectionHandlerInterface,
			InputHandlerInterface inputHandlerInterface, JFrame frame) {
		super(connectionHandlerInterface, inputHandlerInterface);
		this.frame = frame;
	}

	@Override
	protected boolean getDeviceConnection() {
		if(initKeyboards < KEYBOARDS){
			initKeyboards +=1;
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
