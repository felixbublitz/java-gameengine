package engine.controller.manager;

import java.util.ArrayList;

import javax.swing.JFrame;

import engine.controller.Controller;
import engine.controller.manager.handler.ConnectionHandler;
import engine.controller.manager.handler.keyboard.KeyboardConnectionHandler;
import engine.controller.manager.handler.phone.PhoneConnectionHandler;
import engine.controller.manager.handler.server.ServerConnectionHandler;
import engine.interfaces.ConnectionHandlerInterface;
import engine.interfaces.ControllerManagerInterface;
import engine.interfaces.InputHandlerInterface;

public class ControllerManager implements ConnectionHandlerInterface, InputHandlerInterface {

	public static final int CONTROLLER_PHONE = 1;
	public static final int CONTROLLER_KEYBOARD = 2;
	public static final int CONTROLLER_SERVER = 3;

	private ControllerManagerInterface controllerManagerInteface;
	private ConnectionHandler connectionHandler;
	private ArrayList<Controller> controller;
	private int controllerType;
	private int limit = 10;
	private boolean[] freeSlot = {true,true,true,true};


	public ConnectionHandler getConnectionHandler(){
		return this.connectionHandler;
	}

	public ConnectionHandler createConnectionHandler(int controllerType, JFrame frame){
		this.controllerType = controllerType;
		switch(controllerType){
		case CONTROLLER_PHONE:
			return new PhoneConnectionHandler(this,this);
		case CONTROLLER_KEYBOARD:
			return new KeyboardConnectionHandler(this,this,frame);
		case CONTROLLER_SERVER:

			return new ServerConnectionHandler(this,this);
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

	public ControllerManager(int controllerType, ControllerManagerInterface controllerManagerInteface, JFrame frame){

		this.controller = new ArrayList<Controller>();
		this.controllerManagerInteface = controllerManagerInteface;
		this.connectionHandler = createConnectionHandler(controllerType, frame);
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
			controller.controllerType = controllerType;
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
		if(controller == null){
			this.controllerManagerInteface.controllerDisconnected(controller);
			return;
		}

		this.controller.remove(controller);
		this.releaseSlot(controller.getDeviceID());
		this.controllerManagerInteface.controllerDisconnected(controller);
	}

	@Override
	public void controllerConnectionReady(Controller controller) {
		this.controllerManagerInteface.controllerConnected(controller);

	}

}
