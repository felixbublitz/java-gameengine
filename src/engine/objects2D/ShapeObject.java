package engine.objects2D;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import engine.core.Game;

public class ShapeObject extends GameObject {

	private int roundSize = 0;
	private Color borderColor;
	private boolean dashedStroke;
	final float dash1[] = {10.0f};
	final BasicStroke dashed =
		        new BasicStroke(1.0f,
		                        BasicStroke.CAP_BUTT,
		                        BasicStroke.JOIN_MITER,
		                        10.0f, dash1, 0.0f);

	public ShapeObject(Point position, Dimension size, int rotation, Game game) {
		super(position, size, rotation, game);
		this.color = Color.white;
	}

	public void setBorder(Color color, boolean dashed){
		this.borderColor = color;
		this.dashedStroke = dashed;
	}

	public void setRoundEdges(int size){
		this.roundSize = size;
	}

	private void updateAlpha(Graphics2D g){
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()));
	}

	private void clearAlpha(Graphics2D g){
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	@Override
	public void draw(Graphics2D g) {
		this.updateAlpha(g);
		this.startDrawing(g);
		Point shapePosition = this.getAligntPosition(g);

		RenderingHints qualityHints = new RenderingHints(
				  RenderingHints.KEY_ANTIALIASING,
				  RenderingHints.VALUE_ANTIALIAS_ON );
				qualityHints.put(
				  RenderingHints.KEY_RENDERING,
				  RenderingHints.VALUE_RENDER_QUALITY );
				g.setRenderingHints( qualityHints );

		if (this.interpolation != null && this.interpolation.getSize() != null) {
			if(this.borderColor != null){
				g.setColor(this.borderColor);
				if(dashedStroke){
					g.setStroke(dashed);
				}
				g.drawRoundRect(shapePosition.x - 1, shapePosition.y - 1, interpolation.getSize().width + 2, interpolation.getSize().height + 2, this.roundSize, this.roundSize);
			}
			g.setColor(this.color);
			g.fillRoundRect(shapePosition.x, shapePosition.y, interpolation.getSize().width, interpolation.getSize().height, this.roundSize, this.roundSize);
		} else {
			if(this.borderColor != null){
				g.setColor(this.borderColor);
				if(dashedStroke){
					g.setStroke(dashed);
				}
				g.drawRoundRect(shapePosition.x - 1, shapePosition.y - 1, this.size.width + 2, this.size.height + 2, this.roundSize, this.roundSize);
			}
			g.setColor(this.color);
			g.fillRoundRect(shapePosition.x, shapePosition.y, this.size.width, this.size.height, this.roundSize, this.roundSize);
		}

		this.stopDrawing(g);
		this.clearAlpha(g);
	}

}
