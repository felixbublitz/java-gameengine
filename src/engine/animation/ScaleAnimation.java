package engine.animation;

import java.awt.Dimension;

import engine.datatypes.Interpolation;
import engine.objects2D.GameObject;

public class ScaleAnimation extends Animation {

	private boolean horizontalFinished;
	private boolean verticalFinished;
	private boolean scaleWidthPositive;
	private boolean scaleHeightPositive;
	private Dimension startSize;

	public ScaleAnimation(Dimension scaleTo){
		this.speed = Speed.MEDIUM;
		this.value = new float[2];
		this.value[0] = scaleTo.width;
		this.value[1] = scaleTo.height;
	}

	@Override
	public void performAnimation() {
		if (!animating) {
			if (this.value[0] < this.gameObject.getSize().width) {
				this.scaleWidthPositive = false;
			} else {
				this.scaleWidthPositive = true;
			}

			if (this.value[1] < this.gameObject.getSize().height) {
				this.scaleHeightPositive = false;
			} else {
				this.scaleHeightPositive = true;
			}
			animating = true;
		}

		if (this.scaleWidthPositive) {
			if (this.value[0] > this.gameObject.getSize().width) {
				this.gameObject.setSize(new Dimension(this.gameObject.getSize().width + this.getSpeed(),
						this.gameObject.getSize().height));
			} else {
				this.horizontalFinished = true;
			}
		} else {
			if (this.value[0] < this.gameObject.getSize().width) {
				this.gameObject.setSize(new Dimension(this.gameObject.getSize().width - this.getSpeed(),
						this.gameObject.getSize().height));
			} else {
				this.horizontalFinished = true;
			}
		}

		if (this.scaleHeightPositive) {
			if (this.value[1] > this.gameObject.getSize().height) {
				this.gameObject.setSize(new Dimension(this.gameObject.getSize().width,
						this.gameObject.getSize().height + this.getSpeed()));
			} else {
				this.verticalFinished = true;
			}
		} else {
			if (this.value[1] < this.gameObject.getSize().height) {
				this.gameObject.setSize(new Dimension(this.gameObject.getSize().width,
						this.gameObject.getSize().height - this.getSpeed()));
			} else {
				this.verticalFinished = true;
			}
		}

		if (this.horizontalFinished && this.verticalFinished) {
			this.verticalFinished = false;
			this.horizontalFinished = false;
			this.finishAnimation();
		}
	}

	@Override
	public Interpolation calculateInterpolation(float interpolationFactor) {
		Interpolation interpolation = new Interpolation();

		if (this.scaleWidthPositive) {
			int interpolatetValue = Math
					.round((int) gameObject.getSize().getWidth() + this.getSpeed() * interpolationFactor);
			if (this.value[0] >= interpolatetValue) {
				interpolation.addSizeWidthInterpolation(interpolatetValue);
			} else {
				interpolation.addSizeWidthInterpolation((int) value[0]);
			}
		} else {
			int interpolatetValue = Math
					.round((int) gameObject.getSize().getWidth() - this.getSpeed() * interpolationFactor);
			if (this.value[0] <= interpolatetValue) {
				interpolation.addSizeWidthInterpolation(interpolatetValue);
			} else {
				interpolation.addSizeWidthInterpolation((int) value[0]);
			}
		}

		if (this.scaleHeightPositive) {
			int interpolatetValue = Math
					.round((int) gameObject.getSize().getHeight() + this.getSpeed() * interpolationFactor);
			if (this.value[1] >= interpolatetValue) {
				interpolation.addSizeHeightInterpolation(interpolatetValue);
			} else {
				interpolation.addSizeHeightInterpolation((int) value[1]);
			}
		} else {
			int interpolatetValue = Math
					.round((int) gameObject.getSize().getHeight() - this.getSpeed() * interpolationFactor);
			if (this.value[1] <= interpolatetValue) {
				interpolation.addSizeHeightInterpolation(interpolatetValue);
			} else {
				interpolation.addSizeHeightInterpolation((int) value[1]);
			}
		}

		return interpolation;

	}

	@Override
	protected void storeStartValue(GameObject gameObject) {
		this.startSize = gameObject.getSize();
	}

	@Override
	protected void switchValues() {
		int currentHeight = (int) this.value[0];
		int currentWidth = (int) this.value[1];
		this.value[0] = this.startSize.width;
		this.value[1] = this.startSize.height;
		this.startSize = new Dimension(currentHeight, currentWidth);
	}

}
