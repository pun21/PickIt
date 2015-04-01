package com.spun.pickit;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.spun.pickit.model.Demographics;

import java.util.Objects;

public class PickItApp extends Application {
    //region Class variables
    //region Keys
    private static final String PREFS_NAME = "PickItUserData";
    private static final String KEY_USERID = "userID";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ETHNICITY = "ethnicity";
    private static final String KEY_RELIGION = "religion";
    private static final String KEY_POLITICAL = "political";
    private static final String KEY_GUEST = "guest";
    private static final String KEY_RESULT_ID = "nextResultID";
    //endregion

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

    private int resultPickItID;
    //endregion
    //endregion

    //region Life cycle methods
    @Override
    public void onCreate() {
        super.onCreate();

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
    //endregion

    //region Accessor Methods
    public void setResultPickItID(int resultPickItID){
        this.resultPickItID = resultPickItID;

        setInSharedPreferences(KEY_RESULT_ID, resultPickItID);
    }
    public int getResultPickItID(){
        if(resultPickItID != 0)
            return resultPickItID;

        return getFromSharedPreferences(KEY_RESULT_ID, 0);
    }
    public void setDemographics(Demographics demographics){
        this.demographics = demographics;

        setPolitical(demographics.getPoliticalAffiliation());
        setBirthday(demographics.getBirthday());
        setEthnicity(demographics.getEthnicity());
        setGender(demographics.getGender());
        setReligion(demographics.getReligion());
    }
    public void resetUser(){
        setDemographics(null);
        setUserID(0);
        setUsername("");
        setBirthday("");
        setGender("");
        setEthnicity("");
        setReligion("");
        setPolitical("");
        setGuest(false);
    }
    public void setUserID(int userID){
        this.userID = userID;

        setInSharedPreferences(KEY_USERID, userID);
    }
    public int getUserID(){
        if(userID != 0)
            return userID;

        return getFromSharedPreferences(KEY_USERID, 0);
    }
    public void setUsername(String username){
        this.username = username;

        setInSharedPreferences(KEY_USERNAME, username);
    }
    public String getUsername(){
        if(!username.equals(""))
            return username;

        return getFromSharedPreferences(KEY_USERNAME, "");
    }
    public void setBirthday(String birthday){
        this.birthday = birthday;

        setInSharedPreferences(KEY_BIRTHDAY, birthday);
    }
    public String getBirthday(){
        if(!birthday.equals(""))
            return birthday;

        return getFromSharedPreferences(KEY_BIRTHDAY, "");
    }
    public void setGender(String gender){
        this.gender = gender;

        setInSharedPreferences(KEY_GENDER, gender);
    }
    public String getGender(){
        if(!gender.equals(""))
            return gender;

        return getFromSharedPreferences(KEY_GENDER, "");
    }
    public void setEthnicity(String ethnicity){
        this.ethnicity = ethnicity;

        setInSharedPreferences(KEY_ETHNICITY, ethnicity);
    }
    public String getEthnicity(){
        if(!ethnicity.equals(""))
            return ethnicity;

        return getFromSharedPreferences(KEY_ETHNICITY, "");
    }
    public void setReligion(String religion){
        this.religion = religion;

        setInSharedPreferences(KEY_RELIGION, religion);
    }
    public String getReligion(){
        if(!religion.equals(""))
            return religion;

        return getFromSharedPreferences(KEY_RELIGION, "");
    }
    public void setPolitical(String political){
        this.political = political;

        setInSharedPreferences(KEY_POLITICAL, political);
    }
    public String getPolitical(){
        if(!political.equals(""))
            return political;

        return getFromSharedPreferences(KEY_POLITICAL, "");
    }
    public void setGuest(boolean guest){
        this.guest = guest;

        setInSharedPreferences(KEY_GUEST, guest);
    }
    public boolean isGuest(){
        return getFromSharedPreferences(KEY_GUEST, false);
    }
    //endregion

    //region Helper methods
    private void setInSharedPreferences(String key, String value){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(key, value);
        editor.commit();
    }
    private void setInSharedPreferences(String key, int value){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(key, value);
        editor.commit();
    }
    private void setInSharedPreferences(String key, boolean value){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }
    private String getFromSharedPreferences(String key, String defaultValue){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString(key, defaultValue);

        return value;
    }
    private int getFromSharedPreferences(String key, int defaultValue){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int value = settings.getInt(key, defaultValue);

        return value;
    }
    private boolean getFromSharedPreferences(String key, boolean defaultValue){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean value = settings.getBoolean(key, defaultValue);

        return value;
    }
    //endregion
}
