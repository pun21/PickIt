package com.spun.pickit;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spun.pickit.model.SelectDateFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UploadActivity extends FragmentActivity {

    PickItApp pickItApp;
    private String pickItHeading;
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int LOAD_IMAGE_REQUEST_CODE = 200;
    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_SOURCE = 10;
    private static final int GALLERY_SOURCE = 20;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private ImageView imageTopLeft, imageTopRight, imageBottomLeft, imageBottomRight, mImageView;

    private int selectedImageId, selectedImage;
    private Uri fileUri;
    private static EditText mEdit, mTimeEdit;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        pickItApp = (PickItApp)getApplication();
        username = pickItApp.getUsername();

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

        imageTopLeft = (ImageView) findViewById(R.id.imageViewTopLeft);
        imageTopRight = (ImageView) findViewById(R.id.imageViewTopRight);
        imageBottomLeft = (ImageView) findViewById(R.id.imageViewBottomLeft);
        imageBottomRight = (ImageView) findViewById(R.id.imageViewBottomRight);


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
        //if imageView contains an image, bring back gallery and camera buttons
        if (((ImageView) v).getDrawable() != null) {
            setViewsVisibility(v);
        }
    }
    public void onClickGallery(View v) {
        selectedImageId = v.getId();
        //open gallery, select picture, set imageView as picture, set gallery icon and camera icon invisible/inactive
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_REQUEST_CODE);

        //TODO
        //once imageView has been set with image from gallery, make the gallery and camera icons invisible

    }
    public void onClickCamera(View v) {
        selectedImageId = v.getId();

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, this); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);

    }

    private void setViewsVisibility(View v){
        ImageButton button;
        int id = v.getId();

        if (id == R.id.imageViewTopLeft) {
            button = (ImageButton) findViewById(R.id.cameraButtonTopLeft);
            button.setVisibility(View.VISIBLE);
            button = (ImageButton) findViewById(R.id.galleryButtonTopLeft);
            button.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.imageViewTopRight) {
            button = (ImageButton) findViewById(R.id.cameraButtonTopRight);
            button.setVisibility(View.VISIBLE);
            button = (ImageButton) findViewById(R.id.galleryButtonTopRight);
            button.setVisibility(View.VISIBLE);
        }

        else if (id == R.id.imageViewBottomLeft) {
            button = (ImageButton) findViewById(R.id.cameraButtonBottomLeft);
            button.setVisibility(View.VISIBLE);
            button = (ImageButton) findViewById(R.id.galleryButtonBottomLeft);
            button.setVisibility(View.VISIBLE);
        }
        else if (id == R.id.imageViewBottomRight) {
            button = (ImageButton) findViewById(R.id.cameraButtonBottomRight);
            button.setVisibility(View.VISIBLE);
            button = (ImageButton) findViewById(R.id.galleryButtonBottomRight);
            button.setVisibility(View.VISIBLE);
        }

    }
    private void setImageView(int source, Bitmap bitmap) {

        ImageButton button;

        if (source == GALLERY_SOURCE) {  //gallery button was clicked
            if (selectedImageId == R.id.galleryButtonTopLeft) {
                imageTopLeft.setImageBitmap(bitmap);
                button = (ImageButton) findViewById(R.id.cameraButtonTopLeft);
                button.setVisibility(View.INVISIBLE);
            } else if (selectedImageId == R.id.galleryButtonTopRight) {
                imageTopRight.setImageBitmap(bitmap);
                button = (ImageButton) findViewById(R.id.cameraButtonTopRight);
                button.setVisibility(View.INVISIBLE);
            } else if (selectedImageId == R.id.galleryButtonBottomLeft) {
                imageBottomLeft.setImageBitmap(bitmap);
                button = (ImageButton) findViewById(R.id.cameraButtonBottomLeft);
                button.setVisibility(View.INVISIBLE);
            } else {
                imageBottomRight.setImageBitmap(bitmap);
                button = (ImageButton) findViewById(R.id.cameraButtonBottomRight);
                button.setVisibility(View.INVISIBLE);
            }
        }
        else {  //camera button was clicked
            if (selectedImageId == R.id.cameraButtonTopLeft) {
                imageTopLeft.setImageBitmap(bitmap);
                button = (ImageButton) findViewById(R.id.galleryButtonTopLeft);
                button.setVisibility(View.INVISIBLE);
            }
            else if (selectedImageId == R.id.cameraButtonTopRight) {
                imageTopRight.setImageBitmap(bitmap);
                button = (ImageButton) findViewById(R.id.galleryButtonTopRight);
                button.setVisibility(View.INVISIBLE);
            }
            else if (selectedImageId == R.id.cameraButtonBottomLeft) {
                imageBottomLeft.setImageBitmap(bitmap);
                button = (ImageButton) findViewById(R.id.galleryButtonBottomLeft);
                button.setVisibility(View.INVISIBLE);
            }
            else {
                imageBottomRight.setImageBitmap(bitmap);
                button = (ImageButton) findViewById(R.id.galleryButtonBottomRight);
                button.setVisibility(View.INVISIBLE);
            }
        }

        //set camera or gallery button invisible once bitmap is set in the ImageView
        button = (ImageButton)findViewById(selectedImageId);
        button.setVisibility(View.INVISIBLE);
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
                    "IMG_" + timeStamp + ".jpg");
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

        /*Note: maybe load bitmap on another thread? and maybe recycle bitmap? not sure about memory management*/

        //gallery
        if (requestCode == LOAD_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri galleryImage = data.getData();
            //Bitmap bitmap = null;

                new LoadBitmapTask().execute(galleryImage);
                /*bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), galleryImage);
                /*need to resize the bitmap somehow to fit the ImageView or display it better
                setImageView(GALLERY_SOURCE, bitmap);*/

        }
        else if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            try
            {
                new LoadBitmapTask().execute(null, fileUri);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                /*need to resize the bitmap somehow to fit the ImageView or display it better*/
                setImageView(CAMERA_SOURCE, bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

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

    public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }
    public void populateSetDate(int year, int month, int day) {
        mEdit = (EditText)findViewById(R.id.editText1);
        mEdit.setText(month+"/"+day+"/"+year);
    }

    public void selectTime(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }
    public void populateSetTime(int hour, int minute) {
        mTimeEdit = (EditText)findViewById(R.id.editText2);
        mTimeEdit.setText(hour+":"+minute);
    }

    /*loads the bitmaps to be set in the ImageViews from the camera and gallery*/
    private class LoadBitmapTask extends AsyncTask<Uri, Void, Bitmap[]> {

        protected Bitmap[] doInBackground(Uri...uri) {
            Bitmap bitmap[] = new Bitmap[2];
            try {
                if (uri[0] != null)
                    bitmap[0] = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri[0]);
                else
                    bitmap[1] = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri[1]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap[] result) {
            if (result[0] != null)
                setImageView(GALLERY_SOURCE, result[0]);
            else
                setImageView(CAMERA_SOURCE, result[1]);
        }
    }
}