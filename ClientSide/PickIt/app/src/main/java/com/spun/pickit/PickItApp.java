package com.spun.pickit;

import android.app.Application;

import com.spun.pickit.model.Demographics;

public class PickItApp extends Application {
    //region Demographics and user identification
    private int userID;
    private Demographics demographics;
    private String username;
    private String birthday;
    private String gender;
    private String ethnicity;
    private String religion;
    private String political;
    private boolean guest;

    public int resultsID;
    //endregion

    //region Accessor Methods
    public void setDemographics(Demographics demographics){
        this.demographics = demographics;

        setPolitical(demographics.getPoliticalAffiliation());
        setBirthday(demographics.getBirthday());
        setEthnicity(demographics.getEthnicity());
        setGender(demographics.getGender());
        setReligion(demographics.getReligion());
    }
    public void resetUser(){
        userID = -1;
        demographics = null;
        username = "";
        birthday = "";
        gender = "";
        ethnicity = "";
        religion = "";
        political = "";
        guest = false;
    }
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
    public void setBirthday(String birthday){
        this.birthday = birthday;
    }
    public String getBirthday(){
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
    //endregion
}
