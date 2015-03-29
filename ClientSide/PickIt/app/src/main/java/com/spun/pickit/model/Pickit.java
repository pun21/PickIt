package com.spun.pickit.model;

import java.util.ArrayList;


public class PickIt {
    //region Class Variables
    private int pickItID;
    private ArrayList<Choice> choices;
    private int userID;
    private String username;
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
    public PickIt(ArrayList<Choice> choices, int userID, String category, String subjectHeader, String endTime, String username){
        this.choices = choices;
        this.userID = userID;
        this.category = category;
        this.subjectHeader = subjectHeader;
        this.endTime = endTime;
        this.username = username;
    }

    //endregion8

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
    public String getUsername() { return username; }
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
    public void setUsername(String username) {this.username = username; }    //endregion

    public void addChoice(Choice choice){
        choices.add(choice);
    }
}
