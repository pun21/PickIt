package com.spun.pickit.database.handling.crud;

/**
 * Created by jacob_000 on 2/21/2015.
 */
public class PickitCRUD extends CRUD{
    private static final String READ_PICKIT = "read_pickit.php";
    private static final String CREATE_PICKIT = "create_pickit.php";
    private static final String DELETE_PICKIT = "delete_pickit.pnp";

    private String pickitID;

    public PickitCRUD(String pickitID){
        this.pickitID = pickitID;
    }

    protected String readExtension(){
    String extension = READ_PICKIT + "?UserID=" + this.pickitID;
    return extension;
    }

    protected String deleteExtension(){
        String extension = DELETE_PICKIT + "?PickItID=" + this.pickitID;
        return extension;
    }

    protected String updateExtension(){
        try {
            throw new Exception("Cant update a Pickit");
        }catch(Exception e){}
        return null;
    }

    protected String createExtension(){
        //TODO add the next addition to the method
        String extension = CREATE_PICKIT + "?PickItID=" + this.pickitID + "&ChoiceID0...";
        return extension;
    }

    protected void readPrimitive(){}
    protected void deletePrimitive(){}
    protected void updatePrimitive(){}
    protected void createPrimitive(){}
}
