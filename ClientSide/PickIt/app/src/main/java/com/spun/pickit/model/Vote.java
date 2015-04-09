package com.spun.pickit.model;

public class Vote {
    //region Class variables
    private int pickItID;
    private int choiceID;
    private int userID;
    private Demographics demo;
    //endregion

    //region Constructors
    public Vote(){
        demo = null;
    }

    public Vote(int pickItID, int choiceID, int userID, Demographics demo){
        this.pickItID = pickItID;
        this.choiceID = choiceID;
        this.userID = userID;
        this.demo = demo;
    }
    //endregion

    //region Accessor methods
    public int getPickItID(){
        return pickItID;
    }
    public void setPickItID(int pickItID){
        this.pickItID = pickItID;
    }
    public int getChoiceID(){
        return choiceID;
    }
    public void setChoiceID(int choiceID){
        this.choiceID = choiceID;
    }
    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }
    public Demographics getDemographics(){
        return demo;
    }
    public void setDemographics(Demographics demo){
        this.demo = demo;
    }
    //endregion

    //region Helper methods

    //endregion
}
