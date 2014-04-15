package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.FriendAdapter;
import be.ugent.oomo.groep12.studgent.adapter.QuizAdapter;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;
import be.ugent.oomo.groep12.studgent.data.QuizQuestionsDataSource;
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
		
		
		QuizAdapter adapter = new QuizAdapter(this, R.layout.quiz_question_item, new ArrayList<QuizQuestion>());
		quiz_list.setAdapter(adapter); 
		
		adapter.clear();
		List<QuizQuestion> data = new ArrayList<QuizQuestion>(QuizQuestionsDataSource.getInstance().getLastItems().values());
        for (QuizQuestion object : data ) {
        	adapter.add(object);
        }
        adapter.notifyDataSetChanged();        
	}	
}


