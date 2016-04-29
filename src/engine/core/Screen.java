package engine.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import engine.animation.Animation;
import engine.animation.FadeAnimation;
import engine.animation.Animation.Speed;
import engine.controller.Controller;
import engine.interfaces.AnimationInterface;
import engine.objects2D.ShapeObject;

public class Screen {
	protected Game game;
	private boolean loaded;
	public String title;
	private ShapeObject fadeShape;

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

	protected void fadeToColor(Color color, AnimationInterface animationInterface){
		this.fadeShape = new ShapeObject(new Point(0,80), game.getSize(), 0, game);
		this.fadeShape.setColor(color);
		this.fadeShape.setAlpha(0.0f);
		Animation fade = new FadeAnimation(1.0f);
		fade.setSpeed(Speed.SLOW);
		game.animator.animate(this.fadeShape, fade, animationInterface);
	}

	protected boolean isLoaded() {
		return this.loaded;
	}

	protected void screenLoaded() {
		this.loaded = true;
	}

	protected void draw(Graphics2D g) {
		if(fadeShape != null){
			fadeShape.draw(g);
		}
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
