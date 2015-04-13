package com.bcdevops.pickit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.bcdevops.pickit.database.handling.DatabaseAccess;
import com.bcdevops.pickit.fileIO.*;
import com.bcdevops.pickit.model.Demographics;
import com.bcdevops.pickit.model.User;

import java.util.ArrayList;

public class LoginActivity extends Activity {
    //region Activity Variables
    LocalFileManager localFileManager;
    PickItApp pickItApp;
    EditText mUsernameRepresentation;
    EditText mPasswordRepresentation;
    ProgressBar loading;

    private static String username;
    private static String password;
    private static int layoutID;
    //endregion

    //region Life-cycle methods
    @Override
    protected void onStart(){
        super.onStart();


        this.layoutID = R.id.AppLoginDefault;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pickItApp = (PickItApp)getApplication();

        localFileManager = new LocalFileManager(this);
        mUsernameRepresentation = (EditText)findViewById(R.id.usernameLoginTextbox);
        mPasswordRepresentation = (EditText)findViewById(R.id.passwordLoginTextbox);
        loading = (ProgressBar)findViewById(R.id.loading);

        readCredentials();
        updateScreenText();
    }

    @Override
    protected void onResume() {
        super.onResume();

        endLoad();
        readCredentials();
        updateScreenText();
        pickItApp.resetUser();

        //Fb's Insights Dashboard - Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
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
    protected  void onStop() {
        endLoad();

        super.onStop();
    }
    //endregion

    //region Input Handlers
    public void onClickCreateAccount(View v) {
        pickItApp.setGuest(false);
        Intent intent = new Intent(this, ProfileAdminActivity.class);
        startActivity(intent);
    }

    public void onClickGuestLogin(View v) {
        pickItApp.setGuest(true);
        Intent intent = new Intent(this, ProfileAdminActivity.class);
        startActivity(intent);
    }

    public void onClickLogin(View v) {
        startLoad();

        username = mUsernameRepresentation.getText().toString();
        password = mPasswordRepresentation.getText().toString();

        final LoginActivity activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                new AsyncLogIn(activity).start();
            }
        }).start();
    }
    //endregion

    //region Helper Methods
    private void setUserInformation(int userID, String username, Demographics demo){
        pickItApp.setUserID(userID);
        pickItApp.setUsername(username);

        pickItApp.setDemographics(demo);
    }
    private void readCredentials(){
        ArrayList<String> credentials = localFileManager.readSavedCredentials();
        if(credentials.size() == 2){
            username = credentials.get(0);
            password = credentials.get(1);
            CheckBox rememberMe = (CheckBox)findViewById(R.id.box_remember_me);
            rememberMe.setChecked(true);
        }
    }
    private void updateScreenText(){
        mUsernameRepresentation.setText(username);
        mPasswordRepresentation.setText(password);
    }
    public void startLoad(){
        setEnabled(false);
        loading.setVisibility(View.VISIBLE);
    }
    public void endLoad(){
        setEnabled(true);
        loading.setVisibility(View.INVISIBLE);
    }
    private void setEnabled(boolean enabled){
        RelativeLayout layout = (RelativeLayout) findViewById(layoutID);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if(child.getId() != R.id.loading)
                child.setEnabled(enabled);
        }
    }
    //endregion

    class AsyncLogIn extends Thread{
        final LoginActivity  activity;

        public AsyncLogIn(LoginActivity  activity){
            this.activity = activity;
        }

        @Override
        public void run(){
            if(!username.equals("") && !password.equals("")){
                //Create access object to validate username/password input
                DatabaseAccess access = new DatabaseAccess();
                User user= access.validatePassword(username,password);

                if (user != null){
                    //If the password is valid and the checkbox is checked, save username/password locally
                    CheckBox rememberMe = (CheckBox)findViewById(R.id.box_remember_me);
                    if(rememberMe.isChecked()){
                        localFileManager.saveCredentials(username, password);
                    }else{
                        if(localFileManager.credentialFileExists()){
                            localFileManager.deleteCredentials();
                        }
                    }

                    Demographics demo = new Demographics(user.getBirthday(), user.getGender(), user.getEthnicity(), user.getReligion(), user.getPoliticalAffiliation());

                    setUserInformation(user.getID(), user.getUsername(), demo);

                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                    return;
                }
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context context = activity.getApplicationContext();
                    CharSequence text = "Invalid log in credentials";
                    int length = Toast.LENGTH_LONG;

                    Toast.makeText(context, text, length).show();

                    endLoad();
                }
            });
        }
    }
}