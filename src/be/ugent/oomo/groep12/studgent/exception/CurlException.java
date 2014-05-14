package be.ugent.oomo.groep12.studgent.exception;

public class CurlException extends StudGentException {
	
	private static final long serialVersionUID = 1566770561220872299L;

	public CurlException(String e){
		super(e);
	}

	public CurlException(Exception e){
		super(e);
	}
}
