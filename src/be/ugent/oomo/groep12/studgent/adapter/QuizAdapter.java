package be.ugent.oomo.groep12.studgent.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.IQuizQuestion;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;

public class QuizAdapter extends ArrayAdapter<QuizQuestion> {
    static class QuizAdapterItemHolder
    {
    	
        TextView distance;
        TextView points;
        TextView question;
        TextView date;
        LinearLayout item;
        
    }
	
    Context context; 
    int layoutResourceId;    
    List<QuizQuestion> data = new ArrayList<QuizQuestion>();

	public QuizAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
	}
	public QuizAdapter(Context context, int layoutResourceId, List<QuizQuestion> objects) {
		super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = objects;
	}
	

    @SuppressLint("SimpleDateFormat")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        QuizAdapterItemHolder holder = null;
        
        if (row == null) {
        	//System.out.println("ruw==null");
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new QuizAdapterItemHolder();
            holder.points = (TextView)row.findViewById(R.id.quiz_question_point); 
            holder.question = (TextView)row.findViewById(R.id.quiz_question_question);
            holder.distance = (TextView)row.findViewById(R.id.quiz_question_distance);
            holder.item = (LinearLayout)row.findViewById(R.id.quiz_question_form);
            holder.date = (TextView)row.findViewById(R.id.quiz_question_date);
            row.setTag(holder);
        } else {
            holder = (QuizAdapterItemHolder)row.getTag();
        }
        
        IQuizQuestion quizQuestion = data.get(position);
        
        if (quizQuestion.getLocation() != null && quizQuestion.getDistance() != 0 && quizQuestion.getLocation().latitude != 0 && quizQuestion.getLocation().longitude != 0  ){
        	holder.distance.setText(Math.floor(quizQuestion.getDistance()*100)/100 + " KM");
        	holder.distance.setVisibility(View.VISIBLE);
        }else{
        	holder.distance.setVisibility(View.GONE);
        }
        holder.points.setText(quizQuestion.getPoints() + "");
        holder.question.setText(quizQuestion.getQuestion() + "");
        
        
        if (quizQuestion.isSolved() ){
              holder.item.setBackgroundColor(Color.rgb(180, 255, 180));
              SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
              holder.date.setText("Opgelost op " +  sdf.format( quizQuestion.getLastTry().getTime()));
        } else if (!quizQuestion.maySolve() ){
            holder.item.setBackgroundColor(Color.rgb(255, 180, 180));
            
            if (quizQuestion.getLastTry().get(Calendar.DAY_OF_MONTH) == (new GregorianCalendar()).get(Calendar.DAY_OF_MONTH)) {
            	//today filled in 
             	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                holder.date.setText("Wacht tot morgen (" +  sdf.format( quizQuestion.getLastTry().getTime()) + ")");
           
            }else{
            	//filled in yesterday
             	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                holder.date.setText("Wacht tot " +  sdf.format( quizQuestion.getLastTry().getTime()));
            }
          
        } else {
        	holder.item.setBackgroundColor(Color.TRANSPARENT);
        	holder.date.setText("");

        }
            
        
        return row;
    }

    public List<QuizQuestion> getItemList() {
        return data;
    }
 
    public void setItemList(List<QuizQuestion>  itemList) {
        this.data = itemList;
    }
	@Override
	public void clear() {
		super.clear();
	}

	
	
	


}
