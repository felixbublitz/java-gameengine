package engine.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
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

	private Dimension normalDimension;
	private Dimension originalDimension;
	private float resFactor;
	private Dimension resultDimension;
	private JFrame jframe;


	public float getResFactor() {
		return this.resFactor;
	}



	public Graphics(GraphicsInterface graphicsInterface, int resFactor) {
		this.graphicsInterface = graphicsInterface;

		this.normalDimension = new Dimension(960, 540);
		this.originalDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.resFactor = (float) (originalDimension.width) / ((float) this.normalDimension.width);
		this.resFactor = resFactor;
		this.resultDimension = new Dimension(((int) (this.normalDimension.width * this.resFactor)),
				((int) (this.normalDimension.height * this.resFactor)));

		this.createGraphicObjects();
	}

	public Graphics(GraphicsInterface graphicsInterface) {
		this.graphicsInterface = graphicsInterface;

		this.normalDimension = new Dimension(960, 540);
		this.originalDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.resFactor = (float) (originalDimension.width) / ((float) this.normalDimension.width);
		this.resultDimension = new Dimension(((int) (this.normalDimension.width * this.resFactor)),
				((int) (this.normalDimension.height * this.resFactor)));

		this.createGraphicObjects();

	}

	public JFrame getFrame() {
		return jframe;
	}

	public Dimension getDimensions() {

		return this.normalDimension;
	}

	public void setBackground(Ressource ressoruce) {
		this.backgroundRessource = ressoruce;
	}

	private void createGraphicObjects() {
		Canvas canvas = new Canvas();
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

		canvas.createBufferStrategy(1);
		this.buffer = canvas.getBufferStrategy();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		bi = gc.createCompatibleImage(resultDimension.width, resultDimension.height);

	}

	public void requestUpdate(float interpolationFactor) {
		Graphics2D g = bi.createGraphics();
		java.awt.Graphics graphics = null;

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
