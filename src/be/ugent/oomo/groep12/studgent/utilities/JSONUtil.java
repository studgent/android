package be.ugent.oomo.groep12.studgent.utilities;

import org.json.JSONObject;

public class JSONUtil {

	/**
	 * A string represenation of a JSON value without the 'null' bug
	 * @param json : The current JSON value
	 * @param key	:The key value 
	 * @return : the corresponding value or null
	 */
	public static String optString(final JSONObject json, final String key) {
	    return json.isNull(key) ? null : json.optString(key);
	}

}
