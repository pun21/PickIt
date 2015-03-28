package com.spun.pickit.fileIO;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;

public class ServerFileManager {
    final private String KEY_URL_PREFIX = "http://www.bcdev.me:8080/PickIt/";
    final private String KEY_PICTURE_UPLOAD = "";
    final private String KEY_PICTURE_DOWNLOAD = "";
    final private String KEY_DEMOGRAPHICS_UPLOAD = "upload_demographics.php";
    final private String KEY_DEMOGRAPHICS_DOWNLOAD = "";
    final private Activity activity;
    final private String file;
    final private String filename;

    public ServerFileManager(){
        this.activity = null;
        file = null;
        filename = "";

    }

    public ServerFileManager(Activity activity, String file, String filename){
        this.activity = activity;
        this.file = file;
        this.filename = filename;
    }

    public void uploadDemographics(){
        final String url = KEY_URL_PREFIX + KEY_DEMOGRAPHICS_UPLOAD;
        final Posting posting = new Posting(activity, file, url, filename);

        new Thread(new Runnable() {
            @Override
            public void run() {
                posting.execute();
            }
        }).start();
    }
    public File downloadDemographics(String fileName){
        String url = KEY_URL_PREFIX + KEY_DEMOGRAPHICS_DOWNLOAD;

        return null;
    }
    public void uploadPicture(){
        String url = KEY_URL_PREFIX + KEY_PICTURE_UPLOAD;
        Posting posting = new Posting(activity, file, url, filename);
    }
    public Bitmap downloadPicture(String fileName) {
        String url = KEY_URL_PREFIX + KEY_PICTURE_DOWNLOAD;

        return null;
    }

    class AsyncFileUploaderThread extends Thread {
        final File file;
        final String url;

        public AsyncFileUploaderThread(File file, String url){
            this.file = file;
            this.url = url;
        }


        @Override
        public void run() {

        }
    }
}






