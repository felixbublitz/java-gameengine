package engine.controller.manager.handler.phone;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import engine.controller.Controller;
import engine.controller.manager.handler.OutputHandler;
import engine.interfaces.ControllerInterface;

public class PhoneOutputHandler extends OutputHandler implements ControllerInterface {

	Socket client;
	boolean enabled = true;
	String value;
    int key = -1;
    PrintWriter output;
    ArrayList<String> outputStack;

	public PhoneOutputHandler(Controller controller, Socket client){
		this.outputStack = new ArrayList<String>();
		this.client = client;
		controller.setControllerInterface(this);
	}


	@Override
	public void send(int key, String value) {
		  this.value = value;
	      this.key = key;
	      if(this.key != -1) {
              if(value==null){
            	  if(output != null){
            		  output.println(key);
            	  }else{
            		  outputStack.add(String.valueOf(key));
            	  }
              }else {
            	  if(output != null){
            		  output.println(key + "#" + value);
            	  }else{
            		  outputStack.add(key + "#" + value);
            	  }
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

                	 if(outputStack.size() != 0){
                		 for(String single : outputStack){
                			 output.println(single);
                		 }
                		 outputStack.clear();
                	 }

                     Thread.sleep(1);
                 }


             } catch (UnknownHostException e) {

             } catch (IOException e) {
             }
         } catch (Exception e) {
             e.printStackTrace();
         }

}


	@Override
	public void send(int id, int key, String value) {
		// TODO Auto-generated method stub

	}
}
