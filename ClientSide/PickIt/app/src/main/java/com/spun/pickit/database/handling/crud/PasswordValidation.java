package com.spun.pickit.database.handling.crud;

import org.json.JSONObject;

/**
 * Created by jacob_000 on 2/22/2015.
 */
public class PasswordValidation extends CRUD{
    private static final String VALIDATE = "validate_password.php";
    private String username;
    private String password;
    private boolean pass;

    public PasswordValidation(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String readExtension(){
        String extension = VALIDATE +"?Username="+ this.username + "&Password=" +this.password;
        return extension;
    }
    public String deleteExtension(){
        String extension = "";
        return extension;
    }
    public String updateExtension(){
        String extension = "";
        return extension;
    }
    public String createExtension(){
        String extension = "";
        return extension;
    }
}
