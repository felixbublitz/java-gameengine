package engine.datatypes;

public class ServerConnection {
	private int port;
	private String ip;

	public ServerConnection(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}
}
