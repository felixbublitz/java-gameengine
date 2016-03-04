package engine.controller.manager.handler.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import engine.controller.Controller;
import engine.controller.manager.handler.InputHandler;
import engine.datatypes.TCPData;
import engine.interfaces.InputHandlerInterface;

public class ServerInputHandler extends InputHandler {

	public final static int KEY_REQUEST_TYPE = 601;
	public final static int KEY_SEND_TYPE = 602;
	public final static int KEY_SEND_CONNECTION_REQUEST = 603;
	public final static int KEY_CONNECTION_OK = 701;

	public final static int KEY_CONNECT_BY_CODE = 607;
	public final static int KEY_REQUEST_CODE = 608;

	public final static int KEY_SEND_CODE = 609;

	public final static int KEY_SEND_DATA = 606;

	public final static int KEY_SEND_MOBILE_CONNECT = 614;
	public final static int KEY_SEND_MOBILE_DISCONNECT = 615;

	private Socket server;
	private TCPData data;
	private BufferedReader bufferedReader;
	private char[] buffer;
	private ServerInputHandlerInterface serverInputHandlerInterface;


	@Override
	protected void detectInput() {

		TCPData input = this.fetchInput();



		if(input != null){

			if(!isInternCommand(input)){
				this.serverInputHandlerInterface.getInputFromServer(input.id, input.key, input.value);
			}
		}

	}

	private boolean isInternCommand(TCPData input){



		switch(input.key){
			case KEY_REQUEST_TYPE:
				serverInputHandlerInterface.send(-1, KEY_SEND_TYPE, "pc");
				serverInputHandlerInterface.send(-1,KEY_REQUEST_CODE, null);
				return true;

			case KEY_SEND_CODE:
				serverInputHandlerInterface.receivePCCode(input.value);
				return true;

			case KEY_SEND_MOBILE_CONNECT:
				serverInputHandlerInterface.connectController(input.id);

				break;
			case KEY_SEND_MOBILE_DISCONNECT:
				serverInputHandlerInterface.disconnectController(input.id);
				break;
		}

		return false;
	}


	public ServerInputHandler(Socket server, InputHandlerInterface inputHandlerInterface, ServerInputHandlerInterface serverInputHandlerInterface){
		super(null,inputHandlerInterface);
		this.serverInputHandlerInterface = serverInputHandlerInterface;
		this.server = server;
		this.inputHandlerInterface = inputHandlerInterface;
	}


	public void pause(){
		this.enabled = false;
	}

	TCPData readData(Socket socket)  {
		TCPData tcpdata = new TCPData();
		String data = null;

		try {
			if(this.bufferedReader == null){
				this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.buffer = new char[200];

			}

			if(bufferedReader == null){

				return new TCPData(TCPData.CLOSE_CONNECTION);
			}

	 	int dataLength = bufferedReader.read(buffer, 0, 200);
		if(dataLength == -1){
			tcpdata.key = -1;
		}else{

			data = new String(buffer, 0, dataLength);
			data = data.replace("\r", "");
			data = data.replace("\n", "");
			tcpdata = this.decryptData(data);
		}
		} catch (IOException e) {


		}
	 	return tcpdata;
	    }

	private TCPData decryptData(String data){
		TCPData tcpdata = new TCPData();



		if(data.contains("#")){
			try{
			String[] splittedString = data.split("#");
			tcpdata.id = Integer.parseInt(splittedString[0]);
			tcpdata.key = Integer.parseInt(splittedString[1]);
			if(splittedString.length > 2){
				tcpdata.value = splittedString[2];
			}
			}catch (Exception e){
				tcpdata = null;
			}
		}else{
			try{
			tcpdata.key = Integer.parseInt(data);
			tcpdata.value = null;
			}catch (Exception e){
				tcpdata = null;
			}
		}


		return tcpdata;
	}



	protected TCPData fetchInput(){
		this.data = this.readData(this.server);



		if(this.data == null){
			return null;
		}

		if(this.data.key == 0 && this.data.value == null){
			//this.timeout.reset();
			return null;
		}

		if(this.data.key == -1 && this.data.value == null){
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

	public void resume(){
		this.enabled = true;
	}

	public void destroy(){
		this.destroyed = true;
	}

}
