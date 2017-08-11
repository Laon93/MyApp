package com.example.myapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.core.domain.User;
import com.example.myapp.core.exeption.JSONResultException;
import com.example.myapp.core.provider.UserProvider;
import com.example.myapp.network.SafeAsyncTask;
import com.example.myapp.ui.user.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed( new Runnable(){
            @Override
            public void run() {

                SafeAsyncTask<User> authenticationTask = new SafeAsyncTask<User>() {

                    @Override
                    public User call() throws Exception {
                        SharedPreferences sharedPreference = getSharedPreferences( "myapp-preferences", Context.MODE_PRIVATE );
                        String email = sharedPreference.getString( "email", null );
                        String password = sharedPreference.getString( "password", null );
                        User user = new UserProvider( SplashActivity.this ).auth( email, password );
                        return user;
                    }

                    @Override
                    protected void onException( Exception exception ) {
                        if( exception instanceof JSONResultException) {
                            Toast.makeText( SplashActivity.this, exception.getMessage(), Toast.LENGTH_SHORT ).show();
                        } else {
                        }
                    }

                    @Override
                    public void onSuccess( User user ) {
                        System.out.println( user );
                        Intent intent = new Intent( SplashActivity.this, MainActivity.class );
                        startActivity( intent );
                        finish();
                    }
                };

                authenticationTask.execute();
            }
        }, 1000 );
    }
}
