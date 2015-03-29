package com.spun.pickit.model;

import java.util.ArrayList;


public class PickIt {
    //region Class Variables
    private ArrayList<Choice> choices;
    private int userID;
    private String category;
    private String subjectHeader;
    private String timestamp;
    private String endTime;
    //endregion

    //region Constructors
    public PickIt(){
        choices = new ArrayList<>();
    }

    public PickIt(ArrayList<Choice> choices, int userID, String category, String subjectHeader, String endTime){
        this.choices = choices;
        this.userID = userID;
        this.category = category;
        this.subjectHeader = subjectHeader;
        this.endTime = endTime;
    }
    //endregion

    //region Accessor Methods
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
