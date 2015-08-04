package engine.controller.manager.handler.phone;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import engine.controller.Controller;
import engine.controller.manager.handler.OutputHandler;
import engine.interfaces.ControllerInterface;

public class PhoneOutputHandler extends OutputHandler implements ControllerInterface {

	Socket client;
	boolean enabled = true;
	String value;
    int key = -1;
    PrintWriter output;
	
	public PhoneOutputHandler(Controller controller, Socket client){
		this.client = client;
		controller.setControllerInterface(this);
	}
	

	@Override
	public void send(int key, String value) {
		System.out.println(key);
		  this.value = value;
	      this.key = key;
	      if(this.key != -1) {
              if(value==null){
                  output.println(key);
              }else {
                  output.println(key + "#" + value);
              }
              value = null;
              key = -1;
              
          }
	}
	
	 public void run() {
         try {

             try {
            	
                 OutputStream out = client.getOutputStream();
                 output = new PrintWriter(out);
                 this.output = new PrintWriter(client.getOutputStream(), true);

                 while (enabled) {
                     Thread.sleep(1);
                 }
                 client.close();


             } catch (UnknownHostException e) {

             } catch (IOException e) {
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
	
}
}
