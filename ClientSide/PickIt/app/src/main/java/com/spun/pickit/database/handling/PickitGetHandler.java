package com.spun.pickit.database.handling;

/**
 * Created by jacob_000 on 2/21/2015.
 */
public class PickitGetHandler{
    private static final String URL = "SOMETHING";
    private String userID;

    //this gets the Pickits of the specified userID
    public PickitGetHandler(String userID){
        this.userID = userID;
    }

    public String getExtension(){
        String extension = "/"+ this.URL + "?UserID="+this.userID;
        return extension;
    }
}
