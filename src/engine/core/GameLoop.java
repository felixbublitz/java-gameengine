package engine.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import engine.interfaces.GameLoopInterface;
import engine.interfaces.GraphicsInterface;

public class GameLoop implements Runnable, GraphicsInterface {

	Graphics graphics;

	private boolean destroyed;
	private boolean enabled = true;
	private boolean drawFPS = false;

	private long lastUpdate = 0;
	private long elapsedTime;

	private int updateCount;
	private int drawCount;
	private long lastDrawLength = 0;

	private Game game;

	private long lastMeasure = 0;

	private static final int MEASURE_INTERVAL = 1000;
	private static final int MILISECONDS_PER_SECOND = 1000;
	private static final int UPDATES_PER_SECOND = 25;
	private static final int UPDATE_INTERVAL = MILISECONDS_PER_SECOND / UPDATES_PER_SECOND;

	private int ups = 0;
	private int fps = 0;

	public int getUPS() {
		return UPDATES_PER_SECOND;
	}

	public Graphics getGraphics() {
		return this.graphics;
	}

	public GameLoop(Game game) {
		graphics = new Graphics(this);
		this.game = game;

		new Thread(this).start();
	}

	public GameLoop(Game game, int resFactor) {
		graphics = new Graphics(this);
		this.game = game;
		new Thread(this).start();
	}

	public void pause() {
		enabled = false;
	}

	public void resume() {
		enabled = true;
	}

	public void stop() {
		destroyed = true;
	}

	@Override
	public void run() {
		while (!destroyed) {
			if (enabled) {
				elapsedTime = System.currentTimeMillis() - lastUpdate;
				if (elapsedTime >= UPDATE_INTERVAL) {
					lastUpdate = System.currentTimeMillis();
					this.game.update();
					updateCount += 1;
				}

				if (elapsedTime <= UPDATE_INTERVAL - lastDrawLength) {
					long drawBeginTime = System.currentTimeMillis();
					float interpolationFactor = (float) elapsedTime / (float) UPDATE_INTERVAL;
					this.graphics.requestUpdate(interpolationFactor);
					drawCount++;
					lastDrawLength = System.currentTimeMillis() - drawBeginTime;

					if (lastDrawLength > UPDATE_INTERVAL) {
						lastDrawLength = 0;
					}
				}

				if (System.currentTimeMillis() - lastMeasure >= MEASURE_INTERVAL) {
					fps = drawCount;
					ups = updateCount;

					lastMeasure = System.currentTimeMillis();
					updateCount = 0;
					drawCount = 0;
				}

			}
		}
	}

	@Override
	public void Draw(Graphics2D g, float interpolationFactor) {
		this.game.draw(g, interpolationFactor);
		if (drawFPS) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			g.drawString("FPS:" + this.fps, 10, 20);
			g.drawString("UPS:" + this.ups, 10, 44);

		}
	}

}
