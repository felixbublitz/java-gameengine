package engine.animation;

import engine.datatypes.Interpolation;
import engine.objects2D.GameObject;

public class FadeAnimation extends Animation {

	private boolean fadeUp;
	private float startFade;

	public FadeAnimation(float fadeTo){
			this.speed = Speed.MEDIUM;
			this.value = new float[1];
			this.value[0] = fadeTo;
	}

	@Override
	public void performAnimation() {
		if (!animating) {
			if (this.value[0] < this.gameObject.getAlpha()) {
				this.fadeUp = false;
			} else {
				this.fadeUp = true;
			}
			animating = true;
		}

		if (this.fadeUp) {
			if (this.value[0] > this.gameObject.getAlpha()) {
				float newAlpha = this.gameObject.getAlpha() + (float)this.getSpeed() / (float)50;
				if (newAlpha > 1) {
					newAlpha = 1;
				}
				this.gameObject.setAlpha(newAlpha);
			} else {
				this.finishAnimation();
			}
		} else {
			if (this.value[0] < this.gameObject.getAlpha()) {
				float newAlpha = this.gameObject.getAlpha() - (float)this.getSpeed() / (float)50;
				if (newAlpha < 0) {
					newAlpha = 0;
				}
				this.gameObject.setAlpha(newAlpha);
			} else {
				this.finishAnimation();
			}
		}
	}

	@Override
	public Interpolation calculateInterpolation(float interpolationFactor) {
		Interpolation interpolation = new Interpolation();
		if (this.fadeUp) {
			int interpolatetValue = Math
					.round(gameObject.getAlpha() + (float)this.getSpeed() / (float)100 * interpolationFactor);
			if (interpolatetValue <= value[0]) {
				interpolation.addFadeInterpolation(interpolatetValue);
			} else {
				interpolation.addFadeInterpolation((int) value[0]);
			}
		} else {
			int interpolatetValue = Math
					.round(gameObject.getAlpha() - (float)this.getSpeed() / (float)100 * interpolationFactor);
			if (interpolatetValue >= value[0]) {
				interpolation.addFadeInterpolation(interpolatetValue);
			} else {
				interpolation.addFadeInterpolation((int) value[0]);
			}
		}
		return interpolation;
	}

	@Override
	protected void storeStartValue(GameObject gameObject) {
		this.startFade = gameObject.getAlpha();
	}

	@Override
	protected void switchValues() {

		float currentFade = this.value[0];

		this.value[0] = this.startFade;
		this.startFade = currentFade;
	}


}
