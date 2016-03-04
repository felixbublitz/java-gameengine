package engine.datatypes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import engine.interfaces.AnimationInterface;
import engine.objects2D.GameObject;

public class Animation {

	public final static int ANIMATION_SCALE = 01;
	public final static int ANIMATION_MOVE = 02;
	public final static int ANIMATION_ROTATE = 03;
	public final static int ANIMATION_FADE = 04;

	public final static int SPEED_VERY_SLOW = 1;
	public final static int SPEED_SLOW = 2;
	public final static int SPEED_MEDIUM = 3;
	public final static int SPEED_FAST = 5;
	public final static int SPEED_VERY_FAST = 10;


	public final static int REPEAT_FOREVER = -1;

	private int type;
	private int speed;
	private float[] value;
	private int repeat;
	private int progress;

	private GameObject gameObject;
	private boolean animating;
	private AnimationInterface animationInterface;

	private boolean horizontalFinished;
	private boolean verticalFinished;

	boolean scaleWidthPositive;
	boolean scaleHeightPositive;
	boolean rotateClockwise;
	boolean moveRight;
	boolean moveDown;
	boolean fadeUp;

	private Dimension startSize;
	private Point startPosition;
	private int startRotation;
	private float startFade;

	public void setAnimationInterface(AnimationInterface animationInterface){
		this.animationInterface = animationInterface;
	}

	public GameObject getAnimatedObject(){
		return this.gameObject;
	}

	public void setTargetObject(GameObject gameObject){
		this.gameObject = gameObject;
		this.storeStartValue(gameObject);
	}

	private void storeStartValue(GameObject gameObject){
		this.startPosition = gameObject.getPosition();
		this.startRotation = gameObject.getRotation();
		this.startSize = gameObject.getSize();
		this.startFade = gameObject.getColor().getAlpha();
	}

	public void applyInterpolation(float interpolationFactor){

		if(!animating){
			return;
		}

		Interpolation interpolation = new Interpolation();

		switch(this.type){
		case ANIMATION_SCALE:
			if(this.scaleWidthPositive){
				int interpolatetValue = Math.round((int)gameObject.getSize().getWidth() + this.getSpeed() * interpolationFactor);
				if(this.value[0] >= interpolatetValue){
					interpolation.addSizeWidthInterpolation(interpolatetValue);
				}else{
					interpolation.addSizeWidthInterpolation((int)value[0]);
				}
			}else{
				int interpolatetValue = Math.round((int)gameObject.getSize().getWidth() - this.getSpeed() * interpolationFactor);
				if(this.value[0] <= interpolatetValue){
					interpolation.addSizeWidthInterpolation(interpolatetValue);
				}else{
					interpolation.addSizeWidthInterpolation((int)value[0]);
				}
			}


			if(this.scaleHeightPositive){
				int interpolatetValue = Math.round((int)gameObject.getSize().getHeight() + this.getSpeed() * interpolationFactor);
				if(this.value[1] >= interpolatetValue){
					interpolation.addSizeHeightInterpolation(interpolatetValue);
				}else{
					interpolation.addSizeHeightInterpolation((int)value[1]);
				}
			}else{
				int interpolatetValue = Math.round((int)gameObject.getSize().getHeight() - this.getSpeed() * interpolationFactor);
				if(this.value[1]<= interpolatetValue){
					interpolation.addSizeHeightInterpolation(interpolatetValue);
				}else{
					interpolation.addSizeHeightInterpolation((int)value[1]);
				}
			}
			break;

		case ANIMATION_MOVE:

			if(this.moveRight){
				int interpolatetValue = Math.round(gameObject.getPosition().x + this.getSpeed() * interpolationFactor);
				if(this.value[0] >= interpolatetValue){
					interpolation.addPositionXInterpolation(interpolatetValue);
				}else{
					interpolation.addPositionXInterpolation((int)value[0]);
				}
			}else{
				int interpolatetValue = Math.round(gameObject.getPosition().x - this.getSpeed() * interpolationFactor);
				if(this.value[0] <= interpolatetValue){
					interpolation.addPositionXInterpolation(interpolatetValue);
				}else{
					interpolation.addPositionXInterpolation((int)value[0]);
				}
			}

			if(this.moveDown){
				int interpolatetValue = Math.round(gameObject.getPosition().y + this.getSpeed() * interpolationFactor);
				if(this.value[1] >= interpolatetValue){
					interpolation.addPositionYInterpolation(interpolatetValue);
				}else{
					interpolation.addPositionYInterpolation((int)value[1]);
				}
			}else{
				int interpolatetValue = Math.round(gameObject.getPosition().y - this.getSpeed() * interpolationFactor);
				if(this.value[1] <= interpolatetValue){
					interpolation.addPositionYInterpolation(interpolatetValue);
				}else{
					interpolation.addPositionYInterpolation((int)value[1]);
				}
			}

			break;

		case ANIMATION_ROTATE:
			if(this.rotateClockwise){
				int interpolatetValue = Math.round(gameObject.getRotation() + this.getSpeed() * interpolationFactor);
				if(interpolatetValue <= value[0]){
					interpolation.addRotateInterpolation(interpolatetValue);
				}else{
					interpolation.addRotateInterpolation((int)value[0]);
				}
			}else{
				int interpolatetValue = Math.round(gameObject.getRotation() - this.getSpeed() * interpolationFactor);
				if(interpolatetValue >= value[0]){
					interpolation.addRotateInterpolation(interpolatetValue);
				}else{
					interpolation.addRotateInterpolation((int)value[0]);
				}
			}
			break;
		case ANIMATION_FADE:
			if(this.fadeUp){
				int interpolatetValue = Math.round(gameObject.getColor().getAlpha() + this.getSpeed() * interpolationFactor);
				if(interpolatetValue <= value[0]){
					interpolation.addFadeInterpolation(interpolatetValue);
				}else{
					interpolation.addFadeInterpolation((int)value[0]);
				}
			}else{
				int interpolatetValue = Math.round(gameObject.getColor().getAlpha() - this.getSpeed() * interpolationFactor);
				if(interpolatetValue >= value[0]){
					interpolation.addFadeInterpolation(interpolatetValue);
				}else{
					interpolation.addFadeInterpolation((int)value[0]);
				}
			}
			break;
		}
		gameObject.setInterpolationValue(interpolation);
	}

