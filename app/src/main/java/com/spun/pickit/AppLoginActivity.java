package com.spun.pickit;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AppEventsLogger;
import com.facebook.Session;


public class AppLoginActivity extends FragmentActivity {

    private MainFragment mainFragment;
    private static final String TAG = "MainFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
        setContentView(R.layout.activity_app_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Fb's Insights Dashboard - Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

        //Fb's Insights Dashboard - Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    public void onClickCreateAccount(View v) {
        Intent intent = new Intent(this, AccountAdminActivity.class);
        startActivity(intent);
    }

    public void onClickGuestLogin(View v) {
        Intent intent = new Intent(this, AccountAdminActivity.class);
        startActivity(intent);
    }
}
