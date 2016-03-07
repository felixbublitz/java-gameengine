package engine.datatypes;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.scene.media.Media;

public class Ressource {
	private String path;
	public final static int TYPE_FILESYSTEM = 1;
	public final static int TYPE_INTERN = 2;
	public final static int TYPE_WEB = 3;
	private int type;

	public Ressource(String path, int type) {
		this.path = path;
		this.type = type;
	}

	public String getPath() {
		return this.path;
	}

	public Image getImage() {
		switch (type) {
		case TYPE_FILESYSTEM:
			return getImageFromFileSystem();
		case TYPE_INTERN:
			return getImageFromIntern();
		case TYPE_WEB:
			return getImageFromWeb();
		}
		return null;
	}

	public Media getMedia() {
		switch (type) {
		case TYPE_FILESYSTEM:
			return getMediaFromFileSystem();
		case TYPE_INTERN:
			return getMediaFromIntern();
		case TYPE_WEB:
			return getMediaFromWeb();
		}
		return null;
	}

	public Media getMediaFromWeb() {
		String escapedPath = path.replace("https:", "http:");
		return new Media(escapedPath);
	}

	public Media getMediaFromFileSystem() {
		File file = new File(new File(path).getAbsolutePath());
		return new Media(file.toURI().toString());
	}

	public Media getMediaFromIntern() {
		try {
			return new Media(this.getClass().getResource("/" + path).toURI().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;

	}

	public Image getImageFromFileSystem() {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {

		}
		return null;
	}

	public Image getImageFromWeb() {
		try {
			return ImageIO.read(new URL(path));
		} catch (IOException e) {

		}
		return null;
	}

	public Image getImageFromIntern() {

		try {

			return ImageIO.read(this.getClass().getResourceAsStream("/" + path));
		} catch (IOException e) {

		}
		return null;
	}

}
