package engine.controller;

import engine.datatypes.UserInput;
import engine.interfaces.ControllerInterface;

public class Controller {

	public final static int KEY_CANCEL = 1;
	public final static int KEY_OK = 2;
	public final static int KEY_LEFT = 3;
	public final static int KEY_RIGHT = 4;
	public final static int KEY_UP = 5;
	public final static int KEY_DOWN = 6;

	public final static int CONTROLLER_PHONE = 1;
	public final static int CONTROLLER_KEYBOARD = 2;
	public final static int CONTROLLER_SERVER = 3;

	private final static int RUMBLE = 11;

	private int pressedKey;
	private int controllerID;
	public int controllerType;
	private String userInput;
	private int remoteID;
	private ControllerInterface controllerInterface;

	public Controller(int controllerID) {
		this.controllerID = controllerID;
	}

	public void setRemoteID(int id) {
		this.remoteID = id;
	}

	public int getRemoteID() {
		return remoteID;
	}

	public void setControllerInterface(ControllerInterface controllerInterface) {
		this.controllerInterface = controllerInterface;
	}

	public int getDeviceID() {
		return this.controllerID;
	}

	public int getPressedKey() {
		int key = this.pressedKey;
		// this.pressedKey = 0;
		return key;
	}

	public boolean isUInput() {
		if (this.userInput == null) {
			return false;
		}
		return true;
	}

	public void rumble() {
		this.send(RUMBLE);
	}

	public void send(int key){
		this.send(key, null);
	}

	public void send(int key, String value) {
		if (this.controllerInterface != null) {
			if (controllerType == CONTROLLER_SERVER) {
				this.controllerInterface.send(this.getRemoteID(), key, value);
			} else {
				this.controllerInterface.send(key, value);
			}
		}
	}

	public UserInput getUserInput() {
		UserInput userInput = new UserInput();
		userInput.type = this.pressedKey;
		userInput.value = this.userInput;

		// this.userInput = null;
		// this.pressedKey = 0;
		return userInput;
	}

	public void emulateKey(int key) {
		this.userInput = null;
		this.pressedKey = key;
	}

	public void emulateUserInput(int key, String input) {
		this.userInput = input;
		this.pressedKey = key;
	}

}
