package be.ugent.oomo.groep12.studgent.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;


public class LoginActivity extends Activity {


	protected Button button_login;
	protected Button button_logout;
	protected TextView login_info;
	protected EditText email_box;
	protected EditText password_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		login_info = (TextView) findViewById(R.id.login_info);
		email_box = (EditText) findViewById(R.id.login_email);
		password_box = (EditText) findViewById(R.id.login_password);
		button_logout = (Button) findViewById(R.id.logout);
		button_login = (Button) findViewById(R.id.loginbutton);
		
		if ( LoginUtility.isLoggedIn() ) {
			setLoggedIn();
		} else {
			setLoggedOut();
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {

		return MenuUtil.PrepareMenu(this, menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    return MenuUtil.OptionsItemSelected(this, item);
	}
	
	
	public void setLoggedIn() {
		login_info.setText("Ingelogd als :\n" + LoginUtility.getEmail() );
		email_box.setVisibility(android.view.View.GONE);
		password_box.setVisibility(android.view.View.GONE);
		button_login.setVisibility(android.view.View.GONE);
		button_logout.setVisibility(android.view.View.VISIBLE);
	}
	
	public void setLoggedOut() {
		login_info.setText("Niet ingelogd" );
		email_box.setVisibility(android.view.View.VISIBLE);
		password_box.setVisibility(android.view.View.VISIBLE);
		button_login.setVisibility(android.view.View.VISIBLE);
		button_logout.setVisibility(android.view.View.GONE);
	}
	
	public void login(View view){
		String[] credentials = new String[2];
		credentials[0] = email_box.getEditableText().toString();
		credentials[1] = password_box.getEditableText().toString();
		new AsyncLoginLoader().execute(credentials);
	}
	
	public void logout(View view) {
		LoginUtility.LogOut();
		setLoggedOut();
	}
	

	private class AsyncLoginLoader extends AsyncTask<String, Void, String> {
	    private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

	    @Override
	    protected void onPostExecute(String result) {            
	        super.onPostExecute(result);
	        dialog.dismiss();
        	Toast.makeText(LoginActivity.this, LoginUtility.getMessage(), Toast.LENGTH_LONG).show();

	        if (LoginUtility.isLoggedIn()){
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
	        	LoginUtility.LogIn(email, password);
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
