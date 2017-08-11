package kr.ac.skuniv.myapp.core.exception;

/**
 * Created by cs618 on 2017-07-19.
 */

public class HttpResponseException extends Exception {

    public HttpResponseException() {
        super( "Http Response Exception" );
    }

    public HttpResponseException( String message ) {
        super( message );
    }
}
