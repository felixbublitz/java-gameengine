package engine.animation;

import engine.datatypes.Interpolation;
import engine.interfaces.AnimationInterface;
import engine.objects2D.GameObject;

public abstract class Animation {

	private int id;
	private int repeat;
	private int progress;
	private AnimationInterface animationInterface;
	private AnimationInterface targetInterface;
	private int savedInt;

	protected GameObject gameObject;
	protected boolean animating;
	protected Speed speed;
	protected float[] value;

	public static enum Speed {VERY_SLOW, SLOW, MEDIUM, FAST, VERY_FAST};


	public void setAnimationInterface(AnimationInterface animationInterface) {
		this.animationInterface = animationInterface;
	}

	public void setTargetInterface(AnimationInterface animationInterface) {
		this.targetInterface = animationInterface;
	}

	public AnimationInterface getTargetInterface(){
		return this.targetInterface;
	}

	public AnimationInterface getAnimationInterface(){
		return this.animationInterface;
	}

	public GameObject getAnimatedObject() {
		return this.gameObject;
	}

	public void setIdentifier(int id){
		this.id = id;
	}

	public int getIdentifier(){
		return this.id;
	}

	public void setTargetObject(GameObject gameObject) {
		this.gameObject = gameObject;
		this.storeStartValue(gameObject);
	}

	protected abstract void storeStartValue(GameObject gameObject);

	public void applyInterpolation(float interpolationFactor) {
		if (!animating) {
			return;
		}
		Interpolation interpolation = this.calculateInterpolation(interpolationFactor);
		gameObject.setInterpolationValue(interpolation);
	}

	public void setSpeed(Speed speed) {
		this.speed = speed;
	}

	public void repeat(int repeat) {
		this.repeat = repeat;
	}

	public void update() {
		this.performAnimation();
	}

	public abstract void performAnimation();
	public abstract Interpolation calculateInterpolation(float interpolationFactor);


	protected void finishAnimation() {
		this.animating = false;
		if (this.progress < this.repeat || this.repeat == -1) {
			this.progress += 1;
			this.switchValues();
		} else {
			this.animationInterface.finished(this);
		}
	}

	protected int getSpeed() {
		switch (this.speed) {
		case VERY_SLOW:
			return 1;
		case SLOW:
			return 2;
		case MEDIUM:
			return 3;
		case FAST:
			return 5;
		case VERY_FAST:
			return 10;
		}
		return 0;
	}

	protected abstract void switchValues();

	public void saveInteger(int integer){
		this.savedInt = integer;
	}

	public int getSavedInteger(){
		return savedInt;
	}


}
