package com.spun.pickit.database.handling.crud;

public class ChoiceCRUD extends CRUD{
    private static final String READ_CHOICE = "read_choice.php";
    private static final String CREATE_CHOICE = "create_choice.php";

    private String pickItID;
    private String filepath;

    public ChoiceCRUD(String pickItID){
        this.pickItID = pickItID;
    }

    protected String readExtension(){
    String extension = READ_CHOICE + "?PickItID=" +this.pickItID;
    return extension;
    }
    protected String deleteExtension(){
        try {
            throw new Exception("a pickit ID cant delete a PickitID");
        }catch(Exception e){
        }
        return null;
    }
    protected String updateExtension(){
        try {
            throw new Exception("a pickit cant update a Choice");
        }catch(Exception e){
        }
        return null;
    }
    protected String createExtension(){
        String extension = CREATE_CHOICE + "?PickItID=" + this.pickItID + "&Filepath=" + this.filepath;
        return extension;
    }

    protected void readPrimitive(){}
    protected void deletePrimitive(){}
    protected void updatePrimitive(){}
    protected void createPrimitive(){}

}
