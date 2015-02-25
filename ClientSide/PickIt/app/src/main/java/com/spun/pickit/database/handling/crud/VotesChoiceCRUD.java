package com.spun.pickit.database.handling.crud;
import org.json.JSONObject;

/**
 * Created by jacob_000 on 2/21/2015.
 */
public class VotesChoiceCRUD extends CRUD{
    private static final String READ_VOTES_CHOICE = "read_votes.php";

    private String choiceID;

    public VotesChoiceCRUD(String choiceID){
        this.choiceID = choiceID;
    }

    protected String readExtension(){
    String extension = READ_VOTES_CHOICE + "?ChoiceID=" + this.choiceID;
    return extension;
    }
    protected String deleteExtension(){
        try {
            throw new Exception("Cant delete a Votes for a ChoiceID or ever");
        }catch(Exception e){}
        return null;
    }
    protected String updateExtension(){
        try {
            throw new Exception("Cant update a Votes for a ChoiceID");
        }catch(Exception e){}
        return null;
    }
    protected String createExtension(){
        try {
            throw new Exception("Cant create a Votes for a ChoiceID");
        }catch(Exception e){}
        return null;
    }

    protected void readPrimitive(JSONObject json){}
    protected void deletePrimitive(JSONObject json){}
    protected void updatePrimitive(JSONObject json){}
    protected void createPrimitive(JSONObject json){}
}
