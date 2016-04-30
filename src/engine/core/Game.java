package engine.core;

import java.awt.Graphics2D;

import engine.animation.Animator;
import engine.controller.Controller;
import engine.controller.manager.handler.ConnectionHandler;
import engine.gamedata.GameData;
import engine.localisation.Localisation;
import engine.mediaplayer.MPlayer;
import engine.timer.TimerManager;

/**
 * the game class is used to initiate the game engine for creating 2d games
 */
public class Game {

	private ControllerManager controllerManager;
	private Screen screen;
	private Screen overlayScreen;
	private Screen globalScreen;

	private Animator animator;
	private TimerManager timer;
	private MPlayer mediaPlayer;
	private Localisation localisation;
	private Support support;
	private GameData gameData;

	private GameLoop gameLoop;
	private boolean paused;
	private boolean debug;

	/**
	 * Constructs a new Game
	 * @param connectionHandler the connection handler for the main input
	 */
	public Game(ConnectionHandler connectionHandler) {
		this.gameLoop = new GameLoop(this);
		this.animator = new Animator();
		this.timer = new TimerManager(this);
		this.mediaPlayer = new MPlayer();
		this.localisation = new Localisation();
		this.controllerManager = new ControllerManager(connectionHandler, this);
		this.support = new Support();
		this.gameData = new GameData();
	}

	void draw(Graphics2D g, float interpolationFactor) {

		if (this.screen != null && this.screen.isLoaded()) {
			this.screen.draw(g);
		}

		if (this.globalScreen != null && this.globalScreen.isLoaded()) {
			this.globalScreen.draw(g);
		}

		if (this.overlayScreen != null && this.overlayScreen.isLoaded()) {
			this.overlayScreen.draw(g);
			return;
		}

		if (animator != null) {
			this.animator.applyInterpolation(interpolationFactor);
		}

	}

	void update() {

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

	void keyPressed(Controller controller) {

		if (this.globalScreen != null) {
			this.globalScreen.keyPressed(controller);
		}

		if (this.overlayScreen != null) {
			this.overlayScreen.keyPressed(controller);
			return;
		}

		if (this.screen != null) {
			this.screen.keyPressed(controller);
		}

	}


	void controllerConnected(Controller controller) {

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


	void controllerDisconnected(Controller controller) {

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


	void inputReceived(Controller controller) {

		if (this.globalScreen != null) {
			this.globalScreen.inputReceived(controller);
		}

		if (this.overlayScreen != null) {
			this.overlayScreen.inputReceived(controller);
			return;
		}

		if (this.screen != null) {
			this.screen.inputReceived(controller);
		}

	}

	/**
	 * get the graphics object to view and change everything belongs to the graphics of the game
	 * @return the game graphics object
	 */
	public Graphics getGraphics(){
		return this.gameLoop.graphics;
	}

	/**
	 * get the GameData object to load and save game states
	 * @return the GameData object
	 */
	public GameData getGameData(){
		return this.gameData;
	}

	/**
	 * get the support object to use helper methods
	 * @return the game support object
	 */
	public Support getSupport(){
		return this.support;
	}

	/**
	 * get the animator object to animate game objects
	 * @return the game animator object
	 */
	public Animator getAnimator(){
		return this.animator;
	}

	/**
	 * get the timer object to create timers
	 * @return the game timer object
	 */
	public TimerManager getTimer(){
		return this.timer;
	}

	public void setDebugMode(){
		this.debug = true;
	}

	public boolean isDebug(){
		return this.debug;
	}

	/**
	 * show an overlay in front of your current screen and global screen
	 * @param screen a screen to show as overlay
	 */
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

	/**
	 * get the current overlay screen
	 * @return the current overlay screen
	 */
	public Screen getOberlayScreen(){
		return this.overlayScreen;
	}

	/**
	 * hide the overlay screen
	 *
	 */
	public void hideOverlayScreen() {
		this.overlayScreen = null;
	}

	/**
	 * set a global screen that is always visible
	 * @param screen a screen to use as global screen
	 */
	public void setGlobalScreen(Screen screen) {
		screen.setGame(this);
		gameLoop.pause();
		this.globalScreen = screen;
		this.globalScreen.load();
		gameLoop.resume();
	}

	/**
	 * get the global screen
	 * @return the global screen
	 */
	public Screen getGlobalScreen() {
		return this.globalScreen;
	}

	/**
	 * use the localisation object to lcalize your game
	 * @return the game localisation object
	 */
	public Localisation getLocalization(){
		return this.localisation;
	}


	/**
	 * use the mediaplayer object to play music and sound
	 * @return the game mediaplayer object
	 */
	public MPlayer getMediaPlayer(){
		return this.mediaPlayer;
	}

	/**
	 * change the screen
	 * @param screen the screen to show
	 */
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

	/**
	 * get the current screen
	 * @return the current screen
	 */

	public Screen getScreen(){
		return this.screen;
	}

	/**
	 * check if game is paused
	 * @return a boolean that shows if the game is paused
	 */
	public boolean isPaused(){
		return this.paused;
	}

	/**
	 * resume the paused game
	 */
	public void resume() {
		if (this.gameLoop != null) {
			paused = false;
			this.gameLoop.resume();
			this.mediaPlayer.resumeAll();
			this.timer.resumeAll();
		}
	}

	/**
	 * pause the game
	 */
	public void pause() {
		if (this.gameLoop != null) {
			paused = true;
			this.gameLoop.pause();
			this.mediaPlayer.pauseAll();
			this.timer.pauseAll();
		}
	}

	/**
	 * get the current game updates per second
	 * @return the current ups
	 */
	public int getUPS(){
		return gameLoop.getUPS();
	}

	/**
	 * use the controllerManager to manage the user input
	 * @return the game controllerManager object
	 */

	public ControllerManager getControllerManager() {
		return this.controllerManager;
	}


}
