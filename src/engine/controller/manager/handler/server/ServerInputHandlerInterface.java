package engine.controller.manager.handler.server;

public interface ServerInputHandlerInterface {
	void connectController(int id);
	void disconnectController(int id);

	void send(int id, int key, String value);
	void serverOffline();
	void getInputFromServer(int deviceID, int key, String value);
	void receivePCCode(String code);

}
