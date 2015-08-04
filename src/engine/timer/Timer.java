package engine.timer;

import engine.interfaces.TimerInterface;

public class Timer {

	private int elapsedTicks = 0;
	private int destinationTicks;
	private boolean enabled;
	private final static int FRAMESPERSECOND = 60;
	private TimerInterface timerInterface;

	public void update(){
		if(enabled){
		if(elapsedTicks < destinationTicks){
			elapsedTicks += 1;
		}else{
			enabled = false;
			elapsedTicks= 0;
			destinationTicks = 0;
			timerInterface.finished();
		}
		}
	}

	public void start(int seconds, TimerInterface timerInterface){
		this.timerInterface = timerInterface;
		this.destinationTicks = seconds * FRAMESPERSECOND;
		this.enabled = true;
	}


}
