package com.spun.pickit.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Choice implements Parcelable{
    private int choiceID;
    private Bitmap bitmap;
    private String filename;

    public Choice() {}

    public Choice(Bitmap bitmap){
        this.bitmap = bitmap;
        filename = "";
    }

    public Choice(int choiceID, String filename){
        this.choiceID = choiceID;
        this.filename = filename;
    }

    public Choice(Bitmap bitmap, String filename){
        this.bitmap = bitmap;
        this.filename = filename;
    }

    public Choice(Parcel in) {
        this.choiceID = in.readInt();
        this.filename = in.readString();
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public String getFilename(){
        return filename;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(choiceID);
        dest.writeString(filename);
    }

    public static final Parcelable.Creator<Choice> CREATOR = new Parcelable.Creator<Choice>() {
        public Choice createFromParcel(Parcel in) {
            return new Choice(in);
        }
        public Choice[] newArray(int size) {
            return new Choice[size];
        }
    };
}
