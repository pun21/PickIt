package com.spun.pickit.database.handling.crud;


//Following is the sense that the following is the user only
// so the user has leaders, people he can issue and change
public class FollowingCRUD extends CRUD{
    protected static final String READ_LEADER = "read_following_get_leader.php";
    private static final String DELETE_FOLLOWING = "delete_following.php";
    private static final String CREATE_FOLLOWING = "create_following.php";

    private String follower;
    private String leader;

    public FollowingCRUD(String leader){
        this.follower = follower;
    }

    //returns the leaders of the user
    protected String readExtension(){
        String extension = READ_LEADER + "?UserID=" + this.follower;
        return extension;
    }

    //deletes this connection
    protected String deleteExtension(){
        String extension = (DELETE_FOLLOWING + "?Follower=" + this.follower + "&Leader="
                + this.leader);
        return extension;
    }
    protected String updateExtension(){
        try {
            throw new Exception("Cant update a Following");
        }catch(Exception e){
        }
        return null;
    }
    protected String createExtension(){
        String extension = CREATE_FOLLOWING + "?Follower=" + this.follower + "&Leader=" + this.leader;
        return extension;
    }

    protected void readPrimitive(){}
    protected void deletePrimitive(){}
    protected void updatePrimitive(){}
    protected void createPrimitive(){}



}
