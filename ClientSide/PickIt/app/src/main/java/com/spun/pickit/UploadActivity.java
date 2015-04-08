package com.spun.pickit;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spun.pickit.database.handling.DatabaseAccess;
import com.spun.pickit.fileIO.LocalFileManager;
import com.spun.pickit.fileIO.ServerFileManager;
import com.spun.pickit.model.Choice;
import com.spun.pickit.model.PickIt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UploadActivity extends FragmentActivity {
    //region Class Variables
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int LOAD_IMAGE_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private PickItApp pickItApp;

    private ImageView imageTopLeft, imageTopRight, imageBottomLeft, imageBottomRight;
    private EditText mEdit, mTimeEdit, mSubjectHeading;
    private Spinner mCategory;
    private ProgressBar loading;
    private LocalFileManager localFileManager;

    ArrayAdapter<CharSequence> mCategoriesAdapter;

    private int selectedImageId;
    private Uri fileUri;
    //endregion

    //region Activity Life-cycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        pickItApp = (PickItApp)getApplication();

        loading = (ProgressBar) findViewById(R.id.loading);

        imageTopLeft = (ImageView) findViewById(R.id.row0column0);
        imageTopRight = (ImageView) findViewById(R.id.row0column1);
        imageBottomLeft = (ImageView) findViewById(R.id.row1column0);
        imageBottomRight = (ImageView) findViewById(R.id.row1column1);

        mSubjectHeading = (EditText) findViewById(R.id.upload_description);
        mCategory = (Spinner) findViewById(R.id.category_spinner);

        localFileManager = new LocalFileManager(this);

        setUsername();
        setSpinners();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch(requestCode){
                case LOAD_IMAGE_REQUEST_CODE:
                    Uri galleryImage = data.getData();
                    new LoadBitmapTask(galleryImage, this, selectedImageId).start();
                    break;

                case CAPTURE_IMAGE_REQUEST_CODE:
                    new LoadBitmapTask(fileUri, this, selectedImageId).start();
                    break;

                default:
                    try {
                        throw new Exception("Error: Invalid Request Code");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
    //endregion

    //region Input Handlers

    public void onClickUsername(View v) {
        if(!pickItApp.isGuest()){
            //go to Main Activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onClickSignOut(View v) {
        //go to login page after signing out
        pickItApp.resetUser();

        Intent intent = new Intent(this, AppLoginActivity.class);
        startActivity(intent);
    }

    public void onClickUpload(View v) {
        boolean validInputs = validatePickItInputData();

            if(validInputs){
            final ArrayList<Choice> choices = getChoicesFromView();
            final String category = getCategoryFromView();
            final String subjectHeader = getSubjectHeaderFromView();
            final String endTime = getEndTimeFromView();
            final UploadActivity activity = this;

            final PickIt pickIt = new PickIt(choices, pickItApp.getUserID(), category, subjectHeader, Integer.parseInt(endTime));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    new SaveToServer(activity, pickIt).start();
                }
            }).start();

            disableFrontEnd();
        }
    }

    //region Image Handling
    public void onClickImage(View v) {
        //if imageView contains an image, bring back gallery and camera buttons
        if (((ImageView) v).getDrawable() != null) {
            setViewsVisibility(v);
        }
    }

    public void onClickGallery_r0c0(View v) {
        selectedImageId = R.id.row0column0;
        //open gallery, select picture, set imageView as picture, set gallery icon and camera icon invisible/inactive
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_REQUEST_CODE);
    }

    public void onClickGallery_r0c1(View v) {
        selectedImageId = R.id.row0column1;
        //open gallery, select picture, set imageView as picture, set gallery icon and camera icon invisible/inactive
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_REQUEST_CODE);
    }

    public void onClickGallery_r1c0(View v) {
        selectedImageId = R.id.row1column0;
        //open gallery, select picture, set imageView as picture, set gallery icon and camera icon invisible/inactive
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_REQUEST_CODE);
    }

    public void onClickGallery_r1c1(View v) {
        selectedImageId = R.id.row1column1;
        //open gallery, select picture, set imageView as picture, set gallery icon and camera icon invisible/inactive
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_REQUEST_CODE);
    }

    public void onClickCamera_r0c0(View v) {
        selectedImageId = R.id.row0column0;

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, this); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onClickCamera_r0c1(View v) {
        selectedImageId = R.id.row0column1;

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, this); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onClickCamera_r1c0(View v) {
        selectedImageId = R.id.row1column0;

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, this); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onClickCamera_r1c1(View v) {
        selectedImageId = R.id.row1column1;

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, this); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }
    //endregion
    //endregion

    //region Helper Methods
    private boolean validatePickItInputData(){
        if(getNumberOfUploadedImages() < 2){
            Context context = getApplicationContext();
            CharSequence error = "Please upload more photos! (2 min)";
            int length = Toast.LENGTH_SHORT;

            Toast.makeText(context, error, length).show();
            return false;
        }

        return true;
    }

    private int getNumberOfUploadedImages(){
        int counter = 0;

        if(imageTopLeft.getDrawable() != null)
            counter++;
        if(imageTopRight.getDrawable() != null)
            counter++;
        if(imageBottomLeft.getDrawable() != null)
            counter++;
        if(imageBottomRight.getDrawable() != null)
            counter++;

        return counter;
    }

    private String getEndTimeFromView(){
//        EditText dateEdit = (EditText)findViewById(R.id.editDate);
//        EditText timeEdit = (EditText)findViewById(R.id.editTime);

//        String date = dateEdit.getText().toString();
//
//        if(date == "")
            return String.valueOf(7*60*60*24);
//
//        String endTime = date + " " + timeEdit.getText()+":00";
//
//        return endTime;
    }

    private String getCategoryFromView(){
        return mCategory.getSelectedItem().toString().equals("Select a category?") ? "" : mCategory.getSelectedItem().toString();
    }

    private String getSubjectHeaderFromView(){
        return mSubjectHeading.getText().toString();
    }

    private ArrayList<Choice> getChoicesFromView(){
        ArrayList<Choice> choices = new ArrayList<>();

        if(imageTopLeft.getDrawable() != null){
            Bitmap bitmap = ((BitmapDrawable)imageTopLeft.getDrawable()).getBitmap();
            String filename = "1.png";

            Choice choice = new Choice(bitmap, filename);
            choices.add(choice);
        }

        if(imageTopRight.getDrawable() != null){
            Bitmap bitmap = ((BitmapDrawable)imageTopRight.getDrawable()).getBitmap();
            String filename = "2.png";

            Choice choice = new Choice(bitmap, filename);
            choices.add(choice);
        }

        if(imageBottomLeft.getDrawable() != null){
            Bitmap bitmap = ((BitmapDrawable)imageBottomLeft.getDrawable()).getBitmap();
            String filename = "3.png";

            Choice choice = new Choice(bitmap, filename);
            choices.add(choice);
        }

        if(imageBottomRight.getDrawable() != null){
            Bitmap bitmap = ((BitmapDrawable)imageBottomRight.getDrawable()).getBitmap();
            String filename = "4.png";

            Choice choice = new Choice(bitmap, filename);
            choices.add(choice);
        }

        return choices;
    }

    private void disableFrontEnd(){
        View frontEnd = findViewById(R.id.upload_activity_table);
        setEnable((ViewGroup)frontEnd.getRootView(), false);

        loading.setVisibility(View.VISIBLE);
    }

    private void enableFrontEnd(){
        View frontEnd = findViewById(R.id.upload_activity_table);
        setEnable((ViewGroup)frontEnd.getRootView(), true);

        loading.setVisibility(View.INVISIBLE);
    }

    private static void setEnable(ViewGroup layout, boolean enable) {
        layout.setEnabled(enable);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                setEnable((ViewGroup) child, enable);
            } else {
                child.setEnabled(enable);
            }
        }
    }

    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);

        if(pickItApp.isGuest()){
            username.setEnabled(false);
        }

        username.setText(pickItApp.getUsername());
    }

    private void setViewsVisibility(View v){
        ImageButton button;
        selectedImageId = v.getId();

        resetImageBackground();

        if (selectedImageId == R.id.row0column0) {
            button = (ImageButton) findViewById(R.id.cameraUpload_r0c0);
            button.setVisibility(View.VISIBLE);
            button = (ImageButton) findViewById(R.id.galleryUpload_r0c0);
            button.setVisibility(View.VISIBLE);
        }
        else if (selectedImageId == R.id.row0column1) {
            button = (ImageButton) findViewById(R.id.cameraUpload_r0c1);
            button.setVisibility(View.VISIBLE);
            button = (ImageButton) findViewById(R.id.galleryUpload_r0c1);
            button.setVisibility(View.VISIBLE);
        }

        else if (selectedImageId == R.id.row1column0) {
            button = (ImageButton) findViewById(R.id.cameraUpload_r1c0);
            button.setVisibility(View.VISIBLE);
            button = (ImageButton) findViewById(R.id.galleryUpload_r1c0);
            button.setVisibility(View.VISIBLE);
        }
        else if (selectedImageId == R.id.row1column1) {
            button = (ImageButton) findViewById(R.id.cameraUpload_r1c1);
            button.setVisibility(View.VISIBLE);
            button = (ImageButton) findViewById(R.id.galleryUpload_r1c1);
            button.setVisibility(View.VISIBLE);
        }
    }

    private void resetImageBackground(){
        ImageView image = (ImageView)findViewById(selectedImageId);
        image.setImageDrawable(null);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            image.setBackgroundDrawable( getResources().getDrawable(R.drawable.custom_upload_border) );
        } else {
            image.setBackground( getResources().getDrawable(R.drawable.custom_upload_border));
        }
    }

    private void setImageView(int source, Bitmap bitmap) throws Exception {
        ImageButton button;

        switch(source){
            case R.id.row0column0:
                imageTopLeft.setImageBitmap(bitmap);
                imageTopLeft.setBackground(null);

                button = (ImageButton)findViewById(R.id.cameraUpload_r0c0);
                button.setVisibility(View.INVISIBLE);

                button = (ImageButton)findViewById(R.id.galleryUpload_r0c0);
                button.setVisibility(View.INVISIBLE);
                break;
            case R.id.row0column1:
                imageTopRight.setImageBitmap(bitmap);
                imageTopRight.setBackground(null);

                button = (ImageButton)findViewById(R.id.cameraUpload_r0c1);
                button.setVisibility(View.INVISIBLE);

                button = (ImageButton)findViewById(R.id.galleryUpload_r0c1);
                button.setVisibility(View.INVISIBLE);
                break;
            case R.id.row1column0:
                imageBottomLeft.setImageBitmap(bitmap);
                imageBottomLeft.setBackground(null);

                button = (ImageButton)findViewById(R.id.cameraUpload_r1c0);
                button.setVisibility(View.INVISIBLE);

                button = (ImageButton)findViewById(R.id.galleryUpload_r1c0);
                button.setVisibility(View.INVISIBLE);
                break;
            case R.id.row1column1:
                imageBottomRight.setImageBitmap(bitmap);
                imageBottomRight.setBackground(null);

                button = (ImageButton)findViewById(R.id.cameraUpload_r1c1);
                button.setVisibility(View.INVISIBLE);

                button = (ImageButton)findViewById(R.id.galleryUpload_r1c1);
                button.setVisibility(View.INVISIBLE);
                break;
            default:
                throw new Exception("Error in setImageView: Invalid ID for Image");
        }
    }

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type, Context context){
        return Uri.fromFile(getOutputMediaFile(type, context));
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type, Context context){
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

    public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        mEdit = (EditText)findViewById(R.id.editDate);
        mEdit.setText(month + "/" + day + "/" + year);
    }

    public void selectTime(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    public void populateSetTime(int hour, int minute) {
        mTimeEdit = (EditText)findViewById(R.id.editTime);
        mTimeEdit.setText(hour + ":" + minute);
    }

    public void setSpinners() {
        //Gender options
        mCategoriesAdapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        mCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(mCategoriesAdapter);
    }
    //endregion

    /* Asynchronous Operations */
    class LoadBitmapTask extends Thread {
        Uri uri;
        UploadActivity activity;
        int viewID;

        public LoadBitmapTask(Uri uri, UploadActivity activity, int viewID){
            this.uri = uri;
            this.activity = activity;
            this.viewID = viewID;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }

            //Scaling
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setImageView(viewID, scaledBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    class SaveChoice {
        final Activity activity;
        final Choice choice;
        final File file;
        final String filename;
        final int pickItID;

        public SaveChoice(Activity activity, Choice choice, File file, int pickItID, String filename){
            this.activity = activity;
            this.choice = choice;
            this.file = file;
            this.pickItID = pickItID;
            this.filename = filename;
        }

        public void start(){
            saveImage();
            saveChoice();
        }

        private void saveChoice(){
            DatabaseAccess access = new DatabaseAccess();
            access.createChoice(pickItID, choice.getFilename());
        }

        private void saveImage(){
            ServerFileManager sm = new ServerFileManager(activity, file.getPath(), String.valueOf(pickItID)+"_"+filename);
            sm.uploadPicture();
        }

    }

    class SaveToServer extends Thread{
        final UploadActivity activity;
        final PickIt pickIt;

        public SaveToServer(UploadActivity activity, PickIt pickIt){
            this.activity = activity;
            this.pickIt = pickIt;
        }

        @Override
        public void run(){
            //Create PickItID for results reference
            DatabaseAccess access = new DatabaseAccess();
            int pickItID = access.createPickIt(pickIt.getUserID(), pickIt.getSecondsOfLife());
            String error = pickItID != 0 ? "" : "Error saving PickIt to database!";

            if(error != ""){
                updateScreen(error, pickItID);
                return;
            }

            pickIt.setPickItID(pickItID);

            sendPickItFile(pickIt);

            ArrayList<Choice> choices = pickIt.getChoices();
            for(int a = 0; a < choices.size(); a++){
                File savedFile = writeTempImageFile(choices.get(a).getBitmap(), choices.get(a).getFilename());

                if(savedFile == null){
                    error = "Could not save image locally";
                    updateScreen(error, pickItID);
                    return;
                }

                new SaveChoice(activity, choices.get(a), savedFile, pickItID, choices.get(a).getFilename()).start();
            }

            updateScreen("", pickItID);
        }

        private void updateScreen(final String error, final int pickItID){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UpdateScreen(activity, error, pickItID);
                }
            });
        }

        private File writeTempImageFile(Bitmap bitmap, String name) {
            File filesDir = activity.getApplicationContext().getFilesDir();
            File imageFile = new File(filesDir, name);

            OutputStream os;
            try {
                os = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                return null;
            }

            return imageFile;
        }

        private void sendPickItFile(final PickIt pickIt){
            savePickItLocally(pickIt);

            String pickItFilePath = localFileManager.getPickItFilePath();

            ServerFileManager sm = new ServerFileManager(activity, pickItFilePath, pickIt.getPickItID()+".json");
            sm.uploadPickIt();
        }

        private void savePickItLocally(final PickIt pickIt){
            localFileManager.savePickIt(pickIt.getCategory(), pickIt.getSubjectHeader(), pickIt.getPickItID());
        }

        private void UpdateScreen(final UploadActivity activity, final String error, final int nextResultID){
            if(error.length() == 0 && nextResultID != 0){
                pickItApp.setResultPickItID(nextResultID);
                activity.enableFrontEnd();
                SendUserToResultsActivity(activity);
            }
            else{
                EnableUIAndShowError(activity, error);
            }
        }

        private void EnableUIAndShowError(final UploadActivity activity, final String error){
            activity.enableFrontEnd();

            Context context = activity.getApplicationContext();
            int length = Toast.LENGTH_LONG;
            Toast.makeText(context, error, length).show();
        }

        private void SendUserToResultsActivity(final UploadActivity activity){
            //go to Results Activity
            Intent intent = new Intent(activity, Voting_ResultsActivity.class);
            intent.putExtra("com.spun.pickit", pickIt);
            startActivity(intent);
        }
    }
}
