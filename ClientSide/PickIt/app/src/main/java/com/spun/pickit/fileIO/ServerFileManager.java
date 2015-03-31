package com.spun.pickit.fileIO;

import android.app.Activity;
import android.graphics.Bitmap;

import com.spun.pickit.database.handling.DatabaseAccess;
import com.spun.pickit.model.PickIt;

import java.util.ArrayList;

public class ServerFileManager {
    //region Class variables
    final private String KEY_URL_PREFIX = "http://www.bcdev.me:8080/PickIt/php/";
    final private String KEY_PICTURE_UPLOAD = "upload_image.php";
    final private String KEY_PICTURE_DOWNLOAD = "download_image.php";
    final private String KEY_PICKIT_UPLOAD = "upload_pickit.php";
    final private String KEY_PICKIT_DOWNLOAD = "download_pickit.php";
    final private String KEY_DEMOGRAPHICS_UPLOAD = "upload_demographics.php";
    final private String KEY_DEMOGRAPHICS_DOWNLOAD = "download_demographics.php";
    final private String KEY_MOST_RECENT_UPLOADS = "most_recent_uploads.php";
    final private String KEY_TRENDING_UPLOADS = "trending_uploads.php";
    final private String KEY_EXPIRING_UPLOADS = "expiring_uploads.php";
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
    public Bitmap downloadPicture(String fileName) {
        String url = KEY_URL_PREFIX + KEY_PICTURE_DOWNLOAD;

        return null;
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






