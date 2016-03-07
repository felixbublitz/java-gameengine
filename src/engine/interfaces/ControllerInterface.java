package engine.interfaces;

public interface ControllerInterface {

	void send(int key, String value);

	// For Server Controller
	void send(int id, int key, String value);

}
