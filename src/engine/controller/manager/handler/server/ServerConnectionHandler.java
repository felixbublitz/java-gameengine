package engine.controller.manager.handler.server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import engine.controller.Controller;
import engine.controller.manager.handler.ConnectionHandler;
import engine.datatypes.ServerConnection;

public class ServerConnectionHandler extends ConnectionHandler implements ServerInputHandlerInterface {

	Socket server;
	private int initServer = 0;
	private ServerInputHandler input;
	private ServerOutputHandler output;
	private ArrayList<Controller> controller;
	private String pcCode;
	private ServerConnection serverConnection;
	private boolean testCase;

	public ServerConnectionHandler(ServerConnection serverConnection, boolean testCase) {
		this(serverConnection);
		this.testCase = testCase;
	}

	public ServerConnectionHandler(ServerConnection serverConnection) {
		super();
		this.controllerType = Controller.CONTROLLER_SERVER;
		this.serverConnection = serverConnection;
		this.controller = new ArrayList<Controller>();
	}

	@Override
	protected Runnable createInputHandler(Controller controller) {
		input = new ServerInputHandler(server, this.inputHandlerInterface, this, this.testCase);
		return input;
	}

	@Override
	protected Runnable createOutputHandler(Controller controller) {
		output = new ServerOutputHandler(server);
		return output;
	}

	protected boolean getDeviceConnection() {
		if (initServer < 1) {
			try {
				try {
					server = new Socket(this.serverConnection.getIP(), this.serverConnection.getPort());
					server.setPerformancePreferences(1, 5, 0);
					server.setTcpNoDelay(true);
					initServer++;
					return true;
				} catch (ConnectException e) {
					// System.out.print("Server offline!");
					// System.exit(1);
				}
			} catch (UnknownHostException e) {

			} catch (IOException e) {

			}

		}

		return false;
	}

	@Override
	protected void detectDevices() {
		if (this.getDeviceConnection()) {
			this.openIOHandler(null);
		}
	}

	@Override
	public void serverOffline() {
		for (Controller controller : this.controller) {
			this.disconnectController(controller.getRemoteID());
		}
		this.inputHandlerInterface.connectionClosed(null);
	}

	@Override
	public void getInputFromServer(int deviceID, int key, String value) {
		Controller controller = this.getControllerByRID(deviceID);
		if (controller == null) {
			return;
		}
		if (value == null) {
			controller.emulateKey(key);
		} else {
			controller.emulateUserInput(key, value);
		}

		this.inputHandlerInterface.receivedInput(controller);
	}

	public String getPCCode() {
		return pcCode;
	}

	@Override
	public void connectController(int id) {
		Controller controller = this.connectionHandlerInterface.connectController();
		controller.setRemoteID(id);
		controller.setControllerInterface(output);
		this.controller.add(controller);
		this.connectionHandlerInterface.controllerConnectionReady(controller);
	}

	public Controller getControllerByRID(int id) {
		for (Controller controller : this.controller) {
			if (controller.getRemoteID() == id) {
				return controller;
			}
		}
		return null;
	}

	@Override
	public void disconnectController(int id) {
		this.inputHandlerInterface.connectionClosed(getControllerByRID(id));
	}

	@Override
	public void receivePCCode(String code) {
		this.pcCode = code;
	}

	@Override
	public void send(int id, int key, String value) {
		output.send(id, key, value);
	}

	@Override
	public void send(int key, String value) {
		output.send(key, value);
	}

	@Override
	public void send(int key) {
		output.send(key, null);
	}

	@Override
	public void send(int id, int key) {
		output.send(id, key, null);
	}

}
