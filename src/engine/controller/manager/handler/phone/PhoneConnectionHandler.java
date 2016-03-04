package engine.controller.manager.handler.phone;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import engine.controller.Controller;
import engine.controller.manager.handler.ConnectionHandler;
import engine.interfaces.ConnectionHandlerInterface;
import engine.interfaces.InputHandlerInterface;

public class PhoneConnectionHandler extends ConnectionHandler {

	private ServerSocket server;
	private Socket client;

	public PhoneConnectionHandler(ConnectionHandlerInterface connectionHandlerInterface, InputHandlerInterface inputHandlerInterface) {
		super(connectionHandlerInterface, inputHandlerInterface);
	}

	@Override
	protected boolean getDeviceConnection() {
		this.client = null;
		try {
			this.client = server.accept();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		}


	@Override
	protected Runnable createInputHandler(Controller controller) {
		PhoneInputHandler inputHandler = new PhoneInputHandler(controller, client, inputHandlerInterface);
		return inputHandler;
	}

	@Override
	protected Runnable createOutputHandler(Controller controller) {
		PhoneOutputHandler outputHandler = new PhoneOutputHandler(controller, client);
		return outputHandler;
	}

	@Override
	protected void initDetection() {
		try {
			this.server = new ServerSocket(1406);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Der Port '1406' wird bereits verwendet. \nBitte schließen Sie die Anwendung, die diesen Port verwendet.", "Fehler", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	@Override
	public void destroy() {
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.destroy();
	}

}
