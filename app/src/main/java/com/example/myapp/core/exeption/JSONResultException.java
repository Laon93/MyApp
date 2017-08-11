package com.example.myapp.core.exeption;

public class JSONResultException extends Exception {
	private static final long serialVersionUID = -205636236355859475L;
	
	public JSONResultException( String message ){
		super( message );
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
