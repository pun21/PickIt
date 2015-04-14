package com.bcdevops.pickit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcdevops.pickit.fileIO.ServerFileManager;
import com.bcdevops.pickit.model.Choice;
import com.bcdevops.pickit.model.Demographics;
import com.bcdevops.pickit.model.PickIt;
import com.bcdevops.pickit.model.Vote;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ChoiceActivity extends Activity {
    //region Class variables
    private ArrayList<Vote> votes;
    private PickItApp pickItApp;
    private Choice choice;
    private PickIt pickIt;

    private ImageView image;
    private TextView total_stats;
    private TextView gender_stats;
    private TextView ethnicity_stats;
    private TextView political_stats;
    private TextView religion_stats;

    //endregion

    //region Activity life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        pickItApp = (PickItApp)getApplication();

        choice = Globals.choice;
        pickIt = Globals.pickIt;
        votes = pickIt.getVotes();
        votes = getVotesForChoiceID(choice.getChoiceID());

        image = (ImageView)findViewById(R.id.choice_imageView);
        total_stats = (TextView)findViewById(R.id.total_vote_stats);
        gender_stats = (TextView)findViewById(R.id.gender_stats);
        ethnicity_stats = (TextView)findViewById(R.id.ethnicity_stats);
        political_stats = (TextView)findViewById(R.id.political_stats);
        religion_stats = (TextView)findViewById(R.id.religion_stats);

        setUsername();
        setChoiceImage();
        setStatistics();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region UI handlers
    public void onClickAppName(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onClickUsername(View v) {
        // When we go to the ProfileActivity,  set the nextUserName as
        // the profile page that we are intending to view
        if(pickItApp.isGuest())
            return;

        Globals.nextUsername = pickItApp.getUsername();

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    //endregion

    //region Helper methods
    private ArrayList<Vote> getVotesForChoiceID(int choiceID){
        ArrayList<Vote> votesTemp = new ArrayList<>();
        for(int a = 0; a < votes.size(); a++){
            if(votes.get(a).getChoiceID() == choiceID)
                votesTemp.add(votes.get(a));
        }

        return votesTemp;
    }
    private void setChoiceImage(){
        new ServerFileManager().downloadPicture(image, choice.getFilename());
    }
    private void setStatistics(){
        setTotalStatsView();

        setGenderStatsView();
        setEthnicityStatsView();
        setPoliticalStatsView();
        setReligionStatsView();
    }
    private void setGenderStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i< votes.size();i++){
            Demographics demo = votes.get(i).getDemographics();
            values.add(demo.getGender());
        }

        String statsString = "Gender";
        statsString += getStatsForDemo(values);
        gender_stats.setText(statsString);
    }
    private void setEthnicityStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < votes.size();i++){
            Demographics demo = votes.get(i).getDemographics();
            values.add(demo.getEthnicity());
        }

        String statsString = "Ethnicity";
        statsString += getStatsForDemo(values);
        ethnicity_stats.setText(statsString);
    }
    private void setPoliticalStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i< votes.size();i++){
            Demographics demo = votes.get(i).getDemographics();
            values.add(demo.getPoliticalAffiliation());
        }

        String statsString = "Political";
        statsString += getStatsForDemo(values);
        political_stats.setText(statsString);
    }
    private void setReligionStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i< votes.size();i++){
            Demographics demo = votes.get(i).getDemographics();
            values.add(demo.getReligion());
        }

        String statsString = "Religion";
        statsString += getStatsForDemo(values);
        religion_stats.setText(statsString);
    }
    private String getStatsForDemo(ArrayList<String> values){
        ArrayList<Pair<String, Integer>> pairs = new ArrayList<>();

        if(values.size() > 0){
            Collections.sort(values);

            if ((values.size() > 0)) {
                String firstValue = values.get(0);
                int count = 0;
                for (int i = 0; i < values.size(); i++) {
                    if (firstValue.equals(values.get(i))) {
                        count += 1;
                    }
                    else {
                        pairs.add(new Pair(firstValue, count));
                        firstValue = values.get(i);
                        count = 1;
                    }
                }

                pairs.add(new Pair(firstValue, count));

                String demoString = "";
                for (int i = 0; i < pairs.size();i++){
                    String demo = pairs.get(i).first;
                    Integer votes = pairs.get(i).second;
                    double percentage = (double) votes / (double)values.size();
                    String votePercentage = String.valueOf(percentage*100);
                    votePercentage = votePercentage.length() > 4 ? votePercentage.substring(0, 5) : votePercentage.substring(0, votePercentage.length());
                    demoString += "\n" + demo + ": " + votePercentage + "%";
                }
                return demoString;
            }
        }

        return "\nNo votes.";
    }
    private void setTotalStatsView(){
        if(votes.size() == 0){
            String totalStatsString = "\nTotal Votes: 0, 0.00%";
            total_stats.setText(totalStatsString);
            return;
        }

        double percent = (double)getVotesForChoice(choice.getChoiceID()) / (double)votes.size();
        String votePercentage = String.valueOf(percent * 100);
        votePercentage = votePercentage.length() > 4 ? votePercentage.substring(0, 4) : votePercentage.substring(0, votePercentage.length());
        String totalStatsString = "\nTotal Votes: " + getVotesForChoice(choice.getChoiceID()) +  ", " + votePercentage + "%";
        total_stats.setText(totalStatsString);
    }
    private int getVotesForChoice(int choiceID){
        int sum = 0;
        for(int a = 0; a < votes.size(); a++){
            if(pickIt.getVotes().get(a).getChoiceID() == choiceID)
                sum++;
        }

        return sum;
    }
    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);

        if(pickItApp.isGuest())
            pickItApp.setUsername("Guest");

        username.setText(pickItApp.getUsername());
    }
    //endregion
}
