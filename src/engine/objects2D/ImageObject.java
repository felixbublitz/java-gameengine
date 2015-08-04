package engine.objects2D;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.core.Game;

public class ImageObject extends GameObject {

	private Image image;
	
	public ImageObject(Point position, Dimension size, int rotation, Game game) {
		super(position, size, rotation, game);
	}
	
	public void setImage(Image image){
		this.image = image;
	}
	
	public void setImage(String path){
		  try {
		      this.image = ImageIO.read(new File(path));
		  } catch (IOException e) {
		  }

		  
	}

	@Override
	public void draw(Graphics2D g) {
		super.startDrawing(g);
		if(this.image != null){
			Point imagePosition = this.getAligntPosition(g);
				g.drawImage(image, imagePosition.x, imagePosition.y, this.size.width, this.size.height, null);
		}
		super.stopDrawing(g);
		
	}

}
