package com.spun.pickit.database.handling.crud;

public class PickItCRUD extends CRUD{
    private static final String CREATE_PICKIT = "create_pickit.php";
    private static final String READ_PICKIT = "read_pickit.php";
    private static final String DELETE_PICKIT = "delete_pickit.pnp";

    private int userID, pickItID, endTime;
    private String category, subject;

    public PickItCRUD(int userID, int endTime){
        this.userID = userID;
        this.endTime = endTime;
    }

    protected String createExtension(){
        String extension = CREATE_PICKIT + "?UserID=" + userID + "&EndTime=" + endTime;
        return extension;
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
