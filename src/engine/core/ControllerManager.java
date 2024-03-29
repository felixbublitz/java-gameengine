package engine.core;

import java.util.ArrayList;

import engine.controller.Controller;
import engine.controller.manager.handler.ConnectionHandler;
import engine.interfaces.ConnectionHandlerInterface;
import engine.interfaces.ControllerManagerInterface;
import engine.interfaces.InputHandlerInterface;

public class ControllerManager implements ConnectionHandlerInterface, InputHandlerInterface {

	private ConnectionHandler connectionHandler;
	private ArrayList<Controller> controller;
	private int controllerType;
	Game game;

	//freeSlots size must be limit value
	private int limit = 10;
	private boolean[] freeSlot = { true, true, true, true, true, true, true, true, true, true };

	public ConnectionHandler getConnectionHandler() {
		return this.connectionHandler;
	}

	public Controller getController(int controllerID) {

		for (Controller single : controller) {
			if (single.getDeviceID() == controllerID) {
				return single;
			}
		}

		return null;
	}

	public ControllerManager(ConnectionHandler connectionHandler,
			Game game) {

		this.controllerType = connectionHandler.getControllerType();
		this.controller = new ArrayList<Controller>();
		this.game = game;

		connectionHandler.setConnectionHandlerInterface(this);
		connectionHandler.setInputHandlerInterface(this);
		this.connectionHandler = connectionHandler;

		new Thread(connectionHandler).start();
	}

	@Override
	public void receivedInput(Controller controller) {
		if (controller.isUInput()) {
			game.inputReceived(controller);
		} else {
			game.keyPressed(controller);
		}

	}

	@Override
	public Controller connectController() {

		if (this.controller.size() < this.limit) {
			Controller controller = new Controller(this.getFreeSlot());
			controller.controllerType = controllerType;
			this.controller.add(controller);
		}

		return controller.get(this.controller.size() - 1);
	}

	private int getFreeSlot() {
		for (int i = 0; i < this.freeSlot.length; i++) {
			if (this.freeSlot[i] == true) {
				this.freeSlot[i] = false;
				return i;
			}
		}
		return 0;
	}

	private void releaseSlot(int slotID) {
		this.freeSlot[slotID] = true;
	}

	@Override
	public void connectionClosed(Controller controller) {
		if (controller == null) {
			game.controllerDisconnected(controller);
			return;
		}

		this.controller.remove(controller);
		this.releaseSlot(controller.getDeviceID());
		game.controllerDisconnected(controller);
	}

	@Override
	public void controllerConnectionReady(Controller controller) {
		game.controllerConnected(controller);

	}

}
