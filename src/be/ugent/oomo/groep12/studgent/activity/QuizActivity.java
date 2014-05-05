package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.drive.internal.OnContentsResponse;

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
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;



public class QuizActivity extends Activity implements AdapterView.OnItemClickListener, OnClickListener, OnEditorActionListener  {
	
	ListView quiz_list;
	QuizAdapter adapter;
	QuizQuestion currentQuestion;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_quiz);
		
		quiz_list = (ListView) findViewById(R.id.quiz_list );
		
		
		adapter = new QuizAdapter(this, R.layout.quiz_question_item, new ArrayList<QuizQuestion>());
		quiz_list.setAdapter(adapter); 
		quiz_list.setOnItemClickListener(this);
		
		adapter.clear();
		Collection<QuizQuestion> col = QuizQuestionsDataSource.getInstance().getLastItems().values();
		ArrayList<QuizQuestion> data = new ArrayList<QuizQuestion>(col);
		//Collections.sort(data, new QuizQuestionComparator());
		for (QuizQuestion object : data ) {
        	adapter.add(object);
        }
		renewListGui();  
		

	}
	
	private void renewListGui(){
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		currentQuestion = adapter.getItem(position);
		
		if (currentQuestion.maySolve() && currentQuestion.isSolved()==false){
			//detailview visible
			LinearLayout detailview = (LinearLayout) findViewById(R.id.detailViewQuestion);
			detailview.setVisibility(0);
			
			//set question
			TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);
			txtQuestion.setText(currentQuestion.getQuestion());
			
			//get panels
			RelativeLayout oneAnswerPanel = (RelativeLayout) findViewById(R.id.layoutAnswer);
			GridLayout multipleAnswerPanel = (GridLayout) findViewById(R.id.layoutAnswers);
					
			if (currentQuestion.getPossibleAnswers()==null || currentQuestion.getPossibleAnswers().size()==0){
				//no multiple answer question, show inputbox
				oneAnswerPanel.setVisibility(view.VISIBLE);
				multipleAnswerPanel.setVisibility(View.GONE);
				EditText answerInputBox = (EditText) findViewById(R.id.QuizAnswerInputBox);
				answerInputBox.setOnEditorActionListener(this);
			}else{
				//Multiple answers
				oneAnswerPanel.setVisibility(View.GONE);
				multipleAnswerPanel.setVisibility(view.VISIBLE);
				multipleAnswerPanel.removeAllViews();
				
				//LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				int idButton =0;
				for (String possibleAnswer : currentQuestion.getPossibleAnswers() ){
					Button btn = new Button(this);
					btn.setText(possibleAnswer);
					btn.setOnClickListener(this);
				
					int columnIndex = 1;
					int rowIndex=idButton;
							
					GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(GridLayout.spec(rowIndex, 1) , GridLayout.spec(columnIndex, 1));
				   
				    gridLayoutParam.setMargins(20,10,20,10);
				    multipleAnswerPanel.addView(btn, gridLayoutParam);
		            
		            idButton++;
				}
	
			}	
		}
	}



	//handle button click when multiple choice
	@Override
	public void onClick(View v) {
		//MultipleChoise, there is clicked on a button!
		Button target = (Button) v;
		String answer = target.getText().toString();
		sendAnswer(answer);
	}	
	
	//handle enter submit when no multiple choice
	@Override
	public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
		sendAnswer(((EditText) v).getText().toString());		
		return true;
	}
	
	
	public void sendAnswer(String answer){
		//hide answer panel
		LinearLayout detailview = (LinearLayout) findViewById(R.id.detailViewQuestion);
		detailview.setVisibility(View.GONE);
		Boolean correct = currentQuestion.checkAnswer(answer);
		if (correct){
			Toast.makeText(this, "Volledig correct!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "Woeps, dat is mis. Probeer nog eens binnen 24uur.", Toast.LENGTH_SHORT).show();
		}
		renewListGui();
	}





}




