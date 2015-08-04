package engine.core;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import engine.animation.Animator;
import engine.controller.Controller;
import engine.controller.manager.ControllerManager;
import engine.interfaces.ControllerManagerInterface;
import engine.interfaces.GameLoopInterface;
import engine.timer.Timer;


public class Game implements GameLoopInterface, ControllerManagerInterface {

	private ControllerManager controllerManager;
	private Screen screen = null;
	private Screen globalScreen = null;
	private GameLoop gameLoop;
	public Animator animator;
	public Timer timer;


	public Game(int controllerType){
		this.gameLoop = new GameLoop(this);
		this.animator = new Animator();
		this.timer = new Timer();
		this.controllerManager = new ControllerManager(controllerType, this);
	}

	public Dimension getSize(){
		Dimension windowSize = this.gameLoop.graphics.getDimensions();
		return windowSize;
	}

	public Point getCenter(){
		Dimension windowSize = this.getSize();
		Point center = new Point(windowSize.width/2, windowSize.height/2);
		return center;
	}

	public void setGlobalScreen(Screen screen){
		screen.setGame(this);
		gameLoop.pause();
		this.globalScreen = screen;
		this.globalScreen.load();
		gameLoop.resume();
	}

	public Screen getGlobalScreen(){
		return this.globalScreen;
	}

	public float getResFactor(){
		return this.gameLoop.graphics.getResFactor();
	}

	public void changeScreen(Screen screen){
		if(screen != null){
			screen.setGame(this);
			gameLoop.pause();
			this.screen = screen;
			this.screen.load();
			gameLoop.resume();
		}
	}

	public void resume(){
		if(this.gameLoop != null){
			this.gameLoop.resume();
		}
	}

	public void pause(){
		if(this.gameLoop != null){
			this.gameLoop.pause();
		}
	}

	public Controller getController(int controllerID){
		return this.controllerManager.getController(controllerID);
	}

	@Override
	public void draw(Graphics2D g) {

		if(this.screen != null && this.screen.isLoaded()){
			this.screen.draw(g);
		}

		if(this.globalScreen != null && this.globalScreen.isLoaded()){
			this.globalScreen.draw(g);
		}

	}

	@Override
	public void update() {

		if(this.screen != null){
			this.screen.update();
		}

		if(this.globalScreen != null){
			this.globalScreen.update();
		}

		if(this.animator != null){
			animator.update();
		}

		if(this.timer != null){
			timer.update();
		}

	}

	@Override
	public void getPressedKey(Controller controller) {

		if(this.screen != null){
			this.screen.getPressedKey(controller);
		}

		if(this.globalScreen != null){
			this.globalScreen.getPressedKey(controller);
		}

	}

	@Override
	public void controllerConnected(Controller controller) {

		if(this.screen != null){
		this.screen.controllerConnected(controller);
		}

		if(this.globalScreen != null){
		this.globalScreen.controllerConnected(controller);
		}

	}

	@Override
	public void controllerDisconnected(Controller controller) {

		if(this.screen != null){
		this.screen.controllerDisconnected(controller);
		}

		if(this.globalScreen != null){
		this.globalScreen.controllerDisconnected(controller);
		}
	}

	@Override
	public void getUserInput(Controller controller) {

		if(this.screen != null){
			this.screen.getUserInput(controller);
		}

		if(this.globalScreen != null){
			this.globalScreen.getUserInput(controller);
		}

	}



}
