package com.spun.pickit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.spun.pickit.database.handling.DatabaseHandler;


public class AppLoginActivity extends FragmentActivity {
    private static final String USERNAME_KEY = "usernameKey";
    private static final String PASSWORD_KEY = "passwordKey";
    private static final String TAG = "MainFragment";

    private MainFragment mainFragment;
    private EditText mUsernameRepresentation;
    private EditText mPasswordRepresentation;

    private String username;
    private String password;

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

        mUsernameRepresentation = (EditText)findViewById(R.id.usernameTextbox);
        mPasswordRepresentation = (EditText)findViewById(R.id.passwordTextbox);

        username = getPreferences(MODE_PRIVATE).getString(USERNAME_KEY, "");
        password = getPreferences(MODE_PRIVATE).getString(PASSWORD_KEY, "");

        setEventListeners();

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

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString(USERNAME_KEY, username);
        editor.putString(PASSWORD_KEY, password);

        editor.commit();

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

    public void onClickLogin(View v){
        DatabaseHandler data = new DatabaseHandler();
        boolean pass = data.validatePassword(this.username,this.password);
        if (pass){
            Intent intent = new Intent(this, AccountAdminActivity.class);
            startActivity(intent);
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Incorrect login!";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
        }
    }

    private void setEventListeners(){
        mUsernameRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    username = mUsernameRepresentation.getText().toString();
                }catch(Exception e){
                    username = "";
                }
            }
        });

        mPasswordRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    password= mPasswordRepresentation.getText().toString();
                }catch(Exception e){
                    password = "";
                }
            }
        });
    }
}
