package be.ugent.oomo.groep12.studgent.exception;

import java.io.IOException;

public class CurlException extends StudGentException {
	public CurlException(String e){
		super(e);
	}

	public CurlException(Exception e){
		super(e);
	}
}
