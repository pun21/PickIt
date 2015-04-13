package com.bcdevops.pickit.model;

import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class PickIt implements Serializable {
    //region Class Variables
    private AsyncTimer timer;
    private ArrayList<Choice> choices;
    private ArrayList<Vote> votes;
    private int userID;
    private int pickItID;
    private int secondsOfLife;
    private String username;
    private String category;
    private String subjectHeader;
    //endregion

    //region Constructors
    public PickIt(){
        choices = new ArrayList<>();
    }

    public PickIt(int pickItID, int userID, String username, String category, String subjectHeader, int secondsOfLife, ArrayList<Choice> choices){
        this.userID = userID;
        this.username = username;
        this.choices = choices;
        this.pickItID = pickItID;
        this.category = category;
        this.subjectHeader = subjectHeader;
        this.secondsOfLife = secondsOfLife;

        startTimer();
    }

    public PickIt(ArrayList<Choice> choices, int userID, String category, String subjectHeader, int secondsOfLife){
        this.choices = choices;
        this.userID = userID;
        this.category = category;
        this.subjectHeader = subjectHeader;
        this.secondsOfLife = secondsOfLife;
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
    public String getUsername() {
        return username;
    }
    public String getLifeString(){
        int seconds = secondsOfLife % 60;
        secondsOfLife = (secondsOfLife - seconds) / 60;

        int minutes = secondsOfLife % 60;
        secondsOfLife = (secondsOfLife - minutes) / 60;

        int hours = secondsOfLife % 24;

        String dateStructuredText = "Expired: Legacy view available";
        if(hours != 0 || minutes != 0 || seconds != 0){
            dateStructuredText = hours + " h " + minutes + " m " + seconds + " s";
        }

        return dateStructuredText;
    }
    public int getSecondsOfLife() {
        return secondsOfLife;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setUsername(String username) {this.username = username; }
    public void setSecondsOfLife(int secondsOfLife){
        this.secondsOfLife = secondsOfLife;

        if(!timerIsRunning()){
            startTimer();
        }
    }
    public ArrayList<Vote> getVotes(){
        if(votes == null)
            return new ArrayList<>();

        return votes;
    }
    public void setVotes(ArrayList<Vote> votes){
        this.votes = votes;
    }

    //region ...Timer
    private void startTimer(){
        if(timer == null){
            timer = new AsyncTimer(this);

            timer.setTimer(secondsOfLife);
        }
    }
    public void stopTimer(){
        if(timer != null)
            timer.stopTimer();
    }
    private boolean timerIsRunning(){
        if(timer != null)
            return true;

        return false;
    }
    //endregion

    class AsyncTimer{
        private PickIt pickIt;
        private CountDownTimer timer;

        public AsyncTimer(PickIt pickIt) {
            this.pickIt = pickIt;
            timer = null;
        }

        public void setTimer(final int time) {
            if(timer != null){
                return;
            }

            timer = new CountDownTimer(time*1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    pickIt.setSecondsOfLife((int)(millisUntilFinished/1000));
                }
                public void onFinish() {
                    pickIt.setSecondsOfLife(0);
                }
            }.start();
        }

        public void stopTimer(){
            if(timer != null)
                timer.cancel();
        }
    }
}
