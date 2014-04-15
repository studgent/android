package be.ugent.oomo.groep12.studgent.activity;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;
import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QuizActivity extends Activity {
	
	ListView quiz_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_quiz);
		
		quiz_list = (ListView) findViewById(R.id.quiz_list );
		
	}	
}


