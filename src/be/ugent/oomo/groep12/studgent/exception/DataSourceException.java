package be.ugent.oomo.groep12.studgent.exception;

public class DataSourceException extends Exception {

	private static final long serialVersionUID = -1001989150444220274L;

	public DataSourceException(String e){
		super(e);
	}
	
	public DataSourceException(Exception e){
		super(e);
	}
}