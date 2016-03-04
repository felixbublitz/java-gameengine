package engine.objects2D;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import engine.core.Game;
import engine.datatypes.Ressource;

public class ImageObject extends GameObject {

	private Image image;

	public ImageObject(Point position, Dimension size, int rotation, Game game) {
		super(position, size, rotation, game);
	}

	public void setImage(Ressource ressource){
		this.image = ressource.getImage();
	}

	@Override
	public void draw(Graphics2D g) {
		super.startDrawing(g);
		if(this.image != null){
			Point imagePosition = this.getAligntPosition(g);
			if(this.interpolation != null && this.interpolation.getSize() != null){
				if(image != null){
					g.drawImage(image, imagePosition.x, imagePosition.y, this.interpolation.getSize().width, this.interpolation.getSize().height, null);
				}
				}else{
					if(image != null){
							g.drawImage(image, imagePosition.x, imagePosition.y, this.size.width, this.size.height, null);
					}
			}
		}
		super.stopDrawing(g);

	}

}
