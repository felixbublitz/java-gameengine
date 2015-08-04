package engine.controller.manager.handler;

import engine.controller.Controller;
import engine.datatypes.TCPData;
import engine.interfaces.InputHandlerInterface;

public class InputHandler implements Runnable {

	private boolean destroyed;
	private boolean enabled = true;
	protected Controller controller;
	protected InputHandlerInterface inputHandlerInterface;
	boolean onlyKey;
	
	public InputHandler(Controller controller, InputHandlerInterface inputHandlerInterface){
		this.controller = controller;
		this.inputHandlerInterface = inputHandlerInterface;
	}
	
	@Override
	public void run() {
		while(!destroyed){
			if(enabled){
				this.detectInput();
				}
			}
	}
	
	public void pause(){
		this.enabled = false;
	}
	
	protected TCPData fetchInput(){
		return null;
	}
	
	public void resume(){
		this.enabled = true;
	}
	
	public void destroy(){
		this.destroyed = true;
	}
	
	private void detectInput(){
		boolean detected = false;
		
		TCPData input = this.fetchInput();
		
		if(input != null){
			detected = true;
			if(this.onlyKey){
				this.controller.emulateKey(input.key);
			}else{
				this.controller.emulateUserInput(input.key, input.value);
			}
			
		}
		
		if(detected){
			this.inputHandlerInterface.receivedInput(this.controller);
		}
		
		
	}

}
