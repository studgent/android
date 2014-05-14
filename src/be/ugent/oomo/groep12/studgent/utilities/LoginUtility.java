package be.ugent.oomo.groep12.studgent.utilities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import be.ugent.oomo.groep12.studgent.data.FriendListDataSource;
import be.ugent.oomo.groep12.studgent.data.QuizQuestionsDataSource;
import be.ugent.oomo.groep12.studgent.data.TrophieListDataSource;
import be.ugent.oomo.groep12.studgent.exception.CurlException;

public final class LoginUtility {
	public static final String PREFS_NAME = "preferences";
	private String token;
	private String email;
	private String message;
	private boolean logged_in;
	private int id;
	
    private static final LoginUtility INSTANCE = new LoginUtility();

    /**
     * A singleton class constructor
     */
    private LoginUtility() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    /*
     * returns the singleton instance
     */
    public static LoginUtility getInstance() {
        return INSTANCE;
    }

    public static String getMessage() {
    	return getInstance().message;
    }
    public static String getToken() {
    	return getInstance().token;
    }
    
    public static boolean isLoggedIn() {
    	return getInstance().logged_in;
    }
    
    public static int getId() {
    	return getInstance().id;
    }


	public static String getEmail() {
    	return getInstance().email;
	}
    
    public static void AutoLogin(String email, int id, String token) {
		getInstance().email = email;
		getInstance().id = id;
		getInstance().token = token;
		getInstance().logged_in = true;
    }
    
    /**
     * Log the current user out. 
     */
    public static void LogOut() {
    	// Set in  preferences
		SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove("email");
		editor.remove("userid");
		editor.remove("token");
		getInstance().email = null;
		getInstance().id = 0;
		getInstance().logged_in = false;
		// Commit the edits!
		editor.commit();
		QuizQuestionsDataSource.getInstance().delete();
		FriendListDataSource.getInstance().delete();
		TrophieListDataSource.getInstance().delete();
    }
    
    /**
     * Try to login with a email and password
     * @param email : the email address of the user
     * @param password: the corresponding password
     * @returns true if the authentication was succesfully, otherwise false will be returned
     */
    public static boolean LogIn(String email, String password) {

		try {
	    	Map<String, String> params = new HashMap<String, String>();
			params.put("email", email);
			params.put("password", password);
			String result = CurlUtil.post("user/login", params);
			
			// If response not OK (!= 200), then curlutil will throw exception
			// assume login successful
			getInstance().logged_in = true;
			getInstance().message = "Ingelogd!";
			
			JSONObject item = new JSONObject(result);
			getInstance().token = item.getString("token");
			getInstance().email = email;
			getInstance().id = item.getJSONObject("user").getInt("id");
			// Set in  preferences
			SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("email", getInstance().email);
			editor.putInt("userid", getInstance().id);
			editor.putString("token", getInstance().token);
			// Commit the edits!
			editor.commit();
	    	return getInstance().logged_in;
		} catch (CurlException e) {
			getInstance().message = "Kon niet inloggen";
			getInstance().logged_in = false;
		} catch (JSONException e) {
			getInstance().message = "Kon niet inloggen";
			getInstance().logged_in = false;
		}
		getInstance().message = "Kon niet inloggen";
		getInstance().logged_in = false;
    	return getInstance().logged_in;
    }
    
    
}