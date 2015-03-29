package com.spun.pickit;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class ResultsActivity extends ActionBarActivity {

    private ImageView imageTopLeft, imageTopRight, imageBottomLeft, imageBottomRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

//        imageTopLeft = (ImageView) findViewById(R.id.r);
//        imageTopRight = (ImageView) findViewById(R.id.imageView2);
//        imageBottomLeft = (ImageView) findViewById(R.id.imageView3);
//        imageBottomLeft = (ImageView) findViewById(R.id.imageView4);

        //TODO retrieve images from database and set imageViews

        //TODO retrieve PickIt heading from database and set textField
        EditText heading = (EditText) findViewById(R.id.textField_pickit_heading);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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

    public void onClickNavHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickNavUpload(View v) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

    public void onClickUsername(View v) {

        //go to Profile Admin Activity
        Intent intent = new Intent(this, ProfileAdminActivity.class);
        startActivity(intent);
    }
    public void onClickSignOut(View v) {

        //TODO do any sign out stuff

        //go to login page after signing out
        Intent intent = new Intent(this, AppLoginActivity.class);
        startActivity(intent);
    }
}
