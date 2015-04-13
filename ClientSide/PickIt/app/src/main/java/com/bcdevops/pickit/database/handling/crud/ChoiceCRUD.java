package com.bcdevops.pickit.database.handling.crud;
import org.json.JSONObject;

public class ChoiceCRUD extends CRUD{
    private static final String READ_CHOICE = "PickIt/php/read_choice.php";
    private static final String CREATE_CHOICE = "PickIt/php/create_choice.php";

    private int pickItID;
    private String slot;

    public ChoiceCRUD(int pickItID){
        this.pickItID = pickItID;
    }

    public ChoiceCRUD(int pickItID, String slot){
        this.pickItID = pickItID;
        this.slot = slot;
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
        String extension = CREATE_CHOICE + "?PickItID=" + this.pickItID + "&Slot=" + this.slot;
        return extension;
    }
}
