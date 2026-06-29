package com.estatehub.backend.utils;

public class AppBussinessException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AppBussinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppBussinessException(String message) {
		super(message);
		
	}

	public AppBussinessException(Throwable cause) {
		super(cause);
	}
	
	

}
