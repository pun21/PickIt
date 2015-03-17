package com.spun.pickit.model;

/**
 * Created by jacob_000 on 3/13/2015.
 */
public class User {
    private String username;
    private Demographics demo;
    private int id;

    public User(int id, String username, String birthday, String gender, String ethnicity, String religion, String politicalAffiliation){
        this.username = username;
        this.demo = new Demographics(birthday,gender,ethnicity,religion,politicalAffiliation);
        this.id = id;
    }

    public int getID(){return this.id;}
    public String getUsername(){return this.username;}
    public String getGender(){return this.demo.getGender();}
    public String getBirthday(){return this.demo.getBirthday();}
    public String getEthnicity(){return this.demo.getEthnicity();}
    public String getReligion(){return this.demo.getReligion();}
    public String getPoliticalAffiliation(){return this.demo.getPoliticalAffiliation();}
}
