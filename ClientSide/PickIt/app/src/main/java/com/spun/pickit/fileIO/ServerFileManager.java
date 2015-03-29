package com.spun.pickit.fileIO;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;

public class ServerFileManager {
    //region Class variables
    final private String KEY_URL_PREFIX = "http://www.bcdev.me:8080/PickIt/";
    final private String KEY_PICTURE_UPLOAD = "upload_image.php";
    final private String KEY_PICTURE_DOWNLOAD = "download_image.php";
    final private String KEY_DEMOGRAPHICS_UPLOAD = "upload_demographics.php";
    final private String KEY_DEMOGRAPHICS_DOWNLOAD = "download_demographics.php";
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
    public File downloadDemographics(String username){
        String url = KEY_URL_PREFIX + KEY_DEMOGRAPHICS_DOWNLOAD;

        return null;
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






