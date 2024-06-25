package com.app.prommonitor.exception;

public class BookNotFoundException extends Exception {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookNotFoundException(String errorMessage, Throwable err) {
       super(errorMessage, err);
	}

	public BookNotFoundException(String string) {
		 super(string);	
	}

}
