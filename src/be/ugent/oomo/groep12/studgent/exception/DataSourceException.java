package be.ugent.oomo.groep12.studgent.exception;

public class DataSourceException extends Exception {
	public DataSourceException(String e){
		super(e);
	}
	
	public DataSourceException(Exception e){
		super(e);
	}
}