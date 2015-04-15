package com.bcdevops.pickit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bcdevops.pickit.database.handling.DatabaseAccess;
import com.bcdevops.pickit.fileIO.ServerFileManager;
import com.bcdevops.pickit.model.Enums;
import com.bcdevops.pickit.model.PickIt;
import com.bcdevops.pickit.model.Vote;

import java.util.ArrayList;

public class MainActivity extends Activity {
    //region Class Variables
    private static final int MAX_NUMBER_GRID_ROWS = 150;
    private static ArrayList<PickIt> pickItList;

    private ArrayAdapter<CharSequence> mCategoriesAdapter;

    private PickItApp pickItApp;
    private Spinner mCategory;
    private ProgressBar loading;

    private Enums.Toggles mSortingType;
    private String category;
    //endregion

    //region Life-cycle methods
    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickItApp = (PickItApp)getApplication();
        loading = (ProgressBar)findViewById(R.id.loading);
        mCategory = (Spinner) findViewById(R.id.category_menu_main);
        category = "";
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                category = mCategory.getItemAtPosition(arg2).toString();

                if(category.equals("All categories"))
                    category = "";

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        populatePickItList(category);
                        populateListView();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        mSortingType = Enums.Toggles.MOST_RECENT;
        setUsername();
        populatePickItList("");
        populateListView();
        setToggles();
        setSpinners();

        endLoad();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    //endregion

    //region UI Handlers
    public void onClickUpload(View v) {
        Intent intent = new Intent(this, UploadActivity.class);
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

    public void onClickAppName(View v){
        //do nothing: we're on the main page
    }

    /*Handler methods for results pane toggles */

    public void onClickRecencyToggle(View v) {
        mSortingType = Enums.Toggles.MOST_RECENT;

        populatePickItList(category);
        populateListView();

        setToggles();
    }

    public void onClickTrendingToggle(View v) {
        mSortingType = Enums.Toggles.TRENDING;

        populatePickItList(category);
        populateListView();

        setToggles();
    }

    public void onClickTimeRemainingToggle(View v) {
        mSortingType = Enums.Toggles.EXPIRING;

        populatePickItList(category);
        populateListView();

        setToggles();
    }
    //endregion

    //region Helper Methods
    private void populatePickItList(String category) {
        startLoad();

        ServerFileManager sm = new ServerFileManager();
        pickItList = new ArrayList<>();

        switch (mSortingType){
            case TRENDING:
                pickItList = sm.downloadTrendingPickIts(MAX_NUMBER_GRID_ROWS);
                break;
            case MOST_RECENT:
                pickItList = sm.downloadMostRecentPickIts(MAX_NUMBER_GRID_ROWS);
                break;
            case EXPIRING:
                pickItList = sm.downloadExpiringPickIts(MAX_NUMBER_GRID_ROWS);
                break;
            default:
                try {
                    throw new Exception("Invalid sorting type");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        if(!category.equals("")){
            for(int a = 0; a < pickItList.size(); a++){
                if(!pickItList.get(a).getCategory().equals(category)){
                    pickItList.remove(a);
                    a--;
                }
            }
        }
    }
    private void populateListView() {
        CustomListAdapter adapter = new CustomListAdapter(this);
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        endLoad();
    }
    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);

        String tempUsername = pickItApp.getUsername();
        if(tempUsername.contains("Guest"))
            tempUsername = "Guest";

        username.setText(tempUsername);
    }
    private void setToggles(){
        ToggleButton recentlyAddedToggle = (ToggleButton) findViewById(R.id.toggle_recency);
        ToggleButton trendingToggle = (ToggleButton) findViewById(R.id.toggle_trending);
        ToggleButton expiringToggle = (ToggleButton) findViewById(R.id.toggle_time_remaining);

        switch(mSortingType){
            case MOST_RECENT:
                recentlyAddedToggle.setChecked(true);
                trendingToggle.setChecked(false);
                expiringToggle.setChecked(false);
                break;
            case TRENDING:
                recentlyAddedToggle.setChecked(false);
                trendingToggle.setChecked(true);
                expiringToggle.setChecked(false);
                break;
            case EXPIRING:
                recentlyAddedToggle.setChecked(false);
                trendingToggle.setChecked(false);
                expiringToggle.setChecked(true);
                break;
            default:
                break;
        }
    }
    public void startLoad(){
        setEnabled(false);
        loading.setVisibility(View.VISIBLE);
    }
    public void endLoad(){
        setEnabled(true);
        loading.setVisibility(View.INVISIBLE);
    }
    private void setEnabled(boolean enabled){
        TableLayout layout = (TableLayout) findViewById(R.id.main_activity_table);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if(child.getId() != R.id.loading)
                child.setEnabled(enabled);
        }
    }
    private void setSpinners() {
        mCategoriesAdapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        mCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(mCategoriesAdapter);
    }
    //endregion

    //Adapter
    private class CustomListAdapter extends ArrayAdapter<PickIt> {
        private MainActivity mainActivity;
        private TextView vHeading, vUsername, vCategory, vVotingTime;

        public CustomListAdapter(MainActivity mainActivity) {
            super(mainActivity.getApplicationContext(), R.layout.pickit_row, R.id.heading, pickItList);
            this.mainActivity = mainActivity;
        }

        @Override
        public View getView(final int position,View view,ViewGroup parent) {
            super.getView(position, view, parent);

            // Make sure we have a view to work with (may have been given null)
            View itemView = view;

            if (itemView == null) {
                itemView = mainActivity.getLayoutInflater().inflate(R.layout.pickit_row,
                        parent, false);
            }

            final ImageView image_tl= (ImageView) itemView.findViewById(R.id.image_tl);
            final ServerFileManager sm = new ServerFileManager();
            final PickIt pickIt = pickItList.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hasVotedOrIsUser(pickIt)){
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Globals.pickIt = pickIt;

                                Intent intent = new Intent(mainActivity, ResultsActivity.class);
                                startActivity(intent);
                            }
                        });
                    }else{
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Globals.pickIt = pickIt;

                                Intent intent = new Intent(mainActivity, VoteActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });

            // Fill the view
            vHeading = (TextView) itemView.findViewById(R.id.heading);
            vHeading.setText(pickIt.getSubjectHeader());

            vUsername = (TextView) itemView.findViewById(R.id.username);
            vUsername.setText(pickIt.getUsername());

            vUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mainActivity, ProfileActivity.class);
                            Globals.nextUserID = pickIt.getUserID();
                            Globals.nextUsername = pickIt.getUsername();

                            startActivity(intent);
                        }
                    });
                }
            });

            vCategory = (TextView) itemView.findViewById(R.id.category);
            vCategory.setText(pickIt.getCategory());

            vVotingTime = (TextView) itemView.findViewById(R.id.voting_time);
            vVotingTime.setText(pickIt.getLifeString());

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String filename = pickIt.getChoices().get(0).getFilename();
                    sm.downloadPicture(image_tl, filename);
                }
            });

            return itemView;
        }
    }

    private boolean hasVotedOrIsUser(PickIt pickIt){
        if(pickIt.getUserID() == pickItApp.getUserID())
            return true;

        DatabaseAccess access = new DatabaseAccess();
        ArrayList<Vote> votes = access.retrievePickItVotes(pickIt.getPickItID());

        for(int a = 0; a < votes.size(); a++){
            if(votes.get(a).getUserID() == pickItApp.getUserID())
                return true;
        }

        return false;
    }
}