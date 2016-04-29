package engine.animation;

import engine.datatypes.Interpolation;
import engine.objects2D.GameObject;

public class RotateAnimation extends Animation {

	private boolean rotateClockwise;
	private int startRotation;

	public RotateAnimation(int rotateTo){
		this.speed = Speed.MEDIUM;
		this.value = new float[1];
		this.value[0] = rotateTo;
	}

	@Override
	public void performAnimation() {
		if (!animating) {
			if (this.value[0] < this.gameObject.getRotation()) {
				this.rotateClockwise = false;
			} else {
				this.rotateClockwise = true;
			}
			animating = true;
		}

		if (this.rotateClockwise) {
			if (this.value[0] > this.gameObject.getRotation()) {
				this.gameObject.setRotation(this.gameObject.getRotation() + this.getSpeed());
			} else {
				this.finishAnimation();
			}
		} else {
			if (this.value[0] < this.gameObject.getRotation()) {
				this.gameObject.setRotation(this.gameObject.getRotation() - this.getSpeed());
			} else {
				this.finishAnimation();
			}
		}

	}

	@Override
	public Interpolation calculateInterpolation(float interpolationFactor) {
		Interpolation interpolation = new Interpolation();
		if (this.rotateClockwise) {
			int interpolatetValue = Math.round(gameObject.getRotation() + this.getSpeed() * interpolationFactor);
			if (interpolatetValue <= value[0]) {
				interpolation.addRotateInterpolation(interpolatetValue);
			} else {
				interpolation.addRotateInterpolation((int) value[0]);
			}
		} else {
			int interpolatetValue = Math.round(gameObject.getRotation() - this.getSpeed() * interpolationFactor);
			if (interpolatetValue >= value[0]) {
				interpolation.addRotateInterpolation(interpolatetValue);
			} else {
				interpolation.addRotateInterpolation((int) value[0]);
			}
		}
		return interpolation;
	}

	@Override
	protected void storeStartValue(GameObject gameObject) {
		this.startRotation = gameObject.getRotation();
	}

	@Override
	protected void switchValues() {

		int currentRotation = (int) this.value[0];
		this.value[0] = this.startRotation;
		this.startRotation = currentRotation;

	}


}
