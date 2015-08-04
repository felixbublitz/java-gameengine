package engine.controller.manager.handler.phone;

import engine.interfaces.PhoneTimeoutInterface;

public class PhoneTimeoutHandler implements Runnable{
	
	private long lastConnection;
	private static final int TIMEOUT = 15000;
	boolean enabled = true;
	private PhoneTimeoutInterface phoneTimeoutInterface;

	public PhoneTimeoutHandler(PhoneTimeoutInterface phoneTimeoutInterface) {
		this.lastConnection = System.currentTimeMillis();
		this.phoneTimeoutInterface = phoneTimeoutInterface;
	}
	
	public void reset(){
		this.lastConnection = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		while(enabled){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		if(System.currentTimeMillis() - this.lastConnection > TIMEOUT){
			enabled = false;
			phoneTimeoutInterface.connectionClosed();
		}
		}
	}

}
