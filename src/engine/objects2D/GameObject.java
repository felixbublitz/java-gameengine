package engine.objects2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import engine.core.Game;

public class GameObject {
	
	protected Point postition;
	protected Dimension size;
	protected int rotation;
	
	protected Point originalPostition;
	protected Dimension originalSize;
	
	protected Color color = Color.BLACK;
	private AffineTransform af;
	protected Game game;
	protected Point origin;
	private int id;
	
	public final static int ORIGIN_LEFT = -1;
	public final static int ORIGIN_CENTER = 0;
	public final static int ORIGIN_RIGHT = 1;
	public final static int ORIGIN_TOP = -1;
	public final static int ORIGIN_BOTTOM = 1;
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return this.id;
	}
	
	public GameObject(Point position, Dimension size, int rotation, Game game){
		this.originalPostition = position;
		this.originalSize = size;
		this.rotation = rotation;
		this.game = game;
		this.origin = new Point(-1,-1);
		this.applyResFactor();
	}
	
	public void setOrigin(Point origin){
		this.origin = origin;
	}
	
	
	protected Point getAligntPosition(Graphics2D g){
		Point aligntPosition = new Point();
		
		int originX = this.origin.x;
		int originY = this.origin.y;
		
		switch(originX){
		case ORIGIN_LEFT:
			aligntPosition.x = this.postition.x;
			break;
		case ORIGIN_RIGHT:
			aligntPosition.x = this.postition.x - this.size.width;
			break;
		case ORIGIN_CENTER:
			aligntPosition.x = this.postition.x - this.size.width/2;
			break;
		}

		switch(originY){
		case ORIGIN_TOP:
			aligntPosition.y = this.postition.y;
			break;
			
		case ORIGIN_CENTER:
			aligntPosition.y = this.postition.y - this.size.height / 2;
			break;
			
		case ORIGIN_BOTTOM:
			aligntPosition.y = this.postition.y - this.size.height;
			break;
			
		}
		
		return aligntPosition;
	}
	
	public void setSize(Dimension size){
		this.originalSize = size;
		this.applyResFactor();
	}
	
	protected void applyResFactor(){
		float resFactor = this.game.getResFactor();
		this.postition = new Point(Math.round(this.originalPostition.x * resFactor),(Math.round(this.originalPostition.y * resFactor)));
		this.size = new Dimension(Math.round(this.originalSize.width * resFactor),(Math.round(this.originalSize.height * resFactor)));
	}
	
	public Point getPosition(){
		return this.originalPostition;
	}
	
	public void setPosition(Point position){
		this.originalPostition = position;
		this.applyResFactor();
	}
	
	public Dimension getSize(){
		return this.originalSize;
	}
	
	public void setRotation(int rotation){
		this.rotation = rotation;
	}
	
	public int getRotation(){
		return this.rotation;
	}
	
	
	
	public void draw(Graphics2D g){
		
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	protected void startDrawing(Graphics2D g){
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		af = new AffineTransform();
		af.rotate(Math.toRadians(this.rotation), this.postition.x+ this.size.width/2, this.postition.y+this.size.height/2); 
		g.setTransform(af); 
		g.setColor(color);
	}
	
	protected void stopDrawing(Graphics2D g){
		g.rotate(-Math.PI/2);
		af.rotate(-Math.toRadians(this.rotation), this.postition.x + this.size.width/2, this.postition.y + this.size.height/2); 
		g.setTransform(af);
	}
	
}
