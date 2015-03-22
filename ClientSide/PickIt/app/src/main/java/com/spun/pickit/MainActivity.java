package com.spun.pickit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
    //region Class Variables
    PickItApp pickItApp;
    //endregion

    //region Life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickItApp = (PickItApp)getApplication();

        setUsername();

        //need to set current user's username
        TextView username = (TextView) findViewById(R.id.textView_username);
        //username.setText(/*get username here*/);
    }
    //endregion

    //region UI Handlers
    public void onClickUpload(View v) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

    public void onClickUsername(View v) {

        //go to Profile Admin Activity
        Intent intent = new Intent(this, ProfileAdminActivity.class);
        startActivity(intent);
    }

    public void onClickSignOut(View v) {

        //do any sign out stuff

        //go to login page after signing out
        Intent intent = new Intent(this, AppLoginActivity.class);
        startActivity(intent);
    }


    /*Handler methods for results pane toggles ---------------------------------------------------*/

    public void onClickRecencyToggle(View v) {
        ToggleButton tog = (ToggleButton) findViewById(R.id.toggle_recency);
        //toggle button default is sorting by most recent
        if (!tog.isChecked()) {
            //sorting by most recent
            Toast.makeText(this, "Recency Off", Toast.LENGTH_SHORT).show();
        }
        else {
            //sorting by least recent
            Toast.makeText(this, "Recency On", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickTrendingToggle(View v) {
        ToggleButton tog = (ToggleButton) findViewById(R.id.toggle_trending);

        if (!tog.isChecked()) {
            //sorting by most trending
            Toast.makeText(this, "Trending Off", Toast.LENGTH_SHORT).show();
        }
        else {
            //sorting by least trending
            Toast.makeText(this, "Trending On", Toast.LENGTH_SHORT).show();
        }


    }

    public void onClickTimeRemainingToggle(View v) {
        ToggleButton tog = (ToggleButton) findViewById(R.id.toggle_time_remaining);

        if (!tog.isChecked()) {
            //sorting by least voting time remaining
            Toast.makeText(this, "Time Remaining Off", Toast.LENGTH_SHORT).show();
        }
        else {
            //sorting by most voting time remaining
            Toast.makeText(this, "Time Remaining On", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region Helper Methods
    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);
        username.setText(pickItApp.getUsername());
    }
    //endregion
}