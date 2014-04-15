package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;

public class QuizActivity extends Activity implements AdapterView.OnItemClickListener  {
	
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
		quiz_list.setOnItemClickListener(this);
		
		adapter.clear();
		Collection<QuizQuestion> col = QuizQuestionsDataSource.getInstance().getLastItems().values();
		ArrayList<QuizQuestion> data = new ArrayList<QuizQuestion>(col);
		//Collections.sort(data, new QuizQuestionComparator());
		for (QuizQuestion object : data ) {
        	adapter.add(object);
        }
		adapter.sort(new Comparator<QuizQuestion>() {

			@Override
			public int compare(QuizQuestion lhs, QuizQuestion rhs) {
				
				if (lhs.isSolved() != rhs.isSolved()){
					if (rhs.isSolved()){
						return -1;
					}else{
						return 1;
					}
				}
				
				if (lhs.maySolve() != rhs.maySolve()){
					if (rhs.maySolve()){
						return 1;
					}else{
						return -1;
					}
				}
				
				if (lhs.getDistance() != rhs.getDistance()){
					if (rhs.getDistance() > lhs.getDistance()){
						return -1;
					}else{
						return 1;
					}
				}
				return 0;
			}
		   
		});
        adapter.notifyDataSetChanged();   
        
        
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		GridLayout detailview = (GridLayout) findViewById(R.id.detailViewQuestion);
		detailview.setVisibility(0);
	}	
}








