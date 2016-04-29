package engine.animation;

import java.awt.Point;

import engine.datatypes.Interpolation;
import engine.objects2D.GameObject;

public class MoveAnimation extends Animation {

	private boolean horizontalFinished;
	private boolean verticalFinished;
	private boolean moveRight;
	private boolean moveDown;
	private Point startPosition;

	public MoveAnimation(Point moveTo){
	this.speed = Speed.MEDIUM;
	this.value = new float[2];
	this.value[0] = moveTo.x;
	this.value[1] = moveTo.y;

	}

	@Override
	public void performAnimation() {
		if (!animating) {

			if (this.value[0] < this.gameObject.getPosition().getX()) {
				this.moveRight = false;
			} else {
				this.moveRight = true;
			}

			if (this.value[1] < this.gameObject.getPosition().getY()) {
				this.moveDown = false;
			} else {
				this.moveDown = true;
			}

			animating = true;
		}

		if (this.moveRight) {
			if (this.value[0] > this.gameObject.getPosition().x) {
				this.gameObject.setPosition(
						new Point(this.gameObject.getPosition().x + this.getSpeed(), this.gameObject.getPosition().y));
			} else {
				this.horizontalFinished = true;
			}
		} else {
			if (this.value[0] < this.gameObject.getPosition().x) {
				this.gameObject.setPosition(
						new Point(this.gameObject.getPosition().x - this.getSpeed(), this.gameObject.getPosition().y));
			} else {
				this.horizontalFinished = true;
			}
		}

		if (this.moveDown) {
			if (this.value[1] > this.gameObject.getPosition().y) {
				this.gameObject.setPosition(
						new Point(this.gameObject.getPosition().x, this.gameObject.getPosition().y + this.getSpeed()));

			} else {
				this.verticalFinished = true;
			}
		} else {
			if (this.value[1] < this.gameObject.getPosition().y) {
				this.gameObject.setPosition(
						new Point(this.gameObject.getPosition().x, this.gameObject.getPosition().y - this.getSpeed()));
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
		if (this.moveRight) {
			int interpolatetValue = Math.round(gameObject.getPosition().x + this.getSpeed() * interpolationFactor);
			if (this.value[0] >= interpolatetValue) {
				interpolation.addPositionXInterpolation(interpolatetValue);
			} else {
				interpolation.addPositionXInterpolation((int) value[0]);
			}
		} else {
			int interpolatetValue = Math.round(gameObject.getPosition().x - this.getSpeed() * interpolationFactor);
			if (this.value[0] <= interpolatetValue) {
				interpolation.addPositionXInterpolation(interpolatetValue);
			} else {
				interpolation.addPositionXInterpolation((int) value[0]);
			}
		}

		if (this.moveDown) {
			int interpolatetValue = Math.round(gameObject.getPosition().y + this.getSpeed() * interpolationFactor);
			if (this.value[1] >= interpolatetValue) {
				interpolation.addPositionYInterpolation(interpolatetValue);
			} else {
				interpolation.addPositionYInterpolation((int) value[1]);
			}
		} else {
			int interpolatetValue = Math.round(gameObject.getPosition().y - this.getSpeed() * interpolationFactor);
			if (this.value[1] <= interpolatetValue) {
				interpolation.addPositionYInterpolation(interpolatetValue);
			} else {
				interpolation.addPositionYInterpolation((int) value[1]);
			}
		}
		return interpolation;
	}

	@Override
	protected void storeStartValue(GameObject gameObject) {
		this.startPosition = gameObject.getPosition();
	}

	@Override
	protected void switchValues() {
			int currentPositionX = (int) this.value[0];
			int currentPositionY = (int) this.value[1];
			this.value[0] = this.startPosition.x;
			this.value[1] = this.startPosition.y;
			this.startPosition.x = currentPositionX;
			this.startPosition.y = currentPositionY;
	}

}
