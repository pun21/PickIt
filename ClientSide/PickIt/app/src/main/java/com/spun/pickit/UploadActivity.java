package com.spun.pickit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UploadActivity extends ActionBarActivity {

    private String pickItHeading;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int LOAD_IMAGE_REQUEST_CODE = 200;
    private static final int SELECT_PICTURE = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private ImageView imageTopLeft, imageTopRight, imageBottomLeft, imageBottomRight;

    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        final EditText editText = (EditText) findViewById(R.id.textField_pickit_heading);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    pickItHeading = editText.getText().toString();
                    handled = true;
                }
                return handled;
            }
        });

        imageTopLeft = (ImageView) findViewById(R.id.imageView);
        imageTopRight = (ImageView) findViewById(R.id.imageView2);
        imageBottomLeft = (ImageView) findViewById(R.id.imageView3);
        imageBottomLeft = (ImageView) findViewById(R.id.imageView4);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
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
    public void onClickUpload(View v) {

        //TODO do upload stuff - save the heading and pictures to database

        //go to Results Activity
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);

    }
    public void onClickImage(View v) {
        //TODO
        //identify the right image and bring back the right gallery camera icons
        //__.setVisibility(VISIBLE);


    }
    public void onClickGallery(View v) {
        //open gallery, select picture, set imageView as picture, set gallery icon and camera icon invisible/inactive
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

        //TODO
        //once imageView has been set with image from gallery, make the gallery and camera icons invisible

    }
    public void onClickCamera(View v) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, this); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type, Context context){
        return Uri.fromFile(getOutputMediaFile(type, context));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type, Context context){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        // This location is only for pictures which are associated
        //with your application. If your application is uninstalled, any files
        //saved in this location are removed.
        File mediaStorageDir = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        /*// This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");*/



        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //gallery
        if (requestCode == LOAD_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {

        }
    }
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}