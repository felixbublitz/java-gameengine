package engine.core;

import java.awt.Graphics2D;

import engine.controller.Controller;

public class Screen {
	protected Game game;
	private boolean loaded;
	public String title;

	public void setGame(Game game) {
		this.game = game;
	}

	protected void load() {
		this.loaded = true;
	}

	protected void unload() {
		game.animator.stopAll();
		game.timer.stopAll();
	}

	protected boolean isLoaded() {
		return this.loaded;
	}

	protected void screenLoaded() {
		this.loaded = true;
	}

	protected void draw(Graphics2D g) {

	}

	protected void update() {

	}

	protected void getPressedKey(Controller controller) {

	}

	protected void getUserInput(Controller controller) {

	}

	protected void controllerDisconnected(Controller controller) {

	}

	protected void controllerConnected(Controller controller) {

	}

}
