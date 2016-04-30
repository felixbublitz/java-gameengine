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

public abstract class Screen {
	private Game game;
	private boolean loaded;
	private ShapeObject fadeShape;

	void setGame(Game game) {
		this.game = game;
	}

	void load() {
		this.onLoad();
		this.loaded = true;
	}

	void unload() {
		game.getAnimator().stopAll();
		game.getTimer().stopAll();
		this.onUnload();
	}

	void draw(Graphics2D g) {
		if(fadeShape != null){
			fadeShape.draw(g);
		}
		this.onDraw(g);
	}

	void update(){
		this.onUpdate();
	}

	void controllerConnected(Controller controller){
		this.onControllerConnected(controller);
	}

	void controllerDisconnected(Controller controller){
		this.onControllerDisconnected(controller);
	}

	void inputReceived(Controller controller){
		this.onInputReceived(controller);
	}

	void keyPressed(Controller controller){
		this.onKeyPressed(controller);
	}

	public void fadeToColor(Color color, AnimationInterface animationInterface){
		this.fadeShape = new ShapeObject(new Point(0,0), game.getGraphics().getScreenSize(), 0, game);
		this.fadeShape.setColor(color);
		this.fadeShape.setAlpha(0.0f);
		Animation fade = new FadeAnimation(1.0f);
		fade.setSpeed(Speed.SLOW);
		game.getAnimator().animate(this.fadeShape, fade, animationInterface);
	}

	protected boolean isLoaded() {
		return this.loaded;
	}

	protected Game getGame(){
		return this.game;
	}

	public abstract String getTitle();
	protected abstract void onLoad();
	protected abstract void onUnload();
	protected abstract void onDraw(Graphics2D graphics);
	protected abstract void onUpdate();
	protected abstract void onKeyPressed(Controller controller);
	protected abstract void onInputReceived(Controller controller);
	protected abstract void onControllerDisconnected(Controller controller);
	protected abstract void onControllerConnected(Controller controller);


}
