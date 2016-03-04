package engine.controller.manager.handler;

import engine.controller.Controller;
import engine.interfaces.ConnectionHandlerInterface;
import engine.interfaces.InputHandlerInterface;

public class ConnectionHandler implements Runnable {

	protected boolean destroyed;
	protected boolean enabled = true;
	protected ConnectionHandlerInterface connectionHandlerInterface;
	protected InputHandlerInterface inputHandlerInterface;

	public ConnectionHandler(ConnectionHandlerInterface connectionHandlerInterface, InputHandlerInterface inputHandlerInterface) {
		this.connectionHandlerInterface = connectionHandlerInterface;
		this.inputHandlerInterface = inputHandlerInterface;
	}

	@Override
	public void run() {
		this.initDetection();
		while(!destroyed){
			if(enabled){
				this.detectDevices();
			}
	}
	}

	public void pause(){
		this.enabled = false;
	}

	public void resume(){
		this.enabled = true;
	}

	public void destroy(){
		this.destroyed = true;
	}

	protected void initDetection(){

	}

	protected void detectDevices(){
		if(this.getDeviceConnection()){
		Controller controller = this.connectionHandlerInterface.connectController();
		this.openIOHandler(controller);
		this.connectionHandlerInterface.controllerConnectionReady(controller);
		}
	}

	protected void openIOHandler(Controller controller){
		Runnable inputHandler = createInputHandler(controller);
		Runnable outputHandler = createOutputHandler(controller);
		if(inputHandler != null){
			new Thread(inputHandler).start();
		}
		if(outputHandler != null){
			new Thread(outputHandler).start();
		}
	}

	protected Runnable createInputHandler(Controller controller){
		InputHandler inputHandler = new InputHandler(controller,this.inputHandlerInterface);
		return inputHandler;
	}

	protected Runnable createOutputHandler(Controller controller){
		OutputHandler outputHandler = new OutputHandler();
		return outputHandler;
	}

	protected boolean getDeviceConnection(){
		return false;
	}


}
