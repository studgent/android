package be.ugent.oomo.groep12.studgent.utilities;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.activity.AboutActivity;
import be.ugent.oomo.groep12.studgent.activity.LoginActivity;

public class MenuUtil {

	public MenuUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean PrepareMenu(Context context, Menu menu){
		// change login text
		MenuItem login_item = menu.findItem(R.id.login);
		if ( LoginUtility.isLoggedIn() ) {
			login_item.setTitle( context.getString(R.string.login) + " (" + LoginUtility.getEmail() + ")");
		} else {
			login_item.setTitle( context.getString(R.string.login) );
		}
		return true;
	}
	

	public static boolean OptionsItemSelected(Context context, MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.login:
		    	openLoginActivity(context);
		        return true;
		    case R.id.info:
		    	openAboutActivity(context);
		        return true;
	    }
	    return true;
	}

	public static void openLoginActivity(Context context){
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}
	public static void openAboutActivity(Context context){
		Intent intent = new Intent(context, AboutActivity.class);
		context.startActivity(intent);
	}

}