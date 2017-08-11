package com.example.myapp.core.provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapp.core.domain.User;
import com.example.myapp.core.exeption.HttpResponseException;
import com.example.myapp.core.exeption.JSONResultException;
import com.example.myapp.network.JSONResult;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import static android.content.SharedPreferences.*;

/**
 * Created by kicks on 2017-07-18.
 */

public class UserProvider {

    private Context context;

    public UserProvider( Context context ) {
        this.context = context;
    }

    public User auth( String email, String password ) throws IOException, HttpResponseException, JSONResultException {

        String url = "http://192.168.0.12:8080/mysite/api/user?a=login";
        String query = String.format( "email=%s&password=%s", email, password );
        HttpRequest request =  HttpRequest.post( url );

        request.accept( HttpRequest.CONTENT_TYPE_JSON );
        request.connectTimeout( 1000 );
        request.readTimeout( 3000 );
        request.send( query );

        int responseCode = request.code();

        if ( responseCode != HttpURLConnection.HTTP_OK  ) {
            throw new HttpResponseException( request.code() );
        }

        Reader reader = request.bufferedReader();
        JSONResultUser  jsonResult =  new GsonBuilder().create().fromJson( reader, JSONResultUser.class );
        reader.close();

        if( "fail".equals( jsonResult.getResult() ) ) {
            throw new JSONResultException( jsonResult.getMessage() );
        }

        // save cookie
        Map<String, List<String>> responseHeaderMap = request.getConnection().getHeaderFields();
        String jsessionCookie = null;
        if( responseHeaderMap != null ) {
            List<String> cookies = responseHeaderMap.get("Set-Cookie") ;
            if( cookies != null ) {
                for( String cookieValue : cookies ) {
                    if( cookieValue.startsWith( "JSESSIONID=" ) ) {
                        jsessionCookie = cookieValue.substring(0, cookieValue.indexOf(";"));
                    }
                }
            }
        }

        if( jsessionCookie != null ) {
            SharedPreferences pref = context.getSharedPreferences( "myapp-preferences",  Context.MODE_PRIVATE );
            Editor editor = pref.edit();
            editor.remove( "session" );
            editor.putString( "session", jsessionCookie );
            editor.commit();
        }

        return jsonResult.getData();
    }

    public List<User> fetchUserList() throws IOException, HttpResponseException, JSONResultException {
        String url = "http://192.168.0.12:8080/mysite/api/user?a=list";
        HttpRequest request = HttpRequest.get( url );

        SharedPreferences pref = context.getSharedPreferences( "myapp-preferences", Context.MODE_PRIVATE );
        String sessionCookie = pref.getString( "session", null );
        System.out.println( "-------------->" + sessionCookie );
        String cookieString = "";
        if( sessionCookie != null ) {
            cookieString = cookieString + ";" + sessionCookie;
        }
        request.header( "Cookie", cookieString );

        request.contentType( HttpRequest.CONTENT_TYPE_JSON );
        request.accept( HttpRequest.CONTENT_TYPE_JSON );
        request.connectTimeout( 1000 );
        request.readTimeout( 3000 );

        int responseCode = request.code();
        if ( responseCode != HttpURLConnection.HTTP_OK  ) {
            throw new HttpResponseException( request.code() );
        }

        Reader reader = request.bufferedReader();
        JSONResultFetchUserList  jsonResult =  new GsonBuilder().create().fromJson( reader, JSONResultFetchUserList.class );
        reader.close();
        if( "fail".equals( jsonResult.getResult() ) ) {
            throw new JSONResultException( jsonResult.getMessage() );
        }

        return jsonResult.getData();
    }

    class JSONResultUser extends JSONResult<User> {}
    class JSONResultFetchUserList extends JSONResult<List<User>> {}
}
