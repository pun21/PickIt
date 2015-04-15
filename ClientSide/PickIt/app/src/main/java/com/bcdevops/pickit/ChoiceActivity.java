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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ChoiceActivity extends Activity {
    //region Class variables
    private ArrayList<Vote> pickItVotes;
    private ArrayList<Vote> choiceVotes;
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
        pickItVotes = pickIt.getVotes();
        choiceVotes = getVotesForChoiceID(choice.getChoiceID());

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
        TextView username = (TextView)findViewById(R.id.textView_username);
        Globals.nextUsername = username.getText().toString();

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    //endregion

    //region Helper methods
    private ArrayList<Vote> getVotesForChoiceID(int choiceID){
        ArrayList<Vote> votesTemp = new ArrayList<>();
        for(int a = 0; a < pickItVotes.size(); a++){
            if(pickItVotes.get(a).getChoiceID() == choiceID)
                votesTemp.add(pickItVotes.get(a));
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
        for (int i = 0; i< choiceVotes.size();i++){
            Demographics demo = choiceVotes.get(i).getDemographics();
            values.add(demo.getGender());
        }

        String statsString = "Gender";
        statsString += getStatsForDemo(values);
        gender_stats.setText(statsString);
    }
    private void setEthnicityStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < choiceVotes.size();i++){
            Demographics demo = choiceVotes.get(i).getDemographics();
            values.add(demo.getEthnicity());
        }

        String statsString = "Ethnicity";
        statsString += getStatsForDemo(values);
        ethnicity_stats.setText(statsString);
    }
    private void setPoliticalStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i< choiceVotes.size();i++){
            Demographics demo = choiceVotes.get(i).getDemographics();
            values.add(demo.getPoliticalAffiliation());
        }

        String statsString = "Political";
        statsString += getStatsForDemo(values);
        political_stats.setText(statsString);
    }
    private void setReligionStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i< choiceVotes.size();i++){
            Demographics demo = choiceVotes.get(i).getDemographics();
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
                    double votePercentage = percentage*100;
                    String votePercentageString = new DecimalFormat("#.##").format(votePercentage);
                    demoString += "\n" + demo + ": " + votePercentageString + "%";
                }
                return demoString;
            }
        }

        return "\nNo votes.";
    }
    private void setTotalStatsView(){
        if(pickItVotes.size() == 0){
            String totalStatsString = "No votes yet";
            total_stats.setText(totalStatsString);
            return;
        }

        double percent = (double)choiceVotes.size() / (double)pickItVotes.size();
        double votePercentage = percent * 100;
        String votePercentageString = new DecimalFormat("#.##").format(votePercentage);
        String totalStatsString = getVotesForChoice(choice.getChoiceID()) + " of " + pickItVotes.size() +  " votes,  " + votePercentageString + "%";
        total_stats.setText(totalStatsString);
    }
    private int getVotesForChoice(int choiceID){
        int sum = 0;
        for(int a = 0; a < pickItVotes.size(); a++){
            if(pickIt.getVotes().get(a).getChoiceID() == choiceID)
                sum++;
        }

        return sum;
    }
    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);

        String tempUsername = pickIt.getUsername();

        tempUsername = tempUsername.contains("Guest") ? "Guest" : tempUsername;
        username.setText(tempUsername);

        if(tempUsername.equals("Guest"))
            username.setEnabled(false);
    }
    //endregion
}
