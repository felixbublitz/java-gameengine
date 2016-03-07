package engine.interfaces;

import java.awt.Graphics2D;

public interface GameLoopInterface {
	void update();

	void draw(Graphics2D g, float interpolationFactor);
}
