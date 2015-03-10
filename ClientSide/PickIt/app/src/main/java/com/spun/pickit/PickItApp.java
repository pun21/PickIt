package com.spun.pickit;

import android.app.Application;

/**
 * Created by BJClark on 3/10/2015.
 */
public class PickItApp extends Application {
    private String username;
    private int userID;

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }
    public int getUserID(){
        return userID;
    }
}
