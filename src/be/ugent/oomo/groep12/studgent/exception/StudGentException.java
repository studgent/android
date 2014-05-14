package be.ugent.oomo.groep12.studgent.exception;

public class StudGentException extends Exception {

	private static final long serialVersionUID = 1445931245887532568L;

	public StudGentException(String e){
		super(e);
	}
	
	public StudGentException(Exception e){
		super(e);
	}
}
