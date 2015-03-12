package com.spun.pickit;

import android.app.Application;

import java.util.Date;

/**
 * Created by BJClark on 3/10/2015.
 */
public class PickItApp extends Application {
    private int userID = -1;
    private String username = "";
    private Date birthday = new Date(1900, 0, 0);
    private String gender = "";
    private String ethnicity = "";
    private String religion = "";
    private String political = "";
    private boolean guest = false;

    public void setUserID(int userID){
        this.userID = userID;
    }
    public int getUserID(){
        return userID;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }
    public Date getBirthday(){
        return birthday;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return gender;
    }
    public void setEthnicity(String ethnicity){
        this.ethnicity = ethnicity;
    }
    public String getEthnicity(){
        return ethnicity;
    }
    public void setReligion(String religion){
        this.religion = religion;
    }
    public String getReligion(){
        return religion;
    }
    public void setPolitical(String political){
        this.political = political;
    }
    public String getPolitical(){
        return political;
    }
    public void setGuest(boolean guest){
        this.guest = guest;
    }
    public boolean isGuest(){
        return guest;
    }
}
