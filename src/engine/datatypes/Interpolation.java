package engine.datatypes;

import java.awt.Dimension;
import java.awt.Point;

public class Interpolation {
	private Dimension size;
	private Point position;
	private int alpha = 0;
	private int rotation = 0;

	public Interpolation() {
		this.position = new Point(-1,-1);
		this.size = new Dimension(-1,-1);
	}

	public Dimension getSize(){
		if(this.size.height == -1){
			return null;
		}
		Dimension value = this.size;
		this.size =  new Dimension(-1,-1);
		return value;
	}

	public Point getPosition(){
		if(this.position.x == -1){
			return null;
		}
		Point value = this.position;
		this.position = new Point(-1,-1);
		return value;
	}

	public int getAlpha(){
		return this.alpha;
	}

	public int getRotation(){
		return this.rotation;
	}


	public void addRotateInterpolation(int value){
		this.rotation = value;
	}

	public void addPositionYInterpolation(int value){
		this.position.y = value;
	}

	public void addPositionXInterpolation(int value){
		this.position.x = value;
	}

	public void addSizeHeightInterpolation(int value){
		this.size.height = value;
	}

	public void addSizeWidthInterpolation(int value){
		this.size.width = value;
	}

	public void  addFadeInterpolation(int value){
		this.alpha = value;
	}

}
