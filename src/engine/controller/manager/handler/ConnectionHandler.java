package engine.controller.manager.handler;

import engine.controller.Controller;
import engine.interfaces.ConnectionHandlerInterface;
import engine.interfaces.InputHandlerInterface;

public class ConnectionHandler implements Runnable {

	private boolean destroyed;
	private boolean enabled = true;
	private ConnectionHandlerInterface connectionHandlerInterface;
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
	
	private void detectDevices(){
		if(this.getDeviceConnection()){
		Controller controller = this.connectionHandlerInterface.connectController();
		this.openIOHandler(controller);
		this.connectionHandlerInterface.controllerConnectionReady(controller);
		}
	}
	
	private void openIOHandler(Controller controller){
		new Thread(createInputHandler(controller)).start();
		new Thread(createOutputHandler(controller)).start();
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
