package engine.controller.manager.handler.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import engine.controller.Controller;
import engine.controller.manager.handler.OutputHandler;
import engine.interfaces.ControllerInterface;

public class ServerOutputHandler extends OutputHandler implements ControllerInterface {

	Socket server;
	String value;
    int key = -1;
    PrintWriter output;
    ArrayList<String> outputStack;
	boolean enabled = true;

	public final static int CONNECTION_OK = 701;

	public ServerOutputHandler(Socket server){
		this.outputStack = new ArrayList<String>();
		this.server = server;
		this.send(-1,CONNECTION_OK, null);
	}

	@Override
	public void send(int key, String value) {

	}

	 public void run() {
         try {

             try {

                 OutputStream out = server.getOutputStream();
                 output = new PrintWriter(out);
                 this.output = new PrintWriter(server.getOutputStream(), true);

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
		 this.value = value;
	      this.key = key;
	      if(this.key != -1) {
            if(value==null){
          	  if(output != null){
          		  output.println(id + "#" + key);
          	  }else{
          		  outputStack.add(String.valueOf(id + "#" + key));
          	  }
            }else {
          	  if(output != null){
          		  output.println(id + "#" + key + "#" + value);
          	  }else{
          		  outputStack.add(id + "#" +key + "#" + value);
          	  }
            }
            value = null;
            key = -1;

        }

	}


}