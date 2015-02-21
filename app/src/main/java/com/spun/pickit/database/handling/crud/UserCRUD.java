package com.spun.pickit.database.handling.crud;


// use CRUD to read, delete, update, and create Users
public class UserCRUD extends CRUD{
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

    public UserCRUD(String userID){
        this.userID = userID;
    }

    protected String readExtension(){
        String extension = READ_REQUEST +"?Username="+ this.username + "&Password=" +this.password;
        return extension;
    }

    //Since a user cannot delete themselves, this will return null
    protected String deleteExtension(){
        try {
            throw new Exception("Users can not be deleted");
        }catch(Exception e){
        }
        return null;
    }

    protected String updateExtension(){
        String extension = (UPDATE_REQUEST + "?UserID=" + this.userID + "&Username=" + this.username
                + "&Password=" + this.password + "&Birthday=" + this.birthday + "&Gender=" + this.gender
                + "&Ethnicity=" + this.ethnicity + "&Religion=" + this.religion
                + "&PoliticalAffiliation=" + this.politicalAffiliation);
        return extension;
    }
    protected String createExtension(){
        String extension = (CREATE_REQUEST + "?Username=" + this.username + "&Password=" + this.password +
                "&Birthday=" + this.birthday + "&Gender=" + this.gender + "&Ethnicity=" + this.ethnicity
                + "&Religion=" + this.religion + "&PoliticalAffiliation=" + this.politicalAffiliation);
        return extension;
    }

    protected void readPrimitive(){

    }

    protected void deletePrimitive(){


    }

    protected void updatePrimitive(){

    }

    protected void createPrimitive(){
    }
}
