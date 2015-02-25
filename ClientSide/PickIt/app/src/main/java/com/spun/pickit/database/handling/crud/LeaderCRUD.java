package com.spun.pickit.database.handling.crud;


import org.json.JSONObject;

//Leader in the since that the user is the Leader, the following are those people
// that follow the user
public class LeaderCRUD extends CRUD {
    private static final String READ_FOLLOWING = "read_following_get_following.php";
    private static final String DELETE_FOLLOWING = "delete_following.php";
    private static final String CREATE_FOLLOWING = "create_following.php";

    private String leader;

    public LeaderCRUD(String userID){
        this.leader = userID;
    }

    protected String readExtension(){
        String extension = READ_FOLLOWING + "?UserID=" + this.leader;
        return extension;
    }
    protected String deleteExtension(){
        try {
            throw new Exception("a user cant delete his following");
        }catch(Exception e){
        }
        return null;
    }
    protected String updateExtension(){
        try {
            throw new Exception("Cant update a Following");
        }catch(Exception e){
        }
        return null;
    }

    protected String createExtension(){
        try {
            throw new Exception("a Leader cant force people to follow him");
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    protected void readPrimitive(JSONObject json){}
    protected void deletePrimitive(JSONObject json){}
    protected void updatePrimitive(JSONObject json){}
    protected void createPrimitive(JSONObject json){}


}