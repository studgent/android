package be.ugent.oomo.groep12.studgent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.utilities.LayoutUtil;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.data.CheckinsDataSource;



public class UserProfileActivity extends Activity {

	public static final String PREFS_NAME = "preferences";

	protected Button button_trofies;
	protected Button button_checkin;
	protected Friend user;
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

		user = (Friend) getIntent().getParcelableExtra("user");
		
		TextView txtName = (TextView)findViewById(R.id.user_profile_name); 
		TextView txtEmail = (TextView)findViewById(R.id.user_profile_email); 
		TextView txtPhone = (TextView)findViewById(R.id.user_profile_phone); 
		TextView txtscore = (TextView)findViewById(R.id.user_profile_score); 
		
		//hier nog de text van btntrophie veranderen (zodat het aantal trofies er ook in staat)
		btnCheckinText = String.valueOf(button_checkin.getText());
		button_checkin.setText(btnCheckinText+"  ("+CheckinsDataSource.getCheckinNumber(user.getId())+")");
		
		txtName.setText(Html.fromHtml(user.getName()));
		txtEmail.setText(Html.fromHtml("<a href=\"mailto:"+ user.getEmail() +"\" >"+user.getEmail()+"</a>"));
		txtEmail.setMovementMethod(LinkMovementMethod.getInstance());
		txtPhone.setText(Html.fromHtml(user.getPhone() + ""));
		txtscore.setText(Html.fromHtml(user.getScore() +" punten"));
	}
	@Override
	protected void onResume(){
		super.onResume();
		btnCheckinText = String.valueOf(btnCheckinText+" ("+CheckinsDataSource.getCheckinNumber(user.getId())+")");
		
	}
	
	
	public void openTrofieListActivity(View view){
		Intent intent = new Intent(this, TrophiesListActivity.class);
		intent.putExtra("userID", user.getId());
		startActivity(intent);
	}
	
	public void openCheckinListActivity(View view) {
		Intent intent = new Intent(this, CheckinListActivity.class);
		intent.putExtra("checkinOfUser", true);
		intent.putExtra("sourceID", user.getId());
		startActivity(intent);
	}
}
