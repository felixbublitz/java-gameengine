package engine.timer;

import engine.interfaces.TimerInterface;

public class Timer {
	private int elapsedTicks = 0;
	private int destinationTicks;
	private boolean enabled;
	public TimerInterface timerInterface;
	private TimerInterface timerInterfaceManager;
	private int ups = 0;

	public Timer(TimerInterface timerInterfaceManager, int ups) {
		this.ups = ups;
		this.timerInterfaceManager = timerInterfaceManager;
	}

	public void update() {
		if (enabled) {
			if (elapsedTicks < destinationTicks) {
				elapsedTicks += 1;
			} else {
				enabled = false;
				elapsedTicks = 0;
				destinationTicks = 0;
				timerInterfaceManager.finished(this);
			}
		}
	}

	public void stop() {
		elapsedTicks = 0;
		enabled = false;
	}

	public void start(int seconds, TimerInterface timerInterface) {
		this.timerInterface = timerInterface;
		this.destinationTicks = seconds * ups;
		this.enabled = true;
	}
}
