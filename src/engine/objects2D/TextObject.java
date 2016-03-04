package engine.objects2D;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

import engine.core.Game;

public class TextObject extends GameObject {


	private Font font;
	private String text;
	Font tempFont;


	public TextObject(Point position, int size, int rotation, Game game) {
		super(position, new Dimension(size,size), rotation, game);

		try {

			this.font = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getResourceAsStream("/res/font/DroidSans.ttf") );
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return this.text;
	}

	public Dimension getObjectSize(Graphics2D g){
		return getOriginalSize(this.getTextDimensions(g));
	}

	@Override
	protected Point getAligntPosition(Graphics2D g){
		Dimension textDimensions = this.getTextDimensions(g);
		Point aligntPosition = new Point();

		int originX = this.origin.x;
		int originY = this.origin.y;

		int posX = this.postition.x;
		int posY = this.postition.y;


		if(this.interpolation != null){
			Point pos = this.interpolation.getPosition();
			if(pos != null){
				posX = getResultPosition(this.interpolation.getPosition()).x;
				posY = getResultPosition(this.interpolation.getPosition()).y;
			}
		}
		int descentHeight = this.getDescentHeight(g);
		//Switch from BaseLine to DescendLine: http://docs.oracle.com/javase/7/docs/api/java/awt/FontMetrics.html

		switch(originX){
		case ORIGIN_LEFT:
			aligntPosition.x = posX;
			break;
		case ORIGIN_RIGHT:
			aligntPosition.x = posX - textDimensions.width;
			break;
		case ORIGIN_CENTER:
			aligntPosition.x = posX - textDimensions.width/2;
			break;
		}

		switch(originY){
		case ORIGIN_TOP:
			aligntPosition.y = posY - descentHeight + textDimensions.height;
			break;

		case ORIGIN_CENTER:
			aligntPosition.y = posY - descentHeight + textDimensions.height / 2;
			break;

		case ORIGIN_BOTTOM:
			aligntPosition.y = posY - descentHeight;
			break;

		}

		return aligntPosition;
	}

	private Dimension getTextDimensions(Graphics2D g){

		FontMetrics metrics = g.getFontMetrics(tempFont);
		int hgt = metrics.getHeight();
		int adv = metrics.stringWidth(text);

	//	System.out.println(adv);

		return new Dimension(adv, hgt);
	}


	private int getDescentHeight(Graphics2D g){

		FontMetrics metrics = g.getFontMetrics(tempFont);

		return metrics.getDescent();
	}

	@Override
	public void draw(Graphics2D g) {
		super.startDrawing(g);

		if(this.text == null){
			return;
		}

		int textSize = this.size.height;

		if(this.interpolation != null){
			Dimension size = this.interpolation.getSize();
			if(size != null){
				textSize = getResultSize(size).height;
			}
		}

		tempFont = new Font(this.font.getFontName(), Font.PLAIN, textSize);
		Point textPosition = this.getAligntPosition(g);

		g.setFont(tempFont);
		g.drawString(this.text, textPosition.x , textPosition.y);

		super.stopDrawing(g);
	}


}
