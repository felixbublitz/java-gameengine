package engine.controller.manager;

import java.util.ArrayList;

import engine.controller.Controller;
import engine.controller.manager.handler.ConnectionHandler;
import engine.controller.manager.handler.phone.PhoneConnectionHandler;
import engine.interfaces.ConnectionHandlerInterface;
import engine.interfaces.ControllerManagerInterface;
import engine.interfaces.InputHandlerInterface;

public class ControllerManager implements ConnectionHandlerInterface, InputHandlerInterface {
	
	public static final int CONTROLLER_PHONE = 1;
	
	private ControllerManagerInterface controllerManagerInteface;
	private ConnectionHandler connectionHandler;
	private ArrayList<Controller> controller;
	private int limit = 4;
	private boolean[] freeSlot = {true,true,true,true};
	
	
	public ConnectionHandler getConnectionHandler(int controllerType){
		switch(controllerType){
		case CONTROLLER_PHONE:
			return new PhoneConnectionHandler(this,this);
		default:
			return new ConnectionHandler(this,this);
		}
	}
	
	public Controller getController(int controllerID){
		
		for(Controller single: controller){
			if(single.getDeviceID() == controllerID){
				return single;
			}
		}
		
		return null;
	}
	
	public ControllerManager(int controllerType, ControllerManagerInterface controllerManagerInteface){
		
		this.controller = new ArrayList<Controller>();
		this.controllerManagerInteface = controllerManagerInteface;
		this.connectionHandler = getConnectionHandler(controllerType);
		new Thread(connectionHandler).start();
	}


	@Override
	public void receivedInput(Controller controller) {
		if(controller.isUInput()){
			this.controllerManagerInteface.getUserInput(controller);
		}else{
			this.controllerManagerInteface.getPressedKey(controller);
		}
		
	}


	@Override
	public Controller connectController() {
		
		if(this.controller.size() < this.limit){
			Controller controller = new Controller(this.getFreeSlot());
			this.controller.add(controller);
		}
		
		return controller.get(this.controller.size() - 1);
	}
	
	private int getFreeSlot(){
		for(int i=0; i < this.freeSlot.length; i++){
			if(this.freeSlot[i] == true){
				this.freeSlot[i] = false;
				return i;
			}
		}
		return 0;
	}
	
	private void releaseSlot(int slotID){
		this.freeSlot[slotID] = true;
	}


	@Override
	public void connectionClosed(Controller controller) {
		this.controller.remove(controller);
		this.releaseSlot(controller.getDeviceID());
		this.controllerManagerInteface.controllerDisconnected(controller);
	}

	@Override
	public void controllerConnectionReady(Controller controller) {
		this.controllerManagerInteface.controllerConnected(controller);
		
	}
	
}
