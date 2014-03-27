package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.crashlytics.android.Crashlytics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.utilities.CurlUtil;
import be.ugent.oomo.groep12.studgent.utilities.LayoutUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;


public class LoginActivity extends Activity {
	

	protected Button button_login;
	protected EditText email_box;
	protected EditText password_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		email_box = (EditText) findViewById(R.id.login_email);
		password_box = (EditText) findViewById(R.id.login_password);
	}
	
	public void login(View view){
		String[] credentials = new String[2];
		credentials[0] = email_box.getEditableText().toString();
		credentials[1] = password_box.getEditableText().toString();
		new AsyncLoginLoader().execute(credentials);
	}
	

	private class AsyncLoginLoader extends AsyncTask<String, Void, String> {
	    private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

	    @Override
	    protected void onPostExecute(String result) {            
	        super.onPostExecute(result);
	        dialog.dismiss();
        	Toast.makeText(LoginActivity.this, LoginUtility.getInstance().getMessage(), Toast.LENGTH_LONG).show();

	        if (LoginUtility.getInstance().isLoggedIn()){
	        	LoginActivity.this.finish();
	        }
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage("Bezig met inloggen");
	        dialog.show();            
	    }
	    @Override
	    protected String doInBackground(String... credentials) {
	        try {
	        	String email = credentials[0];
	        	String password = credentials[1];
	        	boolean success = LoginUtility.getInstance().LogIn(email, password);
	        	String token = "";
		        return token;
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        return "error";
	    }
	}
}
