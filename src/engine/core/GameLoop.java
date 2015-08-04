package engine.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import engine.interfaces.GameLoopInterface;
import engine.interfaces.GraphicsInterface;

public class GameLoop implements Runnable, GraphicsInterface {
	
	GameLoopInterface gameLoopInterface;
	Graphics graphics;
	
	private boolean destroyed;
	private boolean enabled = true;
	private boolean drawFPS = false;
	
	private long lastUpdateTime = 0;
	private long lastTime = 0;

	private long startTime;
	
	private long fps;
	private int frame = 0;
	
	private static final float MAX_FRAMERATE = 60;
	
	public GameLoop(GameLoopInterface gameLoopInterface){
		graphics = new Graphics(this);
		this.gameLoopInterface = gameLoopInterface;
		
		this.startTime = System.currentTimeMillis();
		new Thread(this).start();
	}
	
	public void pause(){
		enabled = false;
	}
	
	public void resume(){
		enabled = true;
	}
	
	public void stop(){
		destroyed = true;
	}

	@Override
	public void run() {
		while(!destroyed){
			try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
			if(enabled){
				if(this.updateRequired()){
					this.gameLoopInterface.update();
					this.graphics.requestUpdate();
				}
			}
		}
	}
	
	private boolean updateRequired(){
		if(this.getRuntime() >= 1000){
			fps = frame;
			frame = 0;
		}
		
		long a = this.getLastUpdateTime();
		if(a >= 1/MAX_FRAMERATE*1000){
			this.lastUpdateTime -= 1/MAX_FRAMERATE*1000;
			this.frame++;
			return true;
		}
		return false;
	}
	
	private long getLastUpdateTime(){
		long currentTime = System.currentTimeMillis();
		if(this.lastTime != 0){
			this.lastUpdateTime += currentTime - this.lastTime;
		}
		this.lastTime = currentTime;

		return this.lastUpdateTime;
	}
	
	private long getRuntime(){
		long time = System.currentTimeMillis() - this.startTime;
		
		if(time >= 1000){
			this.startTime = System.currentTimeMillis();
		}
		return time;
	}

	@Override
	public void Draw(Graphics2D g) {
		gameLoopInterface.draw(g);
		  if(drawFPS){
			  g.setColor(Color.BLACK);
			  g.setFont(new Font("Arial", Font.PLAIN, 12));
			  g.drawString("FPS:" + this.fps, 10, 20);
			  
			  
		  }
	}

}
