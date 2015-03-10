package com.spun.pickit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.spun.pickit.database.handling.DatabaseAccess;
import com.spun.pickit.fileIO.*;

import java.util.ArrayList;


public class AppLoginActivity extends Activity {
    private static final String USERNAME_KEY = "usernameKey";
    private static final String PASSWORD_KEY = "passwordKey";

    FileManager fileManager;
    EditText mUsernameRepresentation;
    EditText mPasswordRepresentation;

    private static String username;
    private static String password;

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_login);

        fileManager = new FileManager(this);
        mUsernameRepresentation = (EditText)findViewById(R.id.usernameTextbox);
        mPasswordRepresentation = (EditText)findViewById(R.id.passwordTextbox);

        username = getPreferences(MODE_PRIVATE).getString(USERNAME_KEY, "");
        password = getPreferences(MODE_PRIVATE).getString(PASSWORD_KEY, "");

        ArrayList<String> credentials = fileManager.readSavedCredentials();
        if(credentials.size() == 2){
            username = credentials.get(0);
            password = credentials.get(1);
            CheckBox rememberMe = (CheckBox)findViewById(R.id.box_remember_me);
            rememberMe.setChecked(true);
        }

        updateScreenText();

        setEventListeners();

        //
        // Facebook related material
        //

//        if (savedInstanceState == null) {
//            // Add the fragment on initial activity setup
//            mainFragment = new MainFragment();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(android.R.id.content, mainFragment)
//                    .commit();
//        } else {
//            // Or set the fragment from restored state info
//            mainFragment = (MainFragment) getSupportFragmentManager()
//                    .findFragmentById(android.R.id.content);
//        }
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

    public void onClickLogin(View v) {
        if(!username.equals("") && !password.equals("")){
            //Create access object to validate username/password input
            DatabaseAccess access = new DatabaseAccess();
            boolean pass = access.validatePassword(username,password);

            if (pass){
                //If the password is valid and the checkbox is checked, save username/password locally
                CheckBox rememberMe = (CheckBox)findViewById(R.id.box_remember_me);
                if(rememberMe.isChecked()){
                    fileManager.saveCredentials(username, password);
                }else{
                    if(fileManager.credentialFileExists()){
                        fileManager.deleteCredentials();
                    }
                }

                //Take the user to the menu activity
                //TODO- change to appropriate activity once created
                Intent intent = new Intent(this, AccountAdminActivity.class);
                startActivity(intent);
                return;
            }
        }

        Context context = getApplicationContext();
        CharSequence text = "Invalid username or password\nPlease try again";
        int duration = Toast.LENGTH_LONG;

        Toast.makeText(context, text, duration).show();
    }

    private void setEventListeners(){
        mUsernameRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                username = mUsernameRepresentation.getText().toString();
            }
        });

        mPasswordRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                password = mPasswordRepresentation.getText().toString();
            }
        });
    }

    private void updateScreenText(){
        mUsernameRepresentation.setText(username);
        mPasswordRepresentation.setText(password);
    }
}