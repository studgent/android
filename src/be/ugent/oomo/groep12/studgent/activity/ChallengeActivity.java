package be.ugent.oomo.groep12.studgent.activity;

import be.ugent.oomo.groep12.studgent.AmIDrunkActivity;
import be.ugent.oomo.groep12.studgent.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChallengeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_challenge);
	}

	public void openQuizActivity(View view){
		Intent intent = new Intent(this, QuizActivity.class);
		startActivity(intent);
	}
	
	public void openAmIDrunkActivity(View view){
		Intent intent = new Intent(this, AmIDrunkActivity.class);
		startActivity(intent);
	}
	
	public void openCheckInActivity(View view){
		Intent intent = new Intent(this, AmIDrunkActivity.class);
		startActivity(intent);
	}
	

}