	private void setSpeed(int speed){
		this.speed = speed;
	}

	public void createScaleAnimation(Dimension scaleTo, int speed){
		this.type = ANIMATION_SCALE;
		this.setSpeed(speed);

		this.value = new float[2];
		this.value[0] = scaleTo.width;
		this.value[1] = scaleTo.height;
	}

	public void createFadeAnimation(float fadeTo, int speed){
		this.type = ANIMATION_FADE;
		this.setSpeed(speed);

		this.value = new float[1];
		this.value[0] = fadeTo;
	}

	public void createMoveAnimation(Point moveTo, int speed){
		this.type = ANIMATION_MOVE;
		this.setSpeed(speed);

		this.value = new float[2];
		this.value[0] = moveTo.x;
		this.value[1] = moveTo.y;
	}

	public void createRotateAnimation(int rotateTo, int speed){
		this.type = ANIMATION_ROTATE;
		this.setSpeed(speed);

		this.value = new float[1];
		this.value[0] = rotateTo;
	}

	public void repeat(int repeat){
		this.repeat = repeat;
	}



	public void update(){

		switch(this.type){
		case ANIMATION_SCALE:
			this.performScaleAnimation();
			break;

		case ANIMATION_MOVE:
			this.performMoveAnimation();
			break;

		case ANIMATION_ROTATE:
			this.performRotateAnimation();
			break;
		case ANIMATION_FADE:
			this.performFadeAnimation();
			break;
		}
	}

