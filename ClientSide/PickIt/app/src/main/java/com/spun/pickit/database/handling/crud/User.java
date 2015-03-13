package com.spun.pickit.database.handling.crud;

import java.util.Date;

public class User extends CRUD{
    private static final String READ_REQUEST = "validate_password.php";
    private static final String UPDATE_REQUEST = "update_user.php";
    private static final String CREATE_REQUEST = "create_user.php";

    private String userID;
    private String username;
    private String password;
    private String birthday;
    private String gender;
    private String ethnicity;
    private String religion;
    private String politicalAffiliation;

    public User(String userID){
        this.userID = userID;
    }

    public User(String username, String password, String birthday, String gender, String ethnicity, String religion, String politicalAffiliation){
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.politicalAffiliation = politicalAffiliation;
    }

    public String readExtension(){
        String extension = READ_REQUEST +"?UserID="+ this.username;
        return extension;
    }

    //Since a user cannot delete themselves, this will return null
    public String deleteExtension(){
        try {
            throw new Exception("Users can not be deleted");
        }catch(Exception e){
        }
        return null;
    }

    public String updateExtension(){
        String extension = (UPDATE_REQUEST + "?UserID=" + this.userID + "&Username=" + this.username
                + "&Password=" + this.password + "&Birthday=" + this.birthday + "&Gender=" + this.gender
                + "&Ethnicity=" + this.ethnicity + "&Religion=" + this.religion
                + "&PoliticalAffiliation=" + this.politicalAffiliation);
        return extension;
    }
    public String createExtension(){
        String extension = (CREATE_REQUEST + "?Username=" + this.username + "&Password=" + this.password +
                "&Birthday=" + this.birthday + "&Gender=" + this.gender + "&Ethnicity=" + this.ethnicity
                + "&Religion=" + this.religion + "&PoliticalAffiliation=" + this.politicalAffiliation);
        return extension;
    }
}
