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

public class QuizQuestion implements IData, IQuizQuestion {
	protected int id;
	protected int points;
	protected String question;
	protected boolean solved;
	protected List<String> possibleAnswers;
	protected String answer;
	protected Calendar lastTry;
	protected LatLng location;
	protected double distance;
	
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

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getDistance()
	 */
	@Override
	public double getDistance(){
		if (location==null){
			return 0;
		}else{
			//DUMMY, must be replaced if GPS of the device is known!
			return distance;
		}
	}
	
	public void setDistance(float distance2){
		this.distance=distance2;
	}
	
	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#maySolve()
	 */
	@Override
	public boolean maySolve(){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, -24);
		if (lastTry == null || lastTry.compareTo(now) == -1){
			return true;
		}else{
			return false;
		}		
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getPoints()
	 */
	@Override
	public int getPoints() {
		return points;
	}
	
	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getQuestion()
	 */
	@Override
	public String getQuestion(){
		return question;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#isSolved()
	 */
	@Override
	public boolean isSolved() {
		return solved;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getPossibleAnswers()
	 */
	@Override
	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getAnswer()
	 */
	@Override
	public String getAnswer() {
		return answer;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getLastTry()
	 */
	@Override
	public Calendar getLastTry() {
		return lastTry;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getLocation()
	 */
	@Override
	public LatLng getLocation() {
		return location;
	}

	public Location getLocationAsLocation(){
		Location loc = new Location("dummyprovider"); 
		loc.setLatitude(location.latitude);
		loc.setLongitude(location.longitude);
		return loc;
	}
	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setId(int)
	 */
	@Override
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setPoints(int)
	 */
	@Override
	public void setPoints(int points) {
		this.points = points;
	}
	
	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setQuestion(java.lang.String)
	 */
	@Override
	public void setQuestion(String question){
		this.question=question;
		 
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setSolved(boolean)
	 */
	@Override
	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setPossibleAnswers(java.util.List)
	 */
	@Override
	public void setPossibleAnswers(List<String> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setAnswer(java.lang.String)
	 */
	@Override
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setLastTry(java.util.Calendar)
	 */
	@Override
	public void setLastTry(Calendar lastTry) {
		this.lastTry = lastTry;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setLocation(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void setLocation(LatLng location) {
		this.location = location;
	}
	
	
	

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#describeContents()
	 */
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


	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return question;
	}

	/* (non-Javadoc)
	 * @see be.ugent.oomo.groep12.studgent.common.IQuizQuestion#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		question = name;
	}
	
}
