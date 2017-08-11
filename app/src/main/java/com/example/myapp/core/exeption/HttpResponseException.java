package com.example.myapp.core.exeption;

public class HttpResponseException extends Exception {
	private static final long serialVersionUID = -7157681894492625408L;
	
	private int responseCode;
	
	public HttpResponseException( int responseCode ){
		super( "Unexpected Response Code: " + responseCode );
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
