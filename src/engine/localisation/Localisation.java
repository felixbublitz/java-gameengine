package engine.localisation;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Localisation {
	private static ResourceBundle ressourceBundle;

	public Localisation() {

	}

	public void setLocalisationBundle(String bundle){
		ressourceBundle = ResourceBundle.getBundle(bundle);
	}

	public String getString(String key) {
		try {
			return ressourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
