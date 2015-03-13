package com.spun.pickit.model;

/**
 * Created by jacob_000 on 3/13/2015.
 */
public class Demographics {
    private String birthday;
    private String gender;
    private String ethnicity;
    private String religion;
    private String politicalAffiliation;

    public Demographics(String birthday, String gender, String ethnicity, String religion, String politicalAffiliation){
        this.birthday = birthday;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.politicalAffiliation = politicalAffiliation;
    }
    public String getGender(){return this.gender;}
    public String getBirthday(){return this.birthday;}
    public String getEthnicity(){return this.ethnicity;}
    public String getReligion(){return this.religion;}
    public String getPoliticalAffiliation(){return this.politicalAffiliation;}
}
