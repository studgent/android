package be.ugent.oomo.groep12.studgent.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.utilities.LayoutUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;
import be.ugent.oomo.groep12.studgent.activity.AboutActivity;
import be.ugent.oomo.groep12.studgent.data.CheckinsDataSource;
import be.ugent.oomo.groep12.studgent.data.TrophieListDataSource;

import com.crashlytics.android.Crashlytics;


public class UserProfileActivity extends Activity {

	public static final String PREFS_NAME = "preferences";

	protected Button button_trofies;
	protected Button button_checkin;
	private int userID;
	protected String btnCheckinText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Enable crash logs
		 */
		setContentView(R.layout.activity_user_profile);
		setButtons();
		
		
	}
	
	
	
	protected void setButtons(){
		// get the buttons and set them to protected members
		button_trofies = (Button) findViewById(R.id.button_trofie);
		button_checkin = (Button) findViewById(R.id.button_checkin);

		// enable on touch effect
		LayoutUtil.buttonEffect(button_trofies);
		LayoutUtil.buttonEffect(button_checkin);

		userID = getIntent().getExtras().getInt("userID");
		
		//set profile values
		String name = getIntent().getExtras().getString("name");
		String email = getIntent().getExtras().getString("email");
		String phone = getIntent().getExtras().getString("phone");
		String score = getIntent().getExtras().getString("score");
		
		TextView txtName = (TextView)findViewById(R.id.user_profile_name); 
		TextView txtEmail = (TextView)findViewById(R.id.user_profile_email); 
		TextView txtPhone = (TextView)findViewById(R.id.user_profile_phone); 
		TextView txtscore = (TextView)findViewById(R.id.user_profile_score); 
		Button btnTrofie = (Button)findViewById(R.id.button_trofie); 
		Button btnCheckin = (Button)findViewById(R.id.button_checkin); 
		
		//hier nog de text van btntrophie veranderen (zodat het aantal trofies er ook in staat)
		btnCheckinText = String.valueOf(btnCheckin.getText());
		btnCheckin.setText(btnCheckinText+"  ("+CheckinsDataSource.getCheckinNumber(userID)+")");
		
		txtName.setText(Html.fromHtml(name));
		txtEmail.setText(Html.fromHtml("<a href=\"mailto:"+email+"\" >"+email+"</a>"));
		txtEmail.setMovementMethod(LinkMovementMethod.getInstance());
		txtPhone.setText(Html.fromHtml(txtPhone.getText()+ ""));
		txtscore.setText(Html.fromHtml(txtscore.getText() +""));
	}
	@Override
	protected void onResume(){
		super.onResume();
		btnCheckinText = String.valueOf(btnCheckinText+"  ("+CheckinsDataSource.getCheckinNumber(userID)+")");
		
	}
	
	
	public void openTrofieListActivity(View view){
		Intent intent = new Intent(this, TrophiesListActivity.class);
		intent.putExtra("userID", userID);
		startActivity(intent);
	}
	
	public void openCheckinListActivity(View view) {
		Intent intent = new Intent(this, CheckinListActivity.class);
		intent.putExtra("checkinOfUser", true);
		intent.putExtra("sourceID", userID);
		startActivity(intent);
	}
}
