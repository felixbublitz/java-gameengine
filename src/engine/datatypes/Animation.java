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

	public final static int SPEED_VERY_SLOW = 11;
	public final static int SPEED_SLOW = 12;
	public final static int SPEED_MEDIUM = 13;
	public final static int SPEED_FAST = 14;
	public final static int SPEED_VERY_FAST = 15;
	
	public final static int REPEAT_FOREVER = -1;
	
	private int type;
	private int speed;
	private float[] value;
	private int repeat;
	private int progress;
	
	int supressFrames;
	int supressedFrames;
	
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
	
	private void setSpeed(int speed){
		switch(speed){
		case SPEED_VERY_SLOW:
			this.supressFrames = 2;
			break;
		case SPEED_SLOW:
			this.supressFrames = 1;
			break;
		case SPEED_MEDIUM:
			this.supressFrames = 0;
			break;
		default:
			this.supressFrames = 0;
			break;
		}
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
	
	private boolean frameIsSupressed(){
		
		if(this.supressedFrames < this.supressFrames){
			this.supressedFrames +=1;
			return true;
		}else{
			this.supressedFrames = 0;
		}
		
		
		return false;
	}
	
	public void update(){
		
		if(this.frameIsSupressed()){
			return;
		}
		
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
				this.gameObject.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha() + this.getSpeed()));
			}else{
				this.finishAnimation();
			}
		}else{
			if(this.value[0] < this.gameObject.getColor().getAlpha()){
				Color color = this.gameObject.getColor();
				this.gameObject.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha() - this.getSpeed()));
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
		switch(this.speed){
		case SPEED_VERY_SLOW:
			return 1;
		case SPEED_SLOW:
			return 1;
		case SPEED_MEDIUM:
			return 1;
		case SPEED_FAST:
			return 5;
		case SPEED_VERY_FAST:
			return 10;
		}
		
		return 1;
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
