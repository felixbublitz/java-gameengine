package engine.objects2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import engine.core.Game;

public class ShapeObject extends GameObject {
	
	

	public ShapeObject(Point position, Dimension size, int rotation, Game game) {
		super(position, size, rotation, game);
		this.color = Color.white;
	}

	@Override
	public void draw(Graphics2D g) {
		this.startDrawing(g);
		Point shapePosition = this.getAligntPosition(g);
		g.fillRect(shapePosition.x, shapePosition.y, this.size.width, this.size.height);
		this.stopDrawing(g);
	}

}
