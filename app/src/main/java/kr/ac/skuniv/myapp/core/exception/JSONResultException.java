package kr.ac.skuniv.myapp.core.exception;

/**
 * Created by cs618 on 2017-07-19.
 */

public class JSONResultException extends Exception {

    public JSONResultException() {
        super( "JSONResult Exception Occurs" );
    }

    public JSONResultException( String message ) {
        super( message );
    }
}
