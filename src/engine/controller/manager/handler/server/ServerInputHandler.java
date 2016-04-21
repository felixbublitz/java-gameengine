package engine.controller.manager.handler.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import engine.controller.manager.handler.InputHandler;
import engine.datatypes.TCPData;
import engine.interfaces.InputHandlerInterface;

public class ServerInputHandler extends InputHandler {

	public final static int SYSTEM_CONNECTION_READY = 60;
	public final static int SYSTEM_REQUEST_TYPE = 61;
	public final static int SYSTEM_SEND_TYPE = 62;
	public final static int SYSTEM_REQUEST_CODE = 63;
	public final static int SYSTEM_SEND_CODE = 64;
	public final static int SYSTEM_MOBILE_CONNECTED = 65;
	public final static int SYSTEM_MOBILE_DISCONNECTED = 66;
	public final static int SYSTEM_DEVICE_DISCONNECTED = 70;
	public final static String TYPE_PC = "pc";
	public final static String TYPE_TEST_PC = "test_pc";

	private Socket server;
	private TCPData data;
	private BufferedReader bufferedReader;
	private ServerInputHandlerInterface serverInputHandlerInterface;
	private boolean testCase;

	@Override
	protected void detectInput() {

		TCPData input = this.fetchInput();

		if (input != null) {

			if (!isInternCommand(input)) {
				this.serverInputHandlerInterface.getInputFromServer(input.id, input.key, input.value);
			}
		}

	}

	@Override
	protected void loadHandler() {
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isInternCommand(TCPData input) {

		switch (input.key) {
		case SYSTEM_REQUEST_TYPE:
			if(testCase){
				serverInputHandlerInterface.send(SYSTEM_SEND_TYPE, TYPE_TEST_PC);
			}else{
				serverInputHandlerInterface.send(SYSTEM_SEND_TYPE, TYPE_PC);
			}
			serverInputHandlerInterface.send(SYSTEM_REQUEST_CODE);
			return true;

		case SYSTEM_SEND_CODE:
			serverInputHandlerInterface.receivePCCode(input.value);
			return true;

		case SYSTEM_MOBILE_CONNECTED:
			serverInputHandlerInterface.connectController(input.id);

			break;
		case SYSTEM_DEVICE_DISCONNECTED:
			serverInputHandlerInterface.disconnectController(input.id);
			break;
		}

		return false;
	}

	public ServerInputHandler(Socket server, InputHandlerInterface inputHandlerInterface,
			ServerInputHandlerInterface serverInputHandlerInterface, boolean testCase) {
		super(null, inputHandlerInterface);
		this.testCase = testCase;
		this.serverInputHandlerInterface = serverInputHandlerInterface;
		this.server = server;
		this.inputHandlerInterface = inputHandlerInterface;
	}

	public void pause() {
		this.enabled = false;
	}

	TCPData readData(Socket socket) {
		TCPData tcpdata = new TCPData();

		try {
			String line = bufferedReader.readLine();

			if (line == null) {
				return new TCPData(TCPData.CLOSE_CONNECTION);
			}

				line = line.replace("\r", "");
				line = line.replace("\n", "");
				tcpdata = this.decryptData(line);

		} catch (IOException e) {

		}
		return tcpdata;
	}

	private TCPData decryptData(String data) {
		TCPData tcpdata = new TCPData();

		if (data.contains("#")) {
			try {
				String[] splittedString = data.split("#");
				tcpdata.id = Integer.parseInt(splittedString[0]);
				tcpdata.key = Integer.parseInt(splittedString[1]);
				if (splittedString.length > 2) {
					tcpdata.value = splittedString[2];
				}
			} catch (Exception e) {
				tcpdata = null;
			}
		} else {
			try {
				tcpdata.key = Integer.parseInt(data);
				tcpdata.value = null;
			} catch (Exception e) {
				tcpdata = null;
			}
		}

		return tcpdata;
	}

	protected TCPData fetchInput() {
		this.data = this.readData(this.server);

		if (this.data == null) {
			return null;
		}

		if (this.data.key == 0 && this.data.value == null) {
			// this.timeout.reset();
			return null;
		}

		if (this.data.key == -1 && this.data.value == null) {
			data.key = 0;
			data.value = null;
			try {
				this.server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.destroy();
			this.serverInputHandlerInterface.serverOffline();
		}
		return data;
	}

	public void resume() {
		this.enabled = true;
	}

	public void destroy() {
		this.destroyed = true;
	}

}