	private void performFadeAnimation(){
		if(!animating){
			if(this.value[0] < this.gameObject.getColor().getAlpha()){
				this.fadeUp = false;
			}else{
				this.fadeUp = true;
			}
			animating = true;
		}

		if(this.fadeUp){
			if(this.value[0] > this.gameObject.getColor().getAlpha()){
				Color color = this.gameObject.getColor();
				int newAlpha = color.getAlpha() + this.getSpeed();
				if(newAlpha > 255){
					newAlpha = 255;
				}
				this.gameObject.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),newAlpha));
			}else{
				this.finishAnimation();
			}
		}else{
			if(this.value[0] < this.gameObject.getColor().getAlpha()){
				Color color = this.gameObject.getColor();
				int newAlpha = color.getAlpha() - this.getSpeed();
				if(newAlpha < 0){
					newAlpha = 0;
				}
				this.gameObject.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),newAlpha));
			}else{
				this.finishAnimation();
			}
		}
	}

	private void performScaleAnimation(){

		if(!animating){
			if(this.value[0] < this.gameObject.getSize().width){
				this.scaleWidthPositive = false;
			}else{
				this.scaleWidthPositive = true;
			}

			if(this.value[1] < this.gameObject.getSize().height){
				this.scaleHeightPositive = false;
			}else{
				this.scaleHeightPositive = true;
			}
			animating = true;
		}

		if(this.scaleWidthPositive){
			if(this.value[0] > this.gameObject.getSize().width){
				this.gameObject.setSize(new Dimension(this.gameObject.getSize().width + this.getSpeed(),this.gameObject.getSize().height));
			}else{
				this.horizontalFinished = true;
			}
		}else{
			if(this.value[0] < this.gameObject.getSize().width){
				this.gameObject.setSize(new Dimension(this.gameObject.getSize().width - this.getSpeed(),this.gameObject.getSize().height));
			}else{
				this.horizontalFinished = true;
			}
		}


		if(this.scaleHeightPositive){
			if(this.value[0] > this.gameObject.getSize().height){
				this.gameObject.setSize(new Dimension(this.gameObject.getSize().width,this.gameObject.getSize().height + this.getSpeed()));
			}else{
				this.verticalFinished = true;
			}
		}else{
			if(this.value[0] < this.gameObject.getSize().height){
				this.gameObject.setSize(new Dimension(this.gameObject.getSize().width,this.gameObject.getSize().height - this.getSpeed()));
			}else{
				this.verticalFinished = true;
			}
		}

		if(this.horizontalFinished && this.verticalFinished){
			this.verticalFinished = false;
			this.horizontalFinished = false;
			this.finishAnimation();
		}

	}

	private void finishAnimation(){
		this.animating = false;
		if(this.progress < this.repeat || this.repeat == REPEAT_FOREVER){
			this.progress +=1;
			this.switchValues();
		}else{
			this.animationInterface.finished(this);
		}
	}

	private int getSpeed(){
		return this.speed;
	}

	private void switchValues(){
		switch (this.type){
		case ANIMATION_MOVE:
			int currentPositionX = (int)this.value[0];
			int currentPositionY = (int)this.value[1];

			this.value[0] = this.startPosition.x;
			this.value[1] = this.startPosition.y;
			this.startPosition.x = currentPositionX;
			this.startPosition.y = currentPositionY;



			break;
		case ANIMATION_SCALE:
			int currentHeight = (int)this.value[0];
			int currentWidth = (int)this.value[1];

			this.value[0] = this.startSize.width;
			this.value[1] = this.startSize.height;

			this.startSize = new Dimension(currentHeight,currentWidth);
		break;

		case ANIMATION_FADE:
			float currentFade = this.value[0];

			this.value[0] = this.startFade;
			this.startFade = currentFade;
		break;

		case ANIMATION_ROTATE:
			int currentRotation = (int)this.value[0];
			this.value[0] = this.startRotation;
			this.startRotation = currentRotation;
			break;
		}
	}

	private void performMoveAnimation(){

		if(!animating){

			if(this.value[0] < this.gameObject.getPosition().getX()){
				this.moveRight = false;
			}else{
				this.moveRight = true;
			}

			if(this.value[0] < this.gameObject.getPosition().getY()){
				this.moveDown = false;
			}else{
				this.moveDown = true;
			}

			animating = true;
		}


		if(this.moveRight){
			if(this.value[0] > this.gameObject.getPosition().x){
				this.gameObject.setPosition(new Point(this.gameObject.getPosition().x + this.getSpeed(),this.gameObject.getPosition().y));
			}else{
				this.horizontalFinished = true;
			}
		}else{
			if(this.value[0] < this.gameObject.getPosition().x){
				this.gameObject.setPosition(new Point(this.gameObject.getPosition().x - this.getSpeed(),this.gameObject.getPosition().y));
			}else{
				this.horizontalFinished = true;
			}
		}



		if(this.moveDown){
			if(this.value[1] > this.gameObject.getPosition().y){
				this.gameObject.setPosition(new Point(this.gameObject.getPosition().x,this.gameObject.getPosition().y + this.getSpeed()));

			}else{
				this.verticalFinished = true;
			}
		}else{
			if(this.value[1] < this.gameObject.getPosition().y){
				this.gameObject.setPosition(new Point(this.gameObject.getPosition().x,this.gameObject.getPosition().y - this.getSpeed()));
			}else{
				this.verticalFinished = true;
			}
		}

		if(this.horizontalFinished && this.verticalFinished){
			this.verticalFinished = false;
			this.horizontalFinished = false;
			this.finishAnimation();
		}


	}

	private void performRotateAnimation(){

		if(!animating){
			if(this.value[0] < this.gameObject.getRotation()){
				this.rotateClockwise = false;
			}else{
				this.rotateClockwise = true;
			}
			animating = true;
		}

		if(this.rotateClockwise){
			if(this.value[0] > this.gameObject.getRotation()){
				this.gameObject.setRotation(this.gameObject.getRotation() + this.getSpeed());
			}else{
				this.finishAnimation();
			}
		}else{
			if(this.value[0] < this.gameObject.getRotation()){
				this.gameObject.setRotation(this.gameObject.getRotation() - this.getSpeed());
			}else{
				this.finishAnimation();
			}
		}


	}



}
