package be.ugent.oomo.groep12.studgent.common;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.google.android.gms.maps.model.LatLng;

import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;

import android.R.bool;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class QuizQuestion implements IData {
	protected int id;
	protected int points;
	protected String question;
	protected boolean solved;
	protected List<String> possibleAnswers;
	protected String answer;
	protected Calendar lastTry;
	protected LatLng location;
	
	
	public QuizQuestion(int id, int points, String question, Boolean solved,
			List<String> possibleAnswers, String answer, Calendar lastTry,
			LatLng location) {
		super();
		this.id = id;
		this.points = points;
		this.question = question;
		this.solved = solved;
		this.possibleAnswers = possibleAnswers;
		this.answer = answer;
		this.lastTry = lastTry;
		this.location = location;
	}
	
	public QuizQuestion(Parcel in) {
	    this.id = in.readInt();
	    this.points = in.readInt();
	    this.question=in.readString();
	    int tmp = in.readInt();
	    if (tmp == 1)
	    	this.solved = true;
	    else
	    	this.solved = false;
	    
	    this.possibleAnswers = new ArrayList<String>();
	    in.readList(possibleAnswers, null);
	    
	    this.answer = in.readString();
	    this.lastTry = Calendar.getInstance();
	    this.lastTry.setTimeInMillis(in.readLong());
	    this.location = new LatLng(in.readDouble(),in.readDouble());
	    
	}

	public double getDistance(){
		if (location==null){
			return 0;
		}else{
			//DUMMY, must be replaced if GPS of the device is known!
			return (new Random()).nextInt(20);
		}
	}
	
	public boolean checkAnswer(String givenanswer){
		lastTry = Calendar.getInstance();
		givenanswer = givenanswer.toLowerCase().trim().replace(" ","");
		if (answer.equalsIgnoreCase(givenanswer)){
			solved=true;
			return true;
		}else{
			return false;
		}
	}
	
	public boolean maySolve(){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, -24);
		if (lastTry == null || lastTry.compareTo(now) == -1){
			return true;
		}else{
			return false;
		}		
	}

	public int getId() {
		return id;
	}

	public int getPoints() {
		return points;
	}
	
	public String getQuestion(){
		return question;
	}

	public boolean isSolved() {
		return solved;
	}

	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}

	public String getAnswer() {
		return answer;
	}

	public Calendar getLastTry() {
		return lastTry;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public void setQuestion(String question){
		this.question=question;
		 
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	public void setPossibleAnswers(List<String> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public void setLastTry(Calendar lastTry) {
		this.lastTry = lastTry;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}
	
	
	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.points);
		dest.writeString(this.question);
		dest.writeInt(this.solved ? 1 : 0 ); //not boolean!
		dest.writeList(this.possibleAnswers);
		dest.writeString(this.answer);
		dest.writeLong(this.lastTry.getTimeInMillis());
		dest.writeDouble(this.location.latitude);
		dest.writeDouble(this.location.longitude);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	    public QuizQuestion createFromParcel(Parcel in) {
	        return new QuizQuestion(in);
	    }
	
	    public QuizQuestion[] newArray(int size) {
	        return new QuizQuestion[size];
	    }
	};


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return question;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		question = name;
	}
	
	
	
	
}
