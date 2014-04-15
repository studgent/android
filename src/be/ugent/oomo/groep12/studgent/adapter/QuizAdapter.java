package be.ugent.oomo.groep12.studgent.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.QuizQuestion;

public class QuizAdapter extends ArrayAdapter<QuizQuestion> {
    static class QuizAdapterItemHolder
    {
    	
        TextView distance;
        TextView points;
        TextView question;
        RelativeLayout item;
        
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
	

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        QuizAdapterItemHolder holder = null;
        
        if (row == null) {
        	//System.out.println("ruw==null");
        	LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new QuizAdapterItemHolder();
            holder.points = (TextView)row.findViewById(R.id.quiz_question_points); 
            holder.question = (TextView)row.findViewById(R.id.quiz_question_question);
            holder.distance = (TextView)row.findViewById(R.id.quiz_question_distance);
            holder.item = (RelativeLayout)row.findViewById(R.id.quiz_question_form);
            row.setTag(holder);
        } else {
            holder = (QuizAdapterItemHolder)row.getTag();
        }
        
        QuizQuestion quizQuestion = data.get(position);
        
        holder.distance.setText(quizQuestion.getDistance() + " KM");
        holder.points.setText(quizQuestion.getPoints() + "");
        holder.question.setText(quizQuestion.getQuestion() + "");
    
        if (quizQuestion.isSolved() ){
              holder.item.setBackgroundColor(Color.GRAY);
        } else if (!quizQuestion.maySolve() ){
            holder.item.setBackgroundColor(Color.RED);
        } else {
        	holder.item.setBackgroundColor(Color.TRANSPARENT);

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
