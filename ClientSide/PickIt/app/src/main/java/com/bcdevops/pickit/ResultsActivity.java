package com.bcdevops.pickit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bcdevops.pickit.database.handling.DatabaseAccess;
import com.bcdevops.pickit.fileIO.ServerFileManager;
import com.bcdevops.pickit.model.Choice;
import com.bcdevops.pickit.model.PickIt;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultsActivity extends FragmentActivity {
    //region Class variables
    private static PickIt pickIt;

    private PickItApp pickItApp;
    private ServerFileManager sm;

    private TextView username;
    private TextView description;
    private TextView r0c0_textView;
    private TextView r0c1_textView;
    private TextView r1c0_textView;
    private TextView r1c1_textView;
    private ImageView r0c0;
    private ImageView r0c1;
    private ImageView r1c0;
    private ImageView r1c1;
    //endregion

    //region Activity life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_results);

        pickItApp = (PickItApp)getApplication();
        pickIt = Globals.pickIt;

        if(pickIt.getChoices().get(0).getChoiceID() == 0){
            ArrayList<PickIt> pickits = new ServerFileManager().downloadMostRecentPickIts(150);

            for(int a = 0; a < pickits.size(); a++){
                if(pickits.get(a).getPickItID() == pickIt.getPickItID()){
                    Globals.pickIt = pickits.get(a);
                    pickIt = Globals.pickIt;
                    break;
                }
            }
        }

        r0c0_textView = (TextView)findViewById(R.id.r0c0_text);
        r0c1_textView = (TextView)findViewById(R.id.r0c1_text);
        r1c0_textView = (TextView)findViewById(R.id.r1c0_text);
        r1c1_textView = (TextView)findViewById(R.id.r1c1_text);
        r0c0 = (ImageView)findViewById(R.id.row0column0);
        r0c1 = (ImageView)findViewById(R.id.row0column1);
        r1c0 = (ImageView)findViewById(R.id.row1column0);
        r1c1 = (ImageView)findViewById(R.id.row1column1);
        description = (TextView)findViewById(R.id.upload_description);

        DatabaseAccess access = new DatabaseAccess();
        pickIt.setVotes(access.retrievePickItVotes(pickIt.getPickItID()));

        sm = new ServerFileManager();
        getBitmaps();

        setFrontEnd();

        username = (TextView)findViewById(R.id.textView_username);

        String tempUsername = pickIt.getUsername();
        if(tempUsername.contains("Guest"))
            tempUsername = "Guest";

        username.setText(tempUsername+"'s Upload");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_results, menu);
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
    public void onClickUsername(View v) {
        //go to Profile Admin Activity
        Globals.nextUsername = pickItApp.getUsername();

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onClickChoice(View v) {
        int source = v.getId();

        switch (source) {
            case R.id.row0column0:
                Globals.choice = pickIt.getChoices().get(0);
                Globals.pickIt = pickIt;
                break;
            case R.id.row0column1:
                Globals.choice = pickIt.getChoices().get(1);
                Globals.pickIt = pickIt;
                break;
            case R.id.row1column0:
                Globals.choice = pickIt.getChoices().get(2);
                Globals.pickIt = pickIt;
                break;
            case R.id.row1column1:
                Globals.choice = pickIt.getChoices().get(3);
                Globals.pickIt = pickIt;
                break;
            default:
                Globals.choice = new Choice();
                Globals.pickIt = pickIt;
                break;
        }

        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
    }
    //endregion

    //region Helper methods
    private void getBitmaps(){
        for(int a = 0; a < pickIt.getChoices().size(); a++){
            ImageView view = new ImageView(this);
            sm.downloadPicture(view, pickIt.getChoices().get(a).getFilename());
        }
    }

    private void setFrontEnd(){
        //region Download images and set views
        ServerFileManager serverFileManager = new ServerFileManager();
        serverFileManager.downloadPicture(r0c0, pickIt.getChoices().get(0).getFilename());
        r0c0.setBackground(null);

        serverFileManager.downloadPicture(r0c1, pickIt.getChoices().get(1).getFilename());
        r0c1.setBackground(null);

        if(pickIt.getChoices().size() > 2){
            serverFileManager.downloadPicture(r1c0, pickIt.getChoices().get(2).getFilename());
            r1c0.setBackground(null);

            if(pickIt.getChoices().size() > 3){
                serverFileManager.downloadPicture(r1c1, pickIt.getChoices().get(3).getFilename());
                r1c1.setBackground(null);
            }else{
                r1c1.setVisibility(View.GONE);
            }
        }else{
            r1c0.setVisibility(View.GONE);
            r1c1.setVisibility(View.GONE);
        }
        //endregion

        //Description
        if(pickIt.getSubjectHeader().trim().length() == 0)
            description.setVisibility(View.GONE);
        else
            description.setText(pickIt.getSubjectHeader());

        if(pickIt.getVotes().size() != 0){
            // Choice 0
            int choice0Votes = getVotesForChoice(pickIt.getChoices().get(0).getChoiceID());
            double percent = choice0Votes / (double)pickIt.getVotes().size();
            double votePercentage = percent * 100;
            String votePercentageString = new DecimalFormat("#.##").format(votePercentage);
            String totalStatsString = choice0Votes + " of " + pickIt.getVotes().size() +  " votes\n" + votePercentageString + "%";
            r0c0_textView.setText(totalStatsString);

            // Choice 1
            int choice1Votes = getVotesForChoice(pickIt.getChoices().get(1).getChoiceID());
            percent = choice1Votes / (double)pickIt.getVotes().size();
            votePercentage = percent * 100;
            votePercentageString = new DecimalFormat("#.##").format(votePercentage);
            totalStatsString = choice1Votes + " of " + pickIt.getVotes().size() +  " votes\n" + votePercentageString + "%";
            r0c1_textView.setText(totalStatsString);

            // Choice 2
            if(pickIt.getChoices().size() > 2){
                int choice2Votes = getVotesForChoice(pickIt.getChoices().get(2).getChoiceID());
                percent = choice2Votes / (double)pickIt.getVotes().size();
                votePercentage = percent * 100;
                votePercentageString = new DecimalFormat("#.##").format(votePercentage);
                totalStatsString = choice2Votes + " of " + pickIt.getVotes().size() +  " votes\n" + votePercentageString + "%";
                r1c0_textView.setText(totalStatsString);
            }

            // Choice 3
            if(pickIt.getChoices().size() > 3){
                int choice3Votes = getVotesForChoice(pickIt.getChoices().get(3).getChoiceID());
                percent = choice3Votes / (double)pickIt.getVotes().size();
                votePercentage = percent * 100;
                votePercentageString = new DecimalFormat("#.##").format(votePercentage);
                totalStatsString = choice3Votes + " of " + pickIt.getVotes().size() +  " votes\n" + votePercentageString + "%";
                r1c1_textView.setText(totalStatsString);
            }
        }
    }

    private int getVotesForChoice(int choiceID){
        int sum = 0;
        for(int a = 0; a < pickIt.getVotes().size(); a++){
            if(pickIt.getVotes().get(a).getChoiceID() == choiceID)
                sum++;
        }

        return sum;
    }
    //endregion

}
