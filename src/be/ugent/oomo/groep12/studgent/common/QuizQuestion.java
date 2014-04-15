package be.ugent.oomo.groep12.studgent.common;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import be.ugent.oomo.groep12.studgent.utilities.LocationUtil;

import android.R.bool;
import android.location.Location;

public class QuizQuestion {
	protected int id;
	protected int points;
	protected boolean solved;
	protected List<String> possibleAnswers;
	protected String answer;
	protected Calendar lastTry;
	protected Location location;
	
	
	public QuizQuestion(int id, int points, Boolean solved,
			List<String> possibleAnswers, String answer, Calendar lastTry,
			Location location) {
		super();
		this.id = id;
		this.points = points;
		this.solved = solved;
		this.possibleAnswers = possibleAnswers;
		this.answer = answer;
		this.lastTry = lastTry;
		this.location = location;
	}
	
	double getDistance(){
		if (location==null){
			return 0;
		}else{
			//DUMMY, must be replaced if GPS of the device is known!
			return (new Random()).nextDouble();
		}
	}
	
	boolean checkAnswer(String answer){
		lastTry = Calendar.getInstance();
		answer = answer.toLowerCase().trim().replace(" ","");
		if (answer.equalsIgnoreCase(answer)){
			solved=true;
			return true;
		}else{
			return false;
		}
	}
	
	boolean maySolve(){
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

	public Location getLocation() {
		return location;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPoints(int points) {
		this.points = points;
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

	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	
	
}
