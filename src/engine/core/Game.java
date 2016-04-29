package engine.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import javax.swing.JFrame;

import engine.animation.Animator;
import engine.controller.Controller;
import engine.controller.manager.ControllerManager;
import engine.controller.manager.handler.ConnectionHandler;
import engine.datatypes.Ressource;
import engine.interfaces.ControllerManagerInterface;
import engine.interfaces.GameLoopInterface;
import engine.localisation.Localisation;
import engine.mediaplayer.MPlayer;
import engine.timer.TimerManager;

public class Game implements GameLoopInterface, ControllerManagerInterface {

	private ControllerManager controllerManager;
	private Screen screen;
	private Screen overlayScreen;
	private Screen globalScreen;

	public Animator animator;
	public TimerManager timer;
	public MPlayer mediaPlayer;
	public Localisation localisation;

	private GameLoop gameLoop;
	private boolean paused;
	private boolean testCase;

	public int getUPS() {
		return gameLoop.getUPS();
	}

	public boolean isTestCase(){
		return testCase;
	}

	public void setTestCase(boolean testCase){
		this.testCase = testCase;
	}

	public JFrame getFrame() {
		return gameLoop.getGraphics().getFrame();
	}

	public void toggleFullscreen(boolean toggleFS){

		if(toggleFS){
			com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this.getFrame(),true);
			com.apple.eawt.Application.getApplication().requestToggleFullScreen(this.getFrame());
			this.getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);


		}else{
			this.getFrame().setExtendedState(JFrame.NORMAL);
		}


	}

	public Graphics getGraphics(){
		return this.gameLoop.graphics;
	}

	public void setBackground(Ressource ressource) {
		if (gameLoop != null) {
			gameLoop.graphics.setBackground(ressource);
		}
	}

	public void setBackgroudColor(Color color) {
		if (gameLoop != null) {
			gameLoop.graphics.setBackgroundColor(color);
		}
	}

	public int createRandom(int min, int max) {
		if(min < 0){
			System.err.println("Create Random: min < 0");
			min = 0;
		}
		if(max < 0){
			System.err.println("Create Random: max < 0");
			max = 0;
		}

		if(max < min){
			System.err.println("Create Random: max < min");
			return 0;
		}

		return new Random().nextInt((max - min) + 1) + min;
	}

	public Game(ConnectionHandler connectionHandler) {
		this.gameLoop = new GameLoop(this);
		this.animator = new Animator();
		this.timer = new TimerManager(this);
		this.mediaPlayer = new MPlayer();
		this.localisation = new Localisation();
		this.controllerManager = new ControllerManager(connectionHandler, this);
	}

	public Game(ConnectionHandler connectionHandler, int resFactor) {
		this.gameLoop = new GameLoop(this, resFactor);
		this.animator = new Animator();
		this.timer = new TimerManager(this);
		this.mediaPlayer = new MPlayer();
		this.controllerManager = new ControllerManager(connectionHandler, this);
	}

	public Dimension getSize() {
		Dimension windowSize = this.gameLoop.graphics.getDimensions();
		return windowSize;
	}

	public Point getCenter() {
		Dimension windowSize = this.getSize();
		Point center = new Point(windowSize.width / 2, windowSize.height / 2);
		return center;
	}

	public Screen getScreen() {
		return this.screen;
	}

	public void showOverlayScreen(Screen screen) {
		if (screen != null) {
			screen.setGame(this);
			gameLoop.pause();
			this.overlayScreen = screen;
			this.overlayScreen.load();
			gameLoop.resume();
		}

		this.overlayScreen = screen;
	}

	public void hideOverlayScreen() {
		this.overlayScreen = null;
	}

	public void setGlobalScreen(Screen screen) {
		screen.setGame(this);
		gameLoop.pause();
		this.globalScreen = screen;
		this.globalScreen.load();
		gameLoop.resume();
	}

	public Screen getGlobalScreen() {
		return this.globalScreen;
	}

	public float getResFactor() {
		return this.gameLoop.graphics.getResFactor();
	}

	public void changeScreen(Screen screen) {
		if (screen != null) {
			screen.setGame(this);
			gameLoop.pause();
			if (this.screen != null) {
				this.screen.unload();
			}
			this.screen = screen;
			this.screen.load();
			gameLoop.resume();
		}
	}

	public boolean isPaused(){
		return this.paused;
	}

	public void resume() {
		if (this.gameLoop != null) {
			paused = false;
			this.gameLoop.resume();
			this.mediaPlayer.resumeAll();
			this.timer.resumeAll();
		}
	}

	public void pause() {
		if (this.gameLoop != null) {
			paused = true;
			this.gameLoop.pause();
			this.mediaPlayer.pauseAll();
			this.timer.pauseAll();
		}
	}

	public ControllerManager getControllerManager() {
		return this.controllerManager;
	}

	public Controller getController(int controllerID) {
		return this.controllerManager.getController(controllerID);
	}

	@Override
	public void draw(Graphics2D g, float interpolationFactor) {

		if (this.globalScreen != null && this.globalScreen.isLoaded()) {
			this.globalScreen.draw(g);
		}

		if (this.screen != null && this.screen.isLoaded()) {
			this.screen.draw(g);
		}

		if (this.overlayScreen != null && this.overlayScreen.isLoaded()) {
			this.overlayScreen.draw(g);
			return;
		}

		if (animator != null) {
			this.animator.applyInterpolation(interpolationFactor);
		}



	}

	@Override
	public void update() {

		if (this.globalScreen != null) {
			this.globalScreen.update();
		}

		if (this.overlayScreen != null) {
			this.overlayScreen.update();
			return;
		}

		if (this.screen != null) {
			this.screen.update();
		}

		if (this.animator != null) {
			animator.update();
		}

		if (this.timer != null) {
			timer.update();
		}

	}

	@Override
	public void getPressedKey(Controller controller) {

		if (this.globalScreen != null) {
			this.globalScreen.getPressedKey(controller);
		}

		if (this.overlayScreen != null) {
			this.overlayScreen.getPressedKey(controller);
			return;
		}

		if (this.screen != null) {
			this.screen.getPressedKey(controller);
		}

	}

	@Override
	public void controllerConnected(Controller controller) {

		if (this.screen != null) {
			this.screen.controllerConnected(controller);
		}

		if (this.overlayScreen != null) {
			this.overlayScreen.controllerConnected(controller);
			return;
		}

		if (this.globalScreen != null) {
			this.globalScreen.controllerConnected(controller);
		}

	}

	@Override
	public void controllerDisconnected(Controller controller) {

		if (this.globalScreen != null) {
			this.globalScreen.controllerDisconnected(controller);
		}

		if (this.overlayScreen != null) {
			this.overlayScreen.controllerDisconnected(controller);
			return;
		}

		if (this.screen != null) {
			this.screen.controllerDisconnected(controller);
		}

	}

	@Override
	public void getUserInput(Controller controller) {

		if (this.globalScreen != null) {
			this.globalScreen.getUserInput(controller);
		}

		if (this.overlayScreen != null) {
			this.overlayScreen.getUserInput(controller);
			return;
		}

		if (this.screen != null) {
			this.screen.getUserInput(controller);
		}

	}

}
