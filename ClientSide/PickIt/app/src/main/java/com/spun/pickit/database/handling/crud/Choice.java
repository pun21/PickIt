package com.spun.pickit.database.handling.crud;
import org.json.JSONObject;

public class Choice extends CRUD{
    private static final String READ_CHOICE = "read_choice.php";
    private static final String CREATE_CHOICE = "create_choice.php";

    private String pickItID;
    private String filepath;

    public Choice(String pickItID){
        this.pickItID = pickItID;
    }

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
