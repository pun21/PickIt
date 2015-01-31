package com.example.bjclark.pickit;

import java.util.Date;

public class Demographics
{
    private Date birthday;
    private String gender;
    private String ethnicity;
    private String religion;
    private String politicalAffiliation;

    public Demographics()
    {
        birthday = new Date();
        gender = "";
        ethnicity = "";
        religion = "";
        politicalAffiliation = "";
    }
    public Demographics(Date birthday, String gender, String ethnicity,
                        String religion, String politicalAffiliation)
    {
        this.birthday = birthday;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.religion = religion;
        this.politicalAffiliation = politicalAffiliation;
    }

    public Date getBirthday(){
        return birthday;
    }
    public String getGender(){
        return gender;
    }
    public String getEthnicity(){
        return ethnicity;
    }
    public String getReligion(){
        return religion;
    }
    public String getPoliticalAffiliation(){
        return politicalAffiliation;
    }
}
