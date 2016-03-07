package engine.controller.manager.handler.phone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import engine.controller.Controller;
import engine.controller.manager.handler.InputHandler;
import engine.datatypes.TCPData;
import engine.interfaces.InputHandlerInterface;
import engine.interfaces.PhoneTimeoutInterface;

public class PhoneInputHandler extends InputHandler implements PhoneTimeoutInterface {

	Socket client;
	PhoneTimeoutHandler timeout;
	TCPData data;
	private BufferedReader bufferedReader;
	private char[] buffer;

	public PhoneInputHandler(Controller controller, Socket client, InputHandlerInterface inputHandlerInterface) {
		super(controller, inputHandlerInterface);
		this.client = client;

		timeout = new PhoneTimeoutHandler(this);
		new Thread(timeout).start();
	}

	@Override
	protected TCPData fetchInput() {
		this.data = this.readData(this.client);

		if (this.data == null) {
			return null;
		}

		if (this.data.key == 0 && this.data.value == null) {
			this.timeout.reset();
			return null;
		}

		if (this.data.key == -1 && this.data.value == null) {
			data.key = 0;
			data.value = null;
			this.connectionClosed();
		}
		return data;
	}

	private TCPData decryptData(String data) {
		TCPData tcpdata = new TCPData();

		if (data.contains("#")) {
			try {
				String[] splittedString = data.split("#");
				tcpdata.key = Integer.parseInt(splittedString[0]);
				tcpdata.value = splittedString[1];
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

	TCPData readData(Socket socket) {
		TCPData tcpdata = new TCPData();
		String data = null;
		try {
			if (bufferedReader == null) {
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				buffer = new char[200];
			}

			if (bufferedReader == null) {
				return new TCPData(TCPData.CLOSE_CONNECTION);
			}

			int dataLength = bufferedReader.read(buffer, 0, 200);
			if (dataLength == -1) {
				tcpdata.key = -1;
			} else {

				data = new String(buffer, 0, dataLength);
				data = data.replace("\r", "");
				data = data.replace("\n", "");
				tcpdata = this.decryptData(data);
			}
		} catch (IOException e) {

		}
		return tcpdata;
	}

	@Override
	public void connectionClosed() {
		try {
			this.client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.destroy();
		this.inputHandlerInterface.connectionClosed(this.controller);
	}

	@Override
	public void destroy() {
		timeout.enabled = false;
		this.data = null;
		super.destroy();
	}

}
