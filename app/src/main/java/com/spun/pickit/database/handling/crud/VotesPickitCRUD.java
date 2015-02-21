package com.spun.pickit.database.handling.crud;

public class VotesPickitCRUD extends CRUD{
    private static final String READ_VOTES_PICKIT = "read_votes.php";

    private String pickitID;

    public VotesPickitCRUD(String pickitID){
        this.pickitID = pickitID;
    }

    protected String readExtension(){
        String extension = READ_VOTES_PICKIT + "?PickItID=" + this.pickitID;
        return extension;
    }
    protected String deleteExtension(){
        try {
            throw new Exception("Cant delete a Vote with a Pickit");
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
        try {
            throw new Exception("A Pickit can not create a Vote");
        }catch(Exception e){}
        return null;
    }

    protected void readPrimitive(){}
    protected void deletePrimitive(){}
    protected void updatePrimitive(){}
    protected void createPrimitive(){}



}
