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

    protected String readExtension(){
        String extension = VALIDATE +"?Username="+ this.username + "&Password=" +this.password;
        return extension;
    }
    protected String deleteExtension(){
        String extension = "";;
        return extension;
    }
    protected String updateExtension(){
        String extension = "";
        return extension;
    }
    protected String createExtension(){
        String extension = "";;
        return extension;
    }

    protected void readPrimitive(JSONObject json){
        try {
            if (json.get("success")==1) {
                this.pass = true;
            }else{
                this.pass = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    protected void deletePrimitive(JSONObject json){}
    protected void updatePrimitive(JSONObject json){}
    protected void createPrimitive(JSONObject json){}

    public boolean getPass(){
        return this.pass;
    }


}
