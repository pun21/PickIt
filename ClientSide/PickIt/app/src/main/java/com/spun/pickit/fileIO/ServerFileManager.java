package com.spun.pickit.fileIO;

import android.graphics.Bitmap;

import java.io.File;

public class ServerFileManager {
    final private String KEY_URL_PREFIX = "http://www.bcdev.me:8080/PickIt/";
    final private String KEY_PICTURE_UPLOAD = "";
    final private String KEY_PICTURE_DOWNLOAD = "";
    final private String KEY_DEMOGRAPHICS_UPLOAD = "upload_demographics.php";
    final private String KEY_DEMOGRAPHICS_DOWNLOAD = "";
    final private File file;

    public ServerFileManager(){
        file = null;
    }

    public ServerFileManager(File file){
        this.file = file;
    }

    public void uploadDemographics(){
        final String url = KEY_URL_PREFIX + KEY_DEMOGRAPHICS_UPLOAD;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Posting posting = new Posting(file, url);
            }
        }).start();
    }
    public File downloadDemographics(String fileName){
        String url = KEY_URL_PREFIX + KEY_DEMOGRAPHICS_DOWNLOAD;

        return null;
    }
    public void uploadPicture(){
        String url = KEY_URL_PREFIX + KEY_PICTURE_UPLOAD;
        Posting posting = new Posting(file, url);
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






