package com.spun.pickit.database.handling.crud;
import org.json.JSONObject;

public class ChoiceCRUD extends CRUD{
    private static final String READ_CHOICE = "read_choice.php";
    private static final String CREATE_CHOICE = "create_choice.php";

    private String pickItID;
    private String filepath;

    public ChoiceCRUD(String pickItID){
        this.pickItID = pickItID;
    }

    public ChoiceCRUD(String pickItID,String filepath){
        this.pickItID = pickItID;
        this.filepath = filepath;
    }

    //TODO
    public String readExtension(){
    String extension = READ_CHOICE + "?PickItID=" +this.pickItID;
    return extension;
    }
    public String deleteExtension(){
        try {
            throw new Exception("a pickit ID cant delete a PickitID");
        }catch(Exception e){
        }
        return null;
    }
    public String updateExtension(){
        try {
            throw new Exception("a pickit can't update a Choice");
        }catch(Exception e){
        }
        return null;
    }
    public String createExtension(){
        String extension = CREATE_CHOICE + "?PickItID=" + this.pickItID + "&Filepath=" + this.filepath;
        return extension;
    }
}
