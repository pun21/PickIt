package com.spun.pickit.database.handling.crud;

public class VotesUserCRUD extends CRUD{
    private static final String READ_VOTES_USER = "read_votes.php";
    private static final String CREATE_VOTES_USER = "create_vote.php";

    private String choiceID;
    private String pickitID;
    private String userID;

    public VotesUserCRUD(String userID){
        this.userID = userID;
    }

    public VotesUserCRUD(String userID,String choiceID, String pickitID){
        this.userID = userID;
        this.choiceID = choiceID;
        this.pickitID = pickitID;
    }

    //reads the votes from a particular user
    protected String readExtension(){
        String extension = READ_VOTES_USER + "?UserID=" + this.userID;
        return extension;
    }
    protected String deleteExtension(){
        try {
            throw new Exception("Cant delete a Vote");
        }catch(Exception e){}
        return null;
    }
    protected String updateExtension(){
        try {
            throw new Exception("Cant update a Votes for a Pickit");
        }catch(Exception e){}
        return null;
    }
    protected String createExtension(){
        String extension = (CREATE_VOTES_USER + "?UserID=" + this.userID + "&PickitID=" + this.pickitID
                + "&ChoiceID=" + this.choiceID);
        return extension;
    }

    protected void readPrimitive(){}
    protected void deletePrimitive(){}
    protected void updatePrimitive(){}
    protected void createPrimitive(){}




}
