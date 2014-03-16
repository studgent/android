package be.ugent.oomo.groep12.studgent.exception;

public class StudGentException extends Exception {
	public StudGentException(String e){
		super(e);
	}
	
	public StudGentException(Exception e){
		super(e);
	}
}
