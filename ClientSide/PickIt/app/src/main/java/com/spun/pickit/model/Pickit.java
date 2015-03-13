package com.spun.pickit.model;

/**
 * Created by jacob_000 on 3/13/2015.
 */
public class Pickit {
    // the invariant is that the Class will at least have 2 choices
    private final int LAYOUT2 = 2;
    private final int LAYOUT3 = 3;
    private final int LAYOUT4 = 4;


    private Choice choice0;
    private Choice choice1;
    private Choice choice2;
    private Choice choice3;

    private int layout;
    private int id;

    public Pickit(int id, Choice choice0, Choice choice1){
        this.id = id;
        this.choice0 = choice0;
        this.choice1 = choice1;
        this.layout = LAYOUT2;
    }

    public Pickit(int id,Choice choice0,Choice choice1,Choice choice2){
        this.id = id;
        this.choice0 = choice0;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.layout = LAYOUT3;
    }

    public Pickit(int id,Choice choice0,Choice choice1,Choice choice2,Choice choice3){
        this.id = id;
        this.choice0 = choice0;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.layout = LAYOUT4;
    }

    public int getID(){
        return this.id;
    }

    public Choice[] getChoices(){
        Choice[] choices = new Choice[this.layout];
        choices[0] = choice0;
        choices[1] = choice1;
        if (choice2 != null) choices[2] = choice2;
        if (choice3 != null) choices[3] = choice3;
        return choices;
    }
}
