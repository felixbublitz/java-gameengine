package engine.objects2D;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;

import engine.core.Game;

public class TextObject extends GameObject {



	private final static int DEFAULT_PADDING_LEFT = 10;

	private Font font;
	private String text;


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




	@Override
	protected Point getAligntPosition(Graphics2D g){
		Dimension textDimensions = this.getTextDimensions(g);
		Point aligntPosition = new Point();

		int originX = this.origin.x;
		int originY = this.origin.y;

		switch(originX){
		case ORIGIN_LEFT:
			aligntPosition.x = this.postition.x;
			break;
		case ORIGIN_RIGHT:
			aligntPosition.x = this.postition.x - textDimensions.width;
			break;
		case ORIGIN_CENTER:
			aligntPosition.x = this.postition.x - textDimensions.width/2;
			break;
		}

		switch(originY){
		case ORIGIN_TOP:
			aligntPosition.y = this.postition.y + textDimensions.height;
			break;

		case ORIGIN_CENTER:
			aligntPosition.y = this.postition.y + textDimensions.height / 2;
			break;

		case ORIGIN_BOTTOM:
			aligntPosition.y = this.postition.y;
			break;

		}

		return aligntPosition;
	}

	private Dimension getTextDimensions(Graphics2D g){


		FontMetrics metrics = g.getFontMetrics(new Font(this.font.getFontName(), Font.PLAIN, this.size.height));
		int hgt = metrics.getHeight();


		/*Workaround for width */
		int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
		int fontSize = (int)Math.round(this.size.height * screenRes / 72.0);
		Font font = new Font(this.font.getFontName(), Font.PLAIN, fontSize);
		metrics = g.getFontMetrics(font);
		int adv = metrics.stringWidth(this.text);


		return new Dimension(adv+2, hgt+2);
	}

	@Override
	public void draw(Graphics2D g) {
		super.startDrawing(g);

		if(this.text == null){
			return;
		}

		Point textPosition = this.getAligntPosition(g);

		/* Fix the FontSize because Java draws the String to small */
		int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
		int fontSize = (int)Math.round(this.size.height * screenRes / 72.0);
		Font font = new Font(this.font.getFontName(), Font.PLAIN, fontSize);


		g.setFont(font);
		g.drawString(this.text, textPosition.x , textPosition.y);

		super.stopDrawing(g);
	}


}
