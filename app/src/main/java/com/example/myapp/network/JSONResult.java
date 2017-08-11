package com.example.myapp.network;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

public class JSONResult<DataT> {

	private String result;
	private String message;
	private DataT data;

    public static <T> JSONResult<T> fromJSON(HttpRequest request, Type typeOfT ) throws IOException {

        Gson gson = new GsonBuilder().create();

        Reader reader = request.bufferedReader();
        JSONResult<T> v = gson.fromJson( reader, new TypeToken<JSONResult<T>>() {}.getType() );


        reader.close();

        return v;
    }

	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess(){
		return "success".equals( this.result ) ? true : false;
	}
	
	public DataT getData() {
		return data;
	}
	
	public void setData( DataT data ) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "JSONResult [result=" + result + ", message=" + message + ", data=" + data + "]";
	}	
}