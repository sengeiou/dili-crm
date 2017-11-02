package com.dili.alm.exceptions;

public class ProjectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 481485125636785308L;

	public ProjectException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ProjectException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProjectException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ProjectException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Throwable fillInStackTrace() {
		// TODO Auto-generated method stub
		return super.fillInStackTrace();
	}

}
