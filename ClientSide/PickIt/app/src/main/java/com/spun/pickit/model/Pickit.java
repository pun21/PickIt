package com.spun.pickit.model;

import java.util.ArrayList;


public class PickIt {
    //region Class Variables
    private int pickItID;
    private ArrayList<Choice> choices;
    private int userID;
    private String category;
    private String subjectHeader;
    private String timestamp;
    private String endTime;
    private int secondsOfLife;
    //endregion

    //region Constructors
    public PickIt(){
        choices = new ArrayList<>();
    }

    public PickIt(ArrayList<Choice> choices, int userID, String category, String subjectHeader, int secondsOfLife){
        this.choices = choices;
        this.userID = userID;
        this.category = category;
        this.subjectHeader = subjectHeader;
        this.secondsOfLife = secondsOfLife;
    }
    //endregion

    //region Accessor Methods
    public void setPickItID(int pickItID){
        this.pickItID = pickItID;
    }
    public int getPickItID(){
        return pickItID;
    }
    public ArrayList<Choice> getChoices(){
        return choices;
    }
    public int getUserID(){
        return userID;
    }
    public String getCategory(){
        return category;
    }
    public String getSubjectHeader(){
        return subjectHeader;
    }
    public String getTimestamp(){
        return timestamp;
    }
    public String getEndTime(){
        return endTime;
    }
    public int getSecondsOfLife() {
        return secondsOfLife;
    }
    public void setChoices(ArrayList<Choice> choices){
        this.choices = choices;
    }
    public void setUserID(int userId){
        this.userID = userId;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setSubjectHeader(String subjectHeader){
        this.subjectHeader = subjectHeader;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    public void setEndTime(String endTime){
        this.endTime = endTime;
    }
    //endregion

    public void addChoice(Choice choice){
        choices.add(choice);
    }
}
