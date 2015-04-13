package com.bcdevops.pickit.database.handling.crud;

public class PasswordValidation extends CRUD{
    private static final String VALIDATE = "PickIt/php/validate_password.php";
    private String username;
    private String password;

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
