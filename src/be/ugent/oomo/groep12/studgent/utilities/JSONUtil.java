package be.ugent.oomo.groep12.studgent.utilities;

import org.json.JSONObject;

public class JSONUtil {

	public static String optString(final JSONObject json, final String key) {
	    return json.isNull(key) ? null : json.optString(key);
	}

}
