package com.spun.pickit.database.handling.crud;


import org.json.JSONObject;

//Following is the sense that the following is the user only
// so the user has leaders, people he can issue and change
public class Following extends CRUD{
    protected static final String READ_LEADER = "read_following_get_leader.php";
    private static final String DELETE_FOLLOWING = "delete_following.php";
    private static final String CREATE_FOLLOWING = "create_following.php";

    private String follower;
    private String leader;

    public Following(String leader){
        this.follower = follower;
    }

    public Following(String follower, String leader){
        this.follower = follower;
        this.leader = leader;
    }
    //TODO
    public String readExtension(){
        String extension = READ_LEADER + "?UserID=" + this.follower;
        return extension;
    }
    //TODO
    public String deleteExtension(){
        String extension = (DELETE_FOLLOWING + "?Follower=" + this.follower + "&Leader="
                + this.leader);
        return extension;
    }
    public String updateExtension(){
        try {
            throw new Exception("Cant update a Following");
        }catch(Exception e){
        }
        return null;
    }
    public String createExtension(){
        String extension = CREATE_FOLLOWING + "?Follower=" + this.follower + "&Leader=" + this.leader;
        return extension;
    }
}
