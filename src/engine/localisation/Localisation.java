package engine.localisation;

import java.awt.Color;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Localisation {
	private static ResourceBundle ressourceBundle;

	public Localisation() {

	}

	public void setLocalisationBundle(String bundle){
		ressourceBundle = ResourceBundle.getBundle(bundle);
	}

	public Color getColor(String key) {
		try {
			String raw = ressourceBundle.getString(key);
			String[] color = raw.split(",");
			return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]), Integer.parseInt(color[3]));

		} catch (MissingResourceException e) {
			return null;
		}
	}


	public String getString(String key) {
		try {
			return ressourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
