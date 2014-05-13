package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;


import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.QuizAdapter;
import be.ugent.oomo.groep12.studgent.common.IQuizQuestion;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;
import be.ugent.oomo.groep12.studgent.data.QuizQuestionsDataSource;
import be.ugent.oomo.groep12.studgent.exception.CurlException;
import be.ugent.oomo.groep12.studgent.exception.DataSourceException;
import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;
import be.ugent.oomo.groep12.studgent.utilities.iDistanceUpdatedListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;



public class QuizActivity extends Activity implements 
AdapterView.OnItemClickListener, OnClickListener, OnEditorActionListener, iDistanceUpdatedListener

{
	
	ListView quiz_list;
	QuizAdapter adapter;
	IQuizQuestion currentQuestion;
	String currentAddress;
	

	
	
	public void navigateTo(View view) {
		String uri = "geo:" + currentQuestion.getLocation().latitude + ","
				+ currentQuestion.getLocation().longitude;
		if (currentAddress!=null){
			uri += "?q=" + currentAddress.replace(" ", "+");
		}
		try {
			startActivity(new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(uri)));
		} catch (ActivityNotFoundException e) {
			String message = "Navigatie kan niet geopend worden.";
			Toast.makeText(this, message, Toast.LENGTH_LONG)
					.show();
		}
	}

	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_quiz);
		
		quiz_list = (ListView) findViewById(R.id.quiz_list );

		// hide keyboard on start activity
	    this.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		adapter = new QuizAdapter(this, R.layout.quiz_question_item, new ArrayList<QuizQuestion>());
		quiz_list.setAdapter(adapter); 
		quiz_list.setOnItemClickListener(this);
		
		adapter.clear();
		
		if (LoginUtility.getInstance().isLoggedIn()==false){
			Toast.makeText(this, "Log in om de quiz te kunnen spelen!", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}else{
			new AsyncQuizQuestionListViewLoader().execute(adapter);
		}
		
		//start GPS
		LocationUtil.getInstance(this).registerDistanceUpdatedListener(this);
				
		
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
	

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		currentQuestion = adapter.getItem(position);
		currentAddress="";
		new AsyncGEOLoader().execute((QuizQuestion)(currentQuestion));
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
					
			Button btnNavigateto = (Button) findViewById(R.id.navigateToQuestion);
			if (currentQuestion.getLocation() == null){
				btnNavigateto.setVisibility(view.GONE);	
			}else{
				btnNavigateto.setVisibility(view.VISIBLE);
			}
			
			Button submitButton = (Button)findViewById(R.id.quiz_submitText);
			if (currentQuestion.getPossibleAnswers()==null || currentQuestion.getPossibleAnswers().size()==0){
				//no multiple answer question, show inputbox
				oneAnswerPanel.setVisibility(view.VISIBLE);
				multipleAnswerPanel.setVisibility(View.GONE);
				EditText answerInputBox = (EditText) findViewById(R.id.QuizAnswerInputBox);
				answerInputBox.setOnEditorActionListener(this);
				submitButton.setVisibility(View.VISIBLE);
			}else{
				//Multiple answers
				submitButton.setVisibility(View.GONE);
				
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
	
	public void submitButton(View view){
		EditText v = (EditText) findViewById(R.id.QuizAnswerInputBox);
		sendAnswer(v.getText().toString());
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
		//quiz_list.setFocusable(true);
		//quiz_list.requestFocus();
		
		Boolean correct;
		
		new AsyncQuizQuestionCheck().execute(answer);
		
	}


	private class AsyncQuizQuestionCheck extends AsyncTask<String, Void, Boolean> {
	    private final ProgressDialog dialog = new ProgressDialog(QuizActivity.this);

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_checkAnswer));
	        dialog.show();            
	    }
	    
		@Override
		protected Boolean doInBackground(String... answer) {
			// TODO Auto-generated method stub
			try {
				return QuizQuestionsDataSource.getInstance().checkAnswer((QuizQuestion)(currentQuestion), answer[0]);
			} catch (DataSourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
	    @Override
	    protected void onPostExecute(Boolean correct) {            
	        super.onPostExecute(correct);
	            dialog.dismiss();
	            if (correct){
	            	 Toast.makeText(getApplicationContext(), "Volledig correct!", Toast.LENGTH_SHORT).show();
	            	 renewListGui();
	            }else{
	    			Toast.makeText(getApplicationContext(), "Woeps, dat is mis. Probeer nog eens binnen 24uur.", Toast.LENGTH_SHORT).show();
	    		}
	    		renewListGui();
	    }
	    
	    
	}

	
	
	private class AsyncQuizQuestionListViewLoader extends AsyncTask<QuizAdapter, Void, Collection<QuizQuestion>> {
	    private final ProgressDialog dialog = new ProgressDialog(QuizActivity.this);

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_POIlist));
	        dialog.show();            
	    }

		@Override
		protected Collection<QuizQuestion> doInBackground(QuizAdapter... params) {
			//adp = params[0];
	        try {
	        	return QuizQuestionsDataSource.getInstance().getLastItems().values();
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        return null;
		}
		
	    @Override
	    protected void onPostExecute(Collection<QuizQuestion> result) {            
	        super.onPostExecute(result);
	            dialog.dismiss();
		        //adapter.setItemList(result);
		        adapter.clear();
		        for(QuizQuestion question: result){
		        	
		        	adapter.add(question);
		        }
		        renewListGui();
	    }
	}

	
	private class AsyncGEOLoader extends
	AsyncTask<QuizQuestion, Void, String> {
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		
			if (result != null) { // if we found coordinates, update the PoI
				currentAddress = result;
			}
			if (currentQuestion.getLocation().latitude == 0.0
					|| currentQuestion.getLocation().longitude == 0.0) {
				String message = "Kon geen coordinaten vinden.";
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(QuizQuestion... params) {
			QuizQuestion quiz = params[0];
			String location = null;
			try {
				location = LocationUtil.getAddressFromLatLng(currentQuestion
						.getLocation());
				Log.i("poi street:", location + "");
				if (location != null) {
					return location;
				}
			} catch (CurlException e) {
				Log.e("Reverse geocoder exception","");
				e.printStackTrace();
			}
			return null;
		}
	}


	//-----GPS
	@Override
	public void distanceIsUpdated() {
		// TODO Auto-generated method stub
		renewListGui();
	}
	@Override
	public void onPause(){
		LocationUtil.getInstance(this).onPause();
		super.onPause();
	}
	
	@Override
	public void onResume(){
		LocationUtil.getInstance(this).onResume();
		super.onResume();
	}
	


	


}




