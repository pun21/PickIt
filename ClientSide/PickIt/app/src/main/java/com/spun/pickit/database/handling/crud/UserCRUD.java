package com.spun.pickit.database.handling.crud;

public class UserCRUD extends CRUD{
    private static final String UPDATE_REQUEST = "PickIt/php/update_user.php";
    private static final String CREATE_REQUEST = "PickIt/php/create_user.php";
    private static final String READ_REQUEST = "PickIt/php/read_user.php";

    private int userID;
    private String username;
    private String password;
    private String birthday;
    private String gender;
    private String ethnicity;
    private String religion;
    private String politicalAffiliation;

    public UserCRUD(int userID){
        this.userID = userID;
    }

    public UserCRUD(String username, String password){
        this.username = username;
        this.password = password;
    }

    public UserCRUD(int userID, String username, String password, String birthday, String gender, String ethnicity, String religion, String politicalAffiliation){
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.politicalAffiliation = politicalAffiliation;
    }

    public UserCRUD(String username, String password, String birthday, String gender, String ethnicity, String religion, String politicalAffiliation){
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.politicalAffiliation = politicalAffiliation;
    }

    protected String createExtension(){
        String extension = (CREATE_REQUEST + "?Username=" + this.username + "&Password=" + this.password);
        return extension;
    }
    protected String readExtension(){
        String extension = READ_REQUEST +"?UserID="+ this.username;
        return extension;
    }

    protected String updateExtension(){
        String extension = (UPDATE_REQUEST + "?UserID=" + this.userID + "&Username=" + this.username
                + "&Password=" + this.password);
        return extension;
    }

    //Since a user cannot delete themselves, this will return null
    protected String deleteExtension(){
        try {
            throw new Exception("Users cannot be deleted");
        }catch(Exception e){
        }
        return null;
    }
}
