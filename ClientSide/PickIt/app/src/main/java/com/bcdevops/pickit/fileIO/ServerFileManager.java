package com.bcdevops.pickit.fileIO;

import android.app.Activity;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.bcdevops.pickit.database.handling.DatabaseAccess;
import com.bcdevops.pickit.model.PickIt;

import java.util.ArrayList;

public class ServerFileManager {
    //region Class variables
    final private String KEY_URL_PREFIX = "http://www.bcdev.me:8080/";
    final private String KEY_PICTURE_UPLOAD = "PickIt/php/upload_image.php";
    final private String KEY_PICTURE_DOWNLOAD = "PickIt/data/Images/";
    final private String KEY_PICKIT_UPLOAD = "PickIt/php/upload_pickit.php";
    final private String KEY_DEMOGRAPHICS_UPLOAD = "PickIt/php/upload_demographics.php";
    final private String KEY_MOST_RECENT_UPLOADS = "PickIt/php/most_recent_uploads.php";
    final private String KEY_TRENDING_UPLOADS = "PickIt/php/trending_uploads.php";
    final private String KEY_EXPIRING_UPLOADS = "PickIt/php/expiring_uploads.php";
    final private String KEY_USER_UPLOADS = "PickIt/php/users_uploads.php";
    final private String KEY_RECENT_USER_ACTIVITY = "PickIt/php/users_recent_activity.php";
    final private Activity activity;
    final private String file;
    final private String filename;
    //endregion

    //region Constructors
    public ServerFileManager(){
        this.activity = null;
        this.file = null;
        this.filename = "";
    }

    public ServerFileManager(Activity activity, String file, String filename){
        this.activity = activity;
        this.file = file;
        this.filename = filename;
    }
    //endregion

    //region File transfer methods
    public void uploadDemographics(){
        final String url = KEY_URL_PREFIX + KEY_DEMOGRAPHICS_UPLOAD;
        final Posting posting = new Posting(activity, file, url, filename, false);

        new AsyncFileUploaderThread(posting).start();
    }

    public void uploadPickIt(){
        final String url = KEY_URL_PREFIX + KEY_PICKIT_UPLOAD;
        final Posting posting = new Posting(activity, file, url, filename, false);
        new AsyncFileUploaderThread(posting).start();
    }

    public ArrayList<PickIt> getUploadedPickIts(String username){
        String url = KEY_URL_PREFIX + KEY_USER_UPLOADS + "?Username=" + username;
        DatabaseAccess access = new DatabaseAccess();
        ArrayList<PickIt> pickIts = access.getPickIts(url);
        return pickIts;
    }
    public ArrayList<PickIt> getRecentActivityPickIts(String username){
        String url = KEY_URL_PREFIX + KEY_RECENT_USER_ACTIVITY + "?Username=" + username;
        DatabaseAccess access = new DatabaseAccess();
        ArrayList<PickIt> pickIts = access.getPickIts(url);
        return pickIts;
    }
    public ArrayList<PickIt> downloadMostRecentPickIts(int quantity){
        String url = KEY_URL_PREFIX + KEY_MOST_RECENT_UPLOADS + "?NumPickIts=" + String.valueOf(quantity);
        DatabaseAccess access = new DatabaseAccess();
        ArrayList<PickIt> pickIts = access.getPickIts(url);
        return pickIts;
    }
    public ArrayList<PickIt> downloadTrendingPickIts(int quantity){
        String url = KEY_URL_PREFIX + KEY_TRENDING_UPLOADS + "?NumPickIts=" + String.valueOf(quantity);
        DatabaseAccess access = new DatabaseAccess();
        ArrayList<PickIt> pickIts = access.getPickIts(url);
        return pickIts;
    }
    public ArrayList<PickIt> downloadExpiringPickIts(int quantity){
        String url = KEY_URL_PREFIX + KEY_EXPIRING_UPLOADS + "?NumPickIts=" + String.valueOf(quantity);
        DatabaseAccess access = new DatabaseAccess();
        ArrayList<PickIt> pickIts = access.getPickIts(url);
        return pickIts;
    }

    public void uploadPicture(){
        final String url = KEY_URL_PREFIX + KEY_PICTURE_UPLOAD;
        final Posting posting = new Posting(activity, file, url, filename, true);

        new AsyncFileUploaderThread(posting).start();
    }
    public void downloadPicture(ImageView view, String filename) {
        String url = KEY_URL_PREFIX + KEY_PICTURE_DOWNLOAD + filename;

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(null)
                .showImageOnFail(null)
                .showImageOnLoading(null).build();

        imageLoader.displayImage(url, view, options);
    }
    //endregion

    // Async thread
    class AsyncFileUploaderThread extends Thread {
        final Posting posting;

        public AsyncFileUploaderThread(Posting posting){
            this.posting = posting;
        }

        @Override
        public void run() {
            posting.execute();
        }
    }
}