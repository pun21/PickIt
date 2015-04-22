package com.bcdevops.pickit.database.handling.crud;

public class ReportCRUD extends CRUD {
    private static final String CREATE_REQUEST = "PickIt/php/report_pickIt.php";

    private int pickItID;

    public ReportCRUD(int pickItID){
        this.pickItID = pickItID;
    }

    protected String createExtension(){
        String extension = (CREATE_REQUEST + "?PickItID=" + this.pickItID);
        return extension;
    }
    protected String readExtension(){
        try {
            throw new Exception("Users cannot be deleted");
        }catch(Exception e){
        }
        return null;
    }

    protected String updateExtension(){
        try {
            throw new Exception("Users cannot be deleted");
        }catch(Exception e){
        }
        return null;
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
