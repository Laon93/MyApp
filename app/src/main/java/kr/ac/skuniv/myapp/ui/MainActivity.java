package kr.ac.skuniv.myapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import kr.ac.skuniv.myapp.R;
import kr.ac.skuniv.myapp.ui.user.LoginActivity;
import kr.ac.skuniv.myapp.ui.widget.StatusBarLinearLayout;

public class MainActivity extends AppCompatActivity {
    private MainTabsAdapter mainTabsAdapter;
    private NetworkConnectionReceiver networkConnectionReceiver;
    private StatusBarLinearLayout statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 네트워크 상태 리시버 등록
        if( networkConnectionReceiver == null ) {
            networkConnectionReceiver = new NetworkConnectionReceiver();
            registerReceiver(
                    networkConnectionReceiver,
                    new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION ) );
        }

        //계정정보 가져오기( preferences, 나중에는 Accout Manager에게 요청)
        SharedPreferences preferences = getSharedPreferences( "myapp-pref", Context.MODE_PRIVATE );
        String email = preferences.getString( "email", null );
        String password = preferences.getString( "password", null );

        if( email == null || password == null ) {
            //login activity
            Intent intent = new Intent( this, LoginActivity.class );
            startActivity( intent );
            finish();
        }

        String session = preferences.getString( "session", null );
        if( session == null ) {
            //splash activity
            Intent intent = new Intent( this, SplashActivity.class );
            startActivity( intent );
            finish();
        }

        setContentView( R.layout.activity_main );

        mainTabsAdapter = new MainTabsAdapter(
                this,
                (TabHost)findViewById( android.R.id.tabhost ),
                (ViewPager)findViewById( R.id.pager ) );

        mainTabsAdapter.selectTab( MainTabsConfig.TABINDEX.FIRST );

        statusBar = (StatusBarLinearLayout)findViewById( R.id.layout_global_status );
    }

    @Override
    protected void onStop() {
        SharedPreferences preferences = getSharedPreferences( "myapp-pref", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove( "session" );
        editor.commit();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 네트워크 상태 리시버 등록 해제
        if( networkConnectionReceiver != null ) {
            unregisterReceiver( networkConnectionReceiver );
            networkConnectionReceiver = null;
        }
        super.onDestroy();
    }

    private class NetworkConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if( action.equals( ConnectivityManager.CONNECTIVITY_ACTION ) ){
                ConnectivityManager connectivityManager =
                        ( ConnectivityManager )context.getSystemService( Context.CONNECTIVITY_SERVICE );
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                boolean isWifiConnected =
 ( networkInfo != null &&
   networkInfo.getType() == ConnectivityManager.TYPE_WIFI );

                boolean isMobileConnected =
 ( networkInfo != null &&
   networkInfo.getType() == ConnectivityManager.TYPE_MOBILE );

                if( isWifiConnected == true ) {

                    System.out.println( "--------->인터넷 연결(WIFI)" );
                    //Toast.
                    //makeText( MainActivity.this, "인터넷 연결(WIFI)", Toast.LENGTH_LONG ).
                    //show();
                    statusBar.makeText(
                    StatusBarLinearLayout.STATUS.INFORMATION,
                    "인터넷 연결(WIFI)").
                    show( false );
                } else if( isMobileConnected == true ) {
                    System.out.println( "--------->인터넷 연결(Mobile)" );
                    //Toast.
                    //makeText( MainActivity.this, "인터넷 연결(Mobile)", Toast.LENGTH_LONG ).
                    //show();
                    statusBar.makeText(
                    StatusBarLinearLayout.STATUS.INFORMATION,
                    "인터넷 연결(Mobile)").
                    show( false );
                } else {
                    System.out.println( "--------->인터넷 연결 끊어짐" );

                    //makeText( MainActivity.this, "인터넷 연결 끊어짐", Toast.LENGTH_LONG ).
                    //show();
                    statusBar.makeText(
                    StatusBarLinearLayout.STATUS.INFORMATION,
                    "인터넷 연결 끊어짐").
                    show( true );
                }
            }
        }
    }
}
