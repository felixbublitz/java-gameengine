package engine.datatypes;

public class ServerConnection {
	private int port;
	private String ip;
	private boolean valid;

	public ServerConnection(String ip, int port) {

		this.setValid(true);

		if(isValidIP(ip)){
			this.setIP(ip);
		}else{
			this.setValid(false);
			System.err.println("Class 'SeverConnection' : IP '"+ ip + "' not Valid");
		}

		if(this.isValidPort(port)){
			this.setPort(port);
		}else{
			this.setValid(false);
			System.err.println("Class 'SeverConnection' : Port '"+ port + "' not Valid");
		}

	}

	private boolean isValid(){
		return this.valid;
	}

	private void setValid(boolean valid){
		this.valid = valid;
	}

	private boolean isValidIP(String ip){
		return true;
	}

	private boolean isValidPort(int port){
		if(port >=0 && port <= 9999){
			return true;
		}
		return false;
	}

	private void setPort(int port){
		this.port = port;
	}

	private void setIP(String ip){
		this.ip = ip;
	}

	public String getIP() {
		if(this.isValid()){
			return ip;
		}

		System.err.println("Class 'SeverConnection' : No valid connection data");
		return null;
	}

	public int getPort() {
		if(this.isValid()){
			return port;
		}
		System.err.println("Class 'SeverConnection' : No valid connection data");
		return 0;
	}
}
