package engine.core;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import engine.datatypes.Ressource;
import engine.interfaces.GraphicsInterface;

public class Graphics {

	private GraphicsInterface graphicsInterface;

	private BufferStrategy buffer;
	private BufferedImage bi;
	private Color background = Color.WHITE;
	private Image backgroundImage;
	private Ressource backgroundRessource;

	private Dimension defaultDimension;
	private float resFactor;
	private Dimension resultDimension;
	private JFrame jframe;
	private Canvas canvas;
	private float[] resSteps = new float[]{0, 1, 1.42292f, 2};
	private int currentResStep;

	public void increaseResolution(){
		if(this.currentResStep < resSteps.length - 1){
			this.setResolution(++currentResStep);
		}
	}

	public void decreaseResolution(){
		if(this.currentResStep > 0){
			this.setResolution(--currentResStep);
		}
	}

	public float getResFactor() {
		return this.resFactor;
	}

	public Dimension getResolution(){
		return this.resultDimension;
	}

	public Graphics(GraphicsInterface graphicsInterface) {
		this.graphicsInterface = graphicsInterface;
		this.defaultDimension = new Dimension(960, 540);
		this.resSteps[0] = this.getOptimalResFactor();
		this.setResolution(0);
		this.createGraphicObjects();
	}

	private Dimension getResolutionByStep(int step){
		return  new Dimension(((int) (this.defaultDimension.width * this.resSteps[step])),
				((int) (this.defaultDimension.height * this.resSteps[step])));
	}

	private Dimension getOptimalDimension(){
		return this.getResolutionByStep(0);
	}

	private void setResolution(int resStep){
		this.currentResStep = resStep;
		this.resFactor = this.resSteps[resStep];
		this.resultDimension = new Dimension(((int) (this.defaultDimension.width * this.resFactor)),
				((int) (this.defaultDimension.height * this.resFactor)));
		if(this.canvas != null){
		this.canvas.setSize(this.resultDimension);
		if(this.getOptimalDimension().width >= this.resultDimension.width){
			this.canvas.setLocation(this.getOptimalDimension().width / 2 - this.canvas.getSize().width/2, this.getOptimalDimension().height / 2 - this.canvas.getSize().height/2);
		}
		this.jframe.setBackground(Color.BLACK);

		}

	}

	private float getOptimalResFactor(){
		Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		float resFactor = (float) (screenDimension.width) / ((float) this.defaultDimension.width);
		this.resultDimension = new Dimension(((int) (this.defaultDimension.width * this.resFactor)),
				((int) (this.defaultDimension.height * this.resFactor)));

		return resFactor;
	}

	public JFrame getFrame() {
		return jframe;
	}

	public Dimension getScreenSize() {
		return this.defaultDimension;
	}

	public void setBackground(Ressource ressoruce) {
		this.backgroundRessource = ressoruce;
	}

	public void setBackgroundColor(Color color) {
		this.background = color;
	}


	public void setFullscreen(boolean fullscreen){
		if(fullscreen){
		      com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this.getFrame(),true);
		      com.apple.eawt.Application.getApplication().requestToggleFullScreen(this.getFrame());
		      this.getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);


		    }else{
		      this.getFrame().setExtendedState(JFrame.NORMAL);
		    }
	}

	public Point getCenter(){
		Dimension windowSize = this.getScreenSize();
	    Point center = new Point(windowSize.width / 2, windowSize.height / 2);
	    return center;
	}

	private void createGraphicObjects() {
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		canvas.setSize(resultDimension);
		canvas.setBackground(Color.BLACK);

		jframe = new JFrame();
		// jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setIgnoreRepaint(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// jframe.setUndecorated(true);
		jframe.add(canvas);
		jframe.pack();
		jframe.setVisible(true);
		jframe.setFocusable(true);


		canvas.createBufferStrategy(1);
		this.buffer = canvas.getBufferStrategy();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		bi = gc.createCompatibleImage(resultDimension.width, resultDimension.height);

	}


	void requestUpdate(float interpolationFactor) {
		Graphics2D g = bi.createGraphics();
		java.awt.Graphics graphics = null;

		jframe.requestFocusInWindow();
		try {
			g.setColor(this.background);
			g.fillRect(0, 0, resultDimension.width, resultDimension.height);

			this.drawBackground(g);

			this.graphicsInterface.Draw(g, interpolationFactor);

			graphics = buffer.getDrawGraphics();

			graphics.drawImage(bi, 0, 0, null);

			if (!buffer.contentsLost()) {
				buffer.show();
			}
			Thread.yield();
		} finally {
			if (graphics != null)
				graphics.dispose();
		}
	}

	private void drawBackground(Graphics2D g) {
		if (backgroundRessource != null) {
			backgroundImage = backgroundRessource.getImage();
			backgroundRessource = null;
		}

		if (this.backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, resultDimension.width, resultDimension.height, null);
		}
	}

}
