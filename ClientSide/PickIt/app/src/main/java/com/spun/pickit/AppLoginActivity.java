package com.spun.pickit;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.spun.pickit.database.handling.DatabaseAccess;
import com.spun.pickit.fileIO.*;

import java.util.ArrayList;


public class AppLoginActivity extends Activity {
    //region Activity Variables
    FileManager fileManager;
    PickItApp pickItApp;
    EditText mUsernameRepresentation;
    EditText mPasswordRepresentation;
    ProgressBar loading;

    private static String username;
    private static String password;
    //endregion

    //region Life-cycle methods
    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_login);

        pickItApp = (PickItApp)getApplication();
        fileManager = new FileManager(this);
        mUsernameRepresentation = (EditText)findViewById(R.id.usernameLoginTextbox);
        mPasswordRepresentation = (EditText)findViewById(R.id.passwordLoginTextbox);
        loading = (ProgressBar)findViewById(R.id.loading);

        loading.setVisibility(View.INVISIBLE);

        readCredentials();
        updateScreenText();
        setEventListeners();
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

        readCredentials();
        updateScreenText();

        //Fb's Insights Dashboard - Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onStop(){
        endLoad();

        username = "";
        password = "";
        updateScreenText();

        super.onStop();
    }
    //endregion

    //region Input Handlers
    public void onClickCreateAccount(View v) {
        pickItApp.setGuest(false);
        Intent intent = new Intent(this, AccountAdminActivity.class);
        startActivity(intent);
    }

    public void onClickGuestLogin(View v) {
        pickItApp.setGuest(true);
        Intent intent = new Intent(this, AccountAdminActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View v) {
        startLoad();

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

                endLoad();

                //Take the user to the menu activity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return;
            }
        }

        endLoad();

        Context context = getApplicationContext();
        CharSequence text = "Invalid username or password\nPlease try again";
        int duration = Toast.LENGTH_LONG;

        Toast.makeText(context, text, duration).show();
    }
    //endregion

    //Helper Methods
    private void readCredentials(){
        ArrayList<String> credentials = fileManager.readSavedCredentials();
        if(credentials.size() == 2){
            username = credentials.get(0);
            password = credentials.get(1);
            CheckBox rememberMe = (CheckBox)findViewById(R.id.box_remember_me);
            rememberMe.setChecked(true);
        }
    }
    private void startLoad(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);

                enableLayoutChildren(false);
            }
        });
    }
    private void endLoad(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.INVISIBLE);

                enableLayoutChildren(true);
            }
        });
    }
    private void enableLayoutChildren(boolean enable){
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.AppLogin);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(enable);
        }
    }
    private void setEventListeners(){
        mUsernameRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                username = mUsernameRepresentation.getText().toString();
            }
        });

        mPasswordRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

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