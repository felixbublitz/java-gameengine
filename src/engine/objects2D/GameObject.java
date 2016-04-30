package engine.objects2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import engine.core.Game;
import engine.datatypes.Interpolation;

public class GameObject {

	protected Point postition;
	protected Dimension size;
	protected int rotation;
	protected Interpolation interpolation = null;

	protected Point originalPostition;
	protected Dimension originalSize;

	protected Color color = Color.BLACK;
	private AffineTransform af;
	protected Game game;
	protected Point origin;
	private int id;
	private float alpha = 1;

	public final static int ORIGIN_LEFT = -1;
	public final static int ORIGIN_CENTER = 0;
	public final static int ORIGIN_RIGHT = 1;
	public final static int ORIGIN_TOP = -1;
	public final static int ORIGIN_BOTTOM = 1;

	public void setInterpolationValue(Interpolation interpolation) {
		this.interpolation = interpolation;

	}

	public void setAlpha(float alpha){
		if(alpha > 0 || alpha < 1.0){
			this.alpha = alpha;
		}else{
			System.err.println("GameObject: Alpha must be between 0 and 1");
		}
	}

	public float getAlpha(){
		return this.alpha;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public GameObject(Point position, Dimension size, int rotation, Game game) {
		this.originalPostition = position;
		this.originalSize = size;
		this.rotation = rotation;
		this.game = game;
		this.origin = new Point(-1, -1);
		this.applyResFactor();
	}

	public GameObject() {

	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	protected Point getAligntPosition(Graphics2D g) {
		Point aligntPosition = new Point();

		int originX = this.origin.x;
		int originY = this.origin.y;

		int posX = this.postition.x;
		int posY = this.postition.y;

		if (this.interpolation != null) {
			Point pos = this.interpolation.getPosition();
			if (pos != null) {
				posX = getResultPosition(pos).x;
				posY = getResultPosition(pos).y;
			}
		}

		switch (originX) {
		case ORIGIN_LEFT:
			aligntPosition.x = posX;
			break;
		case ORIGIN_RIGHT:
			aligntPosition.x = posX - this.size.width;
			break;
		case ORIGIN_CENTER:
			aligntPosition.x = posX - this.size.width / 2;
			break;
		}

		switch (originY) {
		case ORIGIN_TOP:
			aligntPosition.y = posY;
			break;

		case ORIGIN_CENTER:
			aligntPosition.y = posY - this.size.height / 2;
			break;

		case ORIGIN_BOTTOM:
			aligntPosition.y = posY - this.size.height;
			break;

		}

		return aligntPosition;
	}

	public void setSize(Dimension size) {
		this.originalSize = size;
		this.applyResFactor();
	}

	protected void applyResFactor() {
		float resFactor = this.game.getGraphics().getResFactor();
		this.postition = new Point(Math.round(this.originalPostition.x * resFactor),
				(Math.round(this.originalPostition.y * resFactor)));
		this.size = new Dimension(Math.round(this.originalSize.width * resFactor),
				(Math.round(this.originalSize.height * resFactor)));
	}

	public Point getPosition() {
		return this.originalPostition;
	}

	public Point getResultPosition(Point originalPosition) {
		return new Point(Math.round(originalPosition.x * this.game.getGraphics().getResFactor()),
				(Math.round(originalPosition.y * this.game.getGraphics().getResFactor())));
	}

	public Dimension getResultSize(Dimension originalSize) {
		return new Dimension(Math.round(originalSize.width * this.game.getGraphics().getResFactor()),
				(Math.round(originalSize.height * this.game.getGraphics().getResFactor())));
	}

	public Point getOriginalPosition(Point resultPosition) {
		return new Point(Math.round(resultPosition.x / this.game.getGraphics().getResFactor()),
				(Math.round(resultPosition.y / this.game.getGraphics().getResFactor())));
	}

	public Dimension getOriginalSize(Dimension resultSize) {
		return new Dimension(Math.round(resultSize.width / this.game.getGraphics().getResFactor()),
				(Math.round(resultSize.height / this.game.getGraphics().getResFactor())));
	}

	public void setPosition(Point position) {
		this.originalPostition = position;
		this.applyResFactor();
	}

	public Dimension getSize() {
		return this.originalSize;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getRotation() {
		return this.rotation;
	}

	public void draw(Graphics2D g) {

	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	protected void startDrawing(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		af = new AffineTransform();

		if(this.postition != null && this.size != null){
		if (this.interpolation != null && this.interpolation.getRotation() != 0) {
			af.rotate(Math.toRadians(this.interpolation.getRotation()), this.postition.x + this.size.width / 2,
					this.postition.y + this.size.height / 2);
		} else {
			af.rotate(Math.toRadians(this.rotation), this.postition.x + this.size.width / 2,
					this.postition.y + this.size.height / 2);
		}

		g.setTransform(af);
		}

		//if (this.interpolation != null && this.interpolation.getAlpha() != 0) {
			/*Color interPolColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
					this.interpolation.getAlpha());
			g.setColor(COLOR);
			INTERPOLATION ALPHA FUNKTIONIERT NICHT
*/
		//} else {

			g.setColor(color);
		//}

	}

	protected void stopDrawing(Graphics2D g) {
		g.rotate(-Math.PI / 2);
		af.rotate(-Math.toRadians(this.rotation), this.postition.x + this.size.width / 2,
				this.postition.y + this.size.height / 2);
		g.setTransform(af);
	}

}
