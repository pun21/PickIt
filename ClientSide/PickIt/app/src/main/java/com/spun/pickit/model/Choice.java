package com.spun.pickit.model;

import android.graphics.Bitmap;

public class Choice {
    private Bitmap bitmap;
    private String filename;

    public Choice() {}

    public Choice(Bitmap bitmap){
        this.bitmap = bitmap;
        filename = "";
    }

    public Choice(Bitmap bitmap, String filename){
        this.bitmap = bitmap;
        this.filename = filename;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public String getFilename(){
        return filename;
    }
}
