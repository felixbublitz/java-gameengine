package engine.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import engine.interfaces.GraphicsInterface;

public class Graphics {

	private GraphicsInterface graphicsInterface;


	private BufferStrategy buffer;
	private BufferedImage bi;
	private Color background = Color.WHITE;

	private Dimension normalDimension;
	private Dimension originalDimension;
	private float resFactor;
	private Dimension resultDimension;

	public float getResFactor(){
		return this.resFactor;
	}

	public Graphics(GraphicsInterface graphicsInterface){
		this.graphicsInterface = graphicsInterface;

		this.normalDimension = new Dimension(960,510);
		this.originalDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.resFactor = (float)(originalDimension.width)/((float)this.normalDimension.width);
		//this.resFactor = 2;
		this.resultDimension = new Dimension(((int)(this.normalDimension.width * this.resFactor)),((int)(this.normalDimension.height * this.resFactor)));

		this.createGraphicObjects();
	}

	public Dimension getDimensions(){

		return this.normalDimension;
	}

	private void createGraphicObjects(){
		Canvas canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setSize(resultDimension);
		canvas.setBackground(Color.BLACK);



		JFrame jframe = new JFrame();
	//	jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setIgnoreRepaint(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	jframe.setUndecorated(true);
		jframe.add(canvas);
		jframe.pack();
		jframe.setVisible(true);

		canvas.createBufferStrategy(2);
		this.buffer = canvas.getBufferStrategy();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gd = ge.getDefaultScreenDevice();
	    GraphicsConfiguration gc = gd.getDefaultConfiguration();
	    bi = gc.createCompatibleImage(resultDimension.width,resultDimension.height);
	}

	public void requestUpdate(){
	     Graphics2D g = bi.createGraphics();
	     java.awt.Graphics graphics = null;

		  try {
			  g.setColor(this.background);
			  g.fillRect(0, 0, resultDimension.width, resultDimension.height);

			/*  BufferedImage img = null;
			  try {
			      img = ImageIO.read(new File("background.jpg"));
			      g.drawImage(img, 0, 0, resultDimension.width, img.getWidth()/resultDimension.width * img.getHeight(), null);
			  System.out.println(img.getWidth() / img.getHeight() * resultDimension.height );
			  } catch (IOException e) {
			  }

			  */

			  this.graphicsInterface.Draw(g);



		      graphics = buffer.getDrawGraphics();
		      //graphics.drawImage( bi, 0, (this.originalDimension.height-this.resultDimension.height)/2, null );
		      graphics.drawImage( bi, 0, 0, null );

			  if(!buffer.contentsLost()){
				  buffer.show();
			  }
			  Thread.yield();
			  }finally{
				  if(graphics != null)
					  graphics.dispose();
				  }
		  }

}
