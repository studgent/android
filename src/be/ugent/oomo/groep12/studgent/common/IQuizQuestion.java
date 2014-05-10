package be.ugent.oomo.groep12.studgent.common;

import java.util.Calendar;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public interface IQuizQuestion {

	public abstract double getDistance();

	public abstract boolean maySolve();

	public abstract int getId();

	public abstract int getPoints();

	public abstract String getQuestion();

	public abstract boolean isSolved();

	public abstract List<String> getPossibleAnswers();

	public abstract String getAnswer();

	public abstract Calendar getLastTry();

	public abstract LatLng getLocation();

	public abstract void setId(int id);

	public abstract void setPoints(int points);

	public abstract void setQuestion(String question);

	public abstract void setSolved(boolean solved);

	public abstract void setPossibleAnswers(List<String> possibleAnswers);

	public abstract void setAnswer(String answer);

	public abstract void setLastTry(Calendar lastTry);

	public abstract void setLocation(LatLng location);

	public abstract int describeContents();

	public abstract String getName();

	public abstract void setName(String name);

}