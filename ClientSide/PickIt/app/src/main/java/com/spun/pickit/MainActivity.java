package com.spun.pickit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.spun.pickit.fileIO.ServerFileManager;
import com.spun.pickit.model.PickIt;

import java.util.ArrayList;

public class MainActivity extends Activity {
    //region Class Variables
    private static final int MAX_NUMBER_GRID_ROWS = 10;

    private static final int TRENDING = 0;
    private static final int MOST_RECENT = 1;
    private static final int EXPIRING = 2;
    private int mSortingType;

    ArrayList<PickIt> pickItList;
    PickItApp pickItApp;

    ProgressBar loading;
    //endregion

    //region Life-cycle methods
    @Override
    protected void onStart(){
        super.onStart();
        startLoad();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickItApp = (PickItApp)getApplication();
        loading = (ProgressBar)findViewById(R.id.loading);

        setUsername();

        mSortingType = MOST_RECENT;

        populatePickItList();
        populateListView();
        setToggles();

        endLoad();
    }
    //endregion

    //region UI Handlers
    public void onClickUpload(View v) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

    public void onClickUsername(View v) {
        if(!pickItApp.isGuest()){
            //go to Profile Admin Activity
            Intent intent = new Intent(this, ProfileAdminActivity.class);
            startActivity(intent);
        }
    }

    public void onClickSignOut(View v) {

        pickItApp.resetUser();

        //go to login page after signing out
        Intent intent = new Intent(this, AppLoginActivity.class);
        startActivity(intent);
    }


    /*Handler methods for results pane toggles ---------------------------------------------------*/

    public void onClickRecencyToggle(View v) {
        mSortingType = MOST_RECENT;

        populatePickItList();
        populateListView();

        setToggles();
    }

    public void onClickTrendingToggle(View v) {
        mSortingType = TRENDING;

        populatePickItList();
        populateListView();

        setToggles();
    }

    public void onClickTimeRemainingToggle(View v) {
        mSortingType = EXPIRING;

        populatePickItList();
        populateListView();

        setToggles();
    }
    //endregion

    //region Helper Methods
    private void populatePickItList() {
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
    }
    private void populateListView() {
        CustomListAdapter adapter = new CustomListAdapter();
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }
    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);

        if(pickItApp.isGuest()){
            username.setEnabled(false);
        }

        username.setText(pickItApp.getUsername());
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
    //endregion

    //Adapter
    private class CustomListAdapter extends ArrayAdapter<PickIt> {

        private TextView vHeading, vUsername, vCategory, vVotingTime;

        public CustomListAdapter() {
            super(MainActivity.this, R.layout.pickit_row, R.id.heading, pickItList);
        }

        @Override
        public View getView(int position,View view,ViewGroup parent) {
            super.getView(position, view, parent);

            // Make sure we have a view to work with (may have been given null)
            View itemView = view;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.pickit_row,
                        parent, false);
            }

            final ImageView image_tl = (ImageView) itemView.findViewById(R.id.image_tl);
            final ServerFileManager sm = new ServerFileManager();
            final PickIt pickIt = pickItList.get(position);

            //Convert seconds to d h m s
            int time = pickIt.getSecondsOfLife();

            int seconds = time % 60;
            time = (time - seconds) / 60;

            int minutes = time % 60;
            time = (time - minutes) / 60;

            int hours = time % 24;
            time = (time - hours) / 24;

            int days = time;

            //Assign values
            String votingTimeLeft = days + "d " + hours + "h " + minutes + "m " + seconds + "s";

            // Fill the view
            vHeading = (TextView) itemView.findViewById(R.id.heading);
            vHeading.setText(pickIt.getSubjectHeader());

            vUsername = (TextView) itemView.findViewById(R.id.username);
            vUsername.setText(pickIt.getUsername());

            vCategory = (TextView) itemView.findViewById(R.id.category);
            vCategory.setText(pickIt.getCategory());

            vVotingTime = (TextView) itemView.findViewById(R.id.voting_time);
            vVotingTime.setText(votingTimeLeft);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String filename = pickIt.getChoices().get(0).getFilename();
                    sm.downloadPicture(image_tl, filename);
                    endLoad();
                }
            });

            return itemView;
        }
    }
}