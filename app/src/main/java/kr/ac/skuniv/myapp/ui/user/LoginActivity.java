package kr.ac.skuniv.myapp.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import kr.ac.skuniv.myapp.R;
import kr.ac.skuniv.myapp.core.domain.User;
import kr.ac.skuniv.myapp.core.exception.JSONResultException;
import kr.ac.skuniv.myapp.core.network.SafeAsyncTask;
import kr.ac.skuniv.myapp.core.provider.UserProvider;
import kr.ac.skuniv.myapp.ui.MainActivity;


public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private String email;
    private String password;
    

    private SafeAsyncTask<User> authenticationTask;

	private Button buttonLogin;
	private View layoutProgress;
    EditText editTextEmail;
	EditText editTextPassword;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_login );

		// switch login form UI
        buttonLogin = ( Button )findViewById( R.id.buttonLogin );
        layoutProgress = findViewById( R.id.layout_progress );
        editTextEmail = ( EditText )findViewById( R.id.editTextEmail );
		editTextPassword = ( EditText )findViewById( R.id.editTextPassword );
        TextView signupText = ( TextView )findViewById( R.id.tv_signup);
        
        buttonLogin.setOnClickListener( this );
        signupText.setMovementMethod( LinkMovementMethod.getInstance() );
        signupText.setText( Html.fromHtml( "<u>가입하기</u>" ) );

        uiDefault();
	}

	@Override
	public void onClick( View v ) {
		int viewId = v.getId();
		if( viewId == R.id.buttonLogin ) {
			handleLogin();	
		}
	}

	private void uiDefault() {
		layoutProgress.setVisibility( View.GONE );
		buttonLogin.setVisibility( View.VISIBLE );
	}
	
	private void uiProgress() {
		buttonLogin.setVisibility( View.GONE );
		layoutProgress.setVisibility( View.VISIBLE );
		showSoftInput( layoutProgress, false );
	}
	
	private void showSoftInput( final View view, final boolean show ) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager inputMethodManager = ( InputMethodManager )getSystemService( Context.INPUT_METHOD_SERVICE );
				if( show ) {
					inputMethodManager.showSoftInput( view, 0 );
				} else {
					inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(), 0 );
				}
			}
		}, 100);				
	}

	public final void handleLogin() {

        email = editTextEmail.getText().toString();
		password = editTextPassword.getText().toString();

		if( "".equals( email ) || "".equals( password ) ) {
			return;
		}

		uiProgress();

		new Handler().postDelayed( new Runnable() {

			@Override
			public void run() {

				authenticationTask = new SafeAsyncTask<User>() {
					
					@Override
					public User call() throws Exception {
						User user = new UserProvider( LoginActivity.this ).login( email, password );
						return user;
					}

		            @Override
		            protected void onException( Exception exception ) {
		            	if( exception instanceof JSONResultException) {
                            Toast.makeText( LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT ).show();
                            uiDefault();
		            	} else {
		            	}
		            }
		            
		            @Override
		            public void onSuccess( User user ) {
						System.out.println( "--------------->" + user );
						//계정정보 저장
                        SharedPreferences sharedPreference = getSharedPreferences( "myapp-pref", MODE_PRIVATE );
                        SharedPreferences.Editor sharedPreferenceEditor = sharedPreference.edit();
                        sharedPreferenceEditor.putLong( "userNo", user.getNo() );
                        sharedPreferenceEditor.putString( "email", email );
                        sharedPreferenceEditor.putString( "password", password );
                        sharedPreferenceEditor.commit();

                        Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                        startActivity( intent );
                        finish();
		            }

		            @Override
		            protected void onFinally() throws RuntimeException {
		                authenticationTask = null;
		            }            
				};
				
				authenticationTask.execute();
			}
		}, 1000 );

	}
}
