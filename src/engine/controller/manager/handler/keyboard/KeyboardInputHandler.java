package engine.controller.manager.handler.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import engine.controller.Controller;
import engine.controller.manager.handler.InputHandler;
import engine.interfaces.InputHandlerInterface;

public class KeyboardInputHandler extends InputHandler {

	private JFrame frame;

	public KeyboardInputHandler(Controller controller, InputHandlerInterface inputHandlerInterface, JFrame frame) {
		super(controller, inputHandlerInterface);
		this.frame = frame;
	}

	@Override
	public void run() {
		load();
		while(true){
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void load(){
		frame.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		        if(e.getKeyCode() == KeyEvent.VK_UP){
		        	controller.emulateKey(Controller.KEY_UP);
		        	inputHandlerInterface.receivedInput(controller);
		        }
		        if(e.getKeyCode() == KeyEvent.VK_DOWN){
		        	controller.emulateKey(Controller.KEY_DOWN);
		        	inputHandlerInterface.receivedInput(controller);
		        }
		        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
		        	controller.emulateKey(Controller.KEY_CANCEL);
		        	inputHandlerInterface.receivedInput(controller);
		        }
		        if(e.getKeyCode() == KeyEvent.VK_ENTER){
		        	controller.emulateKey(Controller.KEY_OK);
		        	inputHandlerInterface.receivedInput(controller);
		        }
		    }

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
	}



}
