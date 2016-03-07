package engine.timer;

import java.util.ArrayList;

import engine.core.Game;
import engine.interfaces.TimerInterface;

public class TimerManager implements TimerInterface {
	private ArrayList<Timer> timer;
	private int ups;
	private boolean paused;

	public TimerManager(Game game) {
		this.ups = game.getUPS();
		this.timer = new ArrayList<Timer>();
	}

	public void update() {

		if (this.timer != null && this.timer.size() > 0 && paused == false) {
			for (int i = 0; i < this.timer.size(); i++) {
				timer.get(i).update();
			}
		}

	}

	public void stopAll() {
		if (timer != null && timer.size() > 0) {
			for (Timer timer : this.timer) {
				timer.stop();
			}
			this.timer.clear();
		}

	}

	public void pauseAll() {
		paused = true;
	}

	public void resumeAll() {
		paused = false;
	}

	public void stop(Timer timer) {
		int timerID = this.timer.indexOf(timer);
		if (timerID > -1) {
			this.timer.get(timerID).stop();
			this.timer.remove(timerID);
		}
	}

	public Timer start(int seconds, TimerInterface timerInterface) {
		Timer timer = new Timer(this, ups);
		timer.start(seconds, timerInterface);
		this.timer.add(timer);
		return timer;
	}

	@Override
	public void finished(Timer timer) {
		this.stop(timer);
		timer.timerInterface.finished(timer);
	}

}
