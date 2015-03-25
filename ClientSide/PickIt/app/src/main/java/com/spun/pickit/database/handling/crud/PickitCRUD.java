package com.spun.pickit.database.handling.crud;

public class PickItCRUD extends CRUD{
    private static final String CREATE_PICKIT = "create_pickit.php";
    private static final String READ_PICKIT = "read_pickit.php";
    private static final String DELETE_PICKIT = "delete_pickit.pnp";

    private int userId, pickItID;
    private String category, subject, timestamp, endTime;

    public PickItCRUD(int pickItId){
        this.pickItID = pickItId;
    }

    public PickItCRUD(int userID, String category, String subject, String timestamp, String endTime){
        this.userId = userID;
        this.category = category;
        this.subject = subject;
        this.timestamp = timestamp;
        this.endTime = endTime;
    }

    protected String createExtension(){
        try {
            throw new Exception("Invalid Operation: Can not update a PickIt");
        }catch(Exception e){}

        return null;
    }

    protected String readExtension(){
        String extension = READ_PICKIT + "?PickItID=" + pickItID;
        return extension;
    }

    protected String updateExtension(){
        try {
            throw new Exception("Invalid Operation: Can not update a PickIt");
        }catch(Exception e){}

        return null;
    }

    protected String deleteExtension(){
        String extension = DELETE_PICKIT + "?PickItID=" + pickItID;
        return extension;
    }
}
