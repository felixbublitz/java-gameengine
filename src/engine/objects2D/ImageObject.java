package engine.objects2D;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import engine.core.Game;
import engine.datatypes.Ressource;

public class ImageObject extends GameObject {
	public static enum DisplayMode{MODE_SCALE};

	private Image image;
	private DisplayMode displayMode;

	public ImageObject(Point position, Dimension size, int rotation, Game game) {
		super(position, size, rotation, game);
	}

	public void setImage(Ressource ressource) {
		this.image = ressource.getImage();
	}

	public void setDisplayMode(DisplayMode displayMode){
		this.displayMode = displayMode;
		this.displayModeChanged();
	}

	private void displayModeChanged(){
		switch(displayMode){
		case MODE_SCALE:
			if(image != null){
			float ratio = (float)this.image.getWidth(null) / (float)this.image.getHeight(null);
			this.setSize(new Dimension(Math.round(this.getSize().height * ratio), this.getSize().height));
			}
			break;
		}
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
		super.startDrawing(g);
		if (this.image != null) {
			Point imagePosition = this.getAligntPosition(g);
			if (this.interpolation != null && this.interpolation.getSize() != null) {
				if (image != null) {
					g.drawImage(image, imagePosition.x, imagePosition.y, this.interpolation.getSize().width,
							this.interpolation.getSize().height, null);
				}
			} else {
				if (image != null) {
					g.drawImage(image, imagePosition.x, imagePosition.y, this.size.width, this.size.height, null);
				}
			}
		}
		this.clearAlpha(g);
		super.stopDrawing(g);

	}

}
