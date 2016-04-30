package engine.localisation;

import java.awt.Color;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Localisation {
	private static ResourceBundle ressourceBundle;


	/**
	 * set the path to the localisation file.
	 * the file must be in a package in the res dir
	 * @param bundle the path to your localisation file
	 */
	public void setLocalisationBundle(String bundle){
		ressourceBundle = ResourceBundle.getBundle(bundle);
	}

	/**
	 * Get a color from the localization.
	 * Color must be in format: color=[red (0-255)],[green (0-255)],[blue (0-255)],[alpha (0-255)]
	 * @param key the color key
	 */
	public Color getColor(String key) {
		try {
			String raw = ressourceBundle.getString(key);
			String[] color = raw.split(",");
			return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]), Integer.parseInt(color[3]));

		} catch (MissingResourceException e) {
			return null;
		}
	}

	/**
	 * Get a string from the localization.
	 * @param key the string key
	 */
	public String getString(String key) {
		try {
			return ressourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
