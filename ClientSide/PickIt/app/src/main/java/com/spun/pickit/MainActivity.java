package com.spun.pickit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.spun.pickit.fileIO.ServerFileManager;
import com.spun.pickit.model.Demographics;
import com.spun.pickit.model.PickIt;

import java.util.ArrayList;

public class MainActivity extends Activity {
    //region Class Variables
    private static final int MAX_NUMBER_GRID_ROWS = 10;
    private static final String TRENDING = "0";
    private static final String MOST_RECENT = "1";
    private static final String EXPIRING = "2";
    private String mSortingType;

    ArrayList<PickIt> pickItList;
    PickItApp pickItApp;
    //endregion

    //region Life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickItApp = (PickItApp)getApplication();

        setUsername();

        mSortingType = MOST_RECENT;
        
        populatePickItList();
       // populateListView();

        setToggles();
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

        //do any sign out stuff

        //go to login page after signing out
        Intent intent = new Intent(this, AppLoginActivity.class);
        startActivity(intent);
    }


    /*Handler methods for results pane toggles ---------------------------------------------------*/

    public void onClickRecencyToggle(View v) {
        mSortingType = MOST_RECENT;

        populatePickItList();
        // populateListView();

        setToggles();
    }

    public void onClickTrendingToggle(View v) {
        mSortingType = TRENDING;

        populatePickItList();
        // populateListView();

        setToggles();
    }

    public void onClickTimeRemainingToggle(View v) {
        mSortingType = EXPIRING;

        populatePickItList();
        // populateListView();

        setToggles();
    }
    //endregion

    //region Helper Methods
    private void populatePickItList() {
        //TODO - something to clear out the grid if it has components already in it

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
    //endregion

    //Adapter
    private class CustomListAdapter extends ArrayAdapter<PickIt> {

        private TextView vHeading, vUsername, vCategory, vVotingTime;
        private ImageView image_tl, image_tr, image_bl, image_br;

        public CustomListAdapter() {
            super(MainActivity.this, R.layout.pickit_row, pickItList);
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

            PickIt item = pickItList.get(position);
            String votingTimeLeft = "default";
            // Fill the view

            vHeading = (TextView) itemView.findViewById(R.id.heading);
            vHeading.setText(item.getSubjectHeader());

            vUsername = (TextView) itemView.findViewById(R.id.username);
            vUsername.setText(item.getUsername());

            vCategory = (TextView) itemView.findViewById(R.id.category);
            vCategory.setText(item.getCategory());

            vVotingTime = (TextView) itemView.findViewById(R.id.voting_time);
            /*do something with item.getEndTime() and item.getTimeStamp() to get the time remaining to vote*/
            vVotingTime.setText(votingTimeLeft);

            image_tl = (ImageView) itemView.findViewById(R.id.image_tl);
            image_tl.setImageBitmap(item.getChoices().get(0).getBitmap());

            image_tr = (ImageView) itemView.findViewById(R.id.image_tr);
            image_tr.setImageBitmap(item.getChoices().get(1).getBitmap());

            image_bl = (ImageView) itemView.findViewById(R.id.image_bl);
            image_bl.setImageBitmap(item.getChoices().get(2).getBitmap());

            image_br = (ImageView) itemView.findViewById(R.id.image_br);
            image_br.setImageBitmap(item.getChoices().get(3).getBitmap());

            return itemView;
        }
    }
}