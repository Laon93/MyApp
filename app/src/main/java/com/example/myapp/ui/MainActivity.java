package com.example.myapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.ui.widget.StatusBarLinearLayout;

public class MainActivity extends AppCompatActivity {

    private MainTabsAdapter mainTabsAdapter;
    private NetworkConnectionReceiver networkConnectionReceiver = null;
    private StatusBarLinearLayout statusBar;
    private int indexDefaultTab;

    @Override
    public void onStop() {
        //  세션키 삭제
        SharedPreferences sharedPreference = getSharedPreferences( "myapp-preferences", Context.MODE_PRIVATE );
        SharedPreferences.Editor sharedPreferenceEditor = sharedPreference.edit();
        sharedPreferenceEditor.remove( "session" );
        sharedPreferenceEditor.commit();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 네트워크 상태 리시버 등록
        if( networkConnectionReceiver == null ) {
            networkConnectionReceiver = new NetworkConnectionReceiver();
            registerReceiver( networkConnectionReceiver, new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION ) );
        }

        setContentView( R.layout.activity_main );

        mainTabsAdapter = new MainTabsAdapter( MainActivity.this, (TabHost)findViewById( android.R.id.tabhost ), (ViewPager)findViewById( R.id.pager ) );

        if( indexDefaultTab != MainTabsConfig.TABINDEX.FIRST ) {
            mainTabsAdapter.selectTab( indexDefaultTab );
        }

        statusBar = ( StatusBarLinearLayout )findViewById( R.id.layout_global_status );
    }

    @Override
    protected void onDestroy() {
        // 네트워크 상태 리시버 등록해제
        if( networkConnectionReceiver != null ) {
            unregisterReceiver( networkConnectionReceiver );
            networkConnectionReceiver = null;
        }

        super.onDestroy();
    }

    private class NetworkConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive( Context context, Intent intent ) {

            String action = intent.getAction();

            if ( action.equals( ConnectivityManager.CONNECTIVITY_ACTION ) ) {

                ConnectivityManager connManager = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
                NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

                boolean isWifiConnected = ( networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI );
                boolean isMobileConnected = ( networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE );

                if( isWifiConnected ) {
                    //statusBar.makeText( StatusBarLinearLayout.STATUS.INFORMATION, "인터넷 연결됨" ).show( false );
                    //Toast.makeText( MainActivity.this, "인터넷 연결됨", Toast.LENGTH_SHORT ).show();
                    statusBar.makeText( StatusBarLinearLayout.STATUS.INFORMATION, "인터넷 연결됨" ).show( false );

                } else if( isMobileConnected ) {
                    //statusBar.makeText( StatusBarLinearLayout.STATUS.INFORMATION, "인터넷 연결됨" ).show( false );
                    //Toast.makeText( MainActivity.this, "인터넷 연결됨", Toast.LENGTH_SHORT ).show();
                    statusBar.makeText( StatusBarLinearLayout.STATUS.INFORMATION, "인터넷 연결됨" ).show( false );
                } else {
                    //statusBar.makeText( StatusBarLinearLayout.STATUS.ERROR, "인터넷 연결 끊김" ).show( true );
                    //Toast.makeText( MainActivity.this, "인터넷 연결 끊김", Toast.LENGTH_SHORT ).show();
                    statusBar.makeText( StatusBarLinearLayout.STATUS.ERROR, "인터넷 연결 끊김" ).show( true );

                }
            }
        }
    }
}
