package com.spun.pickit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.spun.pickit.model.Enums;
import com.spun.pickit.model.PickIt;

import java.util.ArrayList;

public class ProfileActivity extends Activity {
    //region Class Variables
    private PickItApp pickItApp;
    private static ArrayList<PickIt> pickItList;
    private ProgressBar loading;
    private TextView username;

    private Enums.Toggles viewSortingType;
    //endregion

    //region Activity Life-cycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        pickItApp = (PickItApp)getApplication();
        loading = (ProgressBar)findViewById(R.id.loading);
        username = (TextView)findViewById(R.id.username);

        username.setText(Globals.nextUsername);

        viewSortingType  = Enums.Toggles.UPLOADED;

        setEditProfile();
        populateUploadedPickItList();
        populateListView();
        setToggles();
        endLoad();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_admin, menu);
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

    @Override
    protected void onStop(){
        for(int a = 0; a < pickItList.size(); a++){
            pickItList.get(a).stopTimer();
        }

        super.onStop();
    }
    //endregion

    //region Input Handlers
    public void onClickNavHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickUsername(View v) {
            Intent intent = new Intent(this, AccountAdminActivity.class);
            startActivity(intent);
    }

    public void onClickNavUpload(View v) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

    public void onClickSignOut(View v) {
        pickItApp.resetUser();

        //go to login page after signing out
        Intent intent = new Intent(this, AppLoginActivity.class);
        startActivity(intent);
    }

    public void onClickUploadedToggle(View v) {
        viewSortingType = Enums.Toggles.UPLOADED;

        populateUploadedPickItList();
        populateListView();

        setToggles();
    }

    public void onClickRecentActivityToggle(View v) {
        viewSortingType = Enums.Toggles.RECENT_ACTIVITY;

        populateRecentActivityPickItList();
        populateListView();

        setToggles();
    }
    //endregion


    private void setToggles(){
        ToggleButton uploadedToggle = (ToggleButton) findViewById(R.id.toggle_uploaded);
        ToggleButton votedOnToggle = (ToggleButton) findViewById(R.id.toggle_voted_on);

        switch(viewSortingType){
            case UPLOADED:
                uploadedToggle.setChecked(true);
                votedOnToggle.setChecked(false);
                break;
            case RECENT_ACTIVITY:
                uploadedToggle.setChecked(false);
                votedOnToggle.setChecked(true);
                break;
            default:
                break;
        }
    }

    private void setEditProfile(){
        boolean nextUserIsThisUser = pickItApp.getUsername().equals(Globals.nextUsername);

        if (nextUserIsThisUser){
            TextView editProfile = (TextView)findViewById(R.id.textView_EditProfile);
            editProfile.setEnabled(true);
            editProfile.setVisibility(View.VISIBLE);
        }
    }

    private void populateUploadedPickItList() {
        startLoad();

        ServerFileManager sm = new ServerFileManager();
        pickItList = sm.getUploadedPickIts(Globals.nextUsername);
    }

    private void populateRecentActivityPickItList() {
        startLoad();

        ServerFileManager sm = new ServerFileManager();
        pickItList = sm.getRecentActivityPickIts(Globals.nextUsername);
    }

    private void populateListView() {
        CustomListAdapter adapter = new CustomListAdapter(this);
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        endLoad();
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
        TableLayout layout = (TableLayout) findViewById(R.id.profile_activity_table);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if(child.getId() != R.id.loading)
                child.setEnabled(enabled);
        }
    }

    //Adapter
    private class CustomListAdapter extends ArrayAdapter<PickIt> {
        private ProfileActivity profileActivity;
        private TextView vHeading, vUsername, vCategory, vVotingTime;

        public CustomListAdapter(ProfileActivity profileActivity) {
            super(profileActivity.getApplicationContext(), R.layout.pickit_row, R.id.heading, pickItList);
            this.profileActivity = profileActivity;
        }

        @Override
        public View getView(final int position,View view,ViewGroup parent) {
            super.getView(position, view, parent);

            // Make sure we have a view to work with (may have been given null)
            View itemView = view;
            if (itemView == null) {
                itemView = profileActivity.getLayoutInflater().inflate(R.layout.pickit_row,
                        parent, false);
            }

            final ImageView image_tl= (ImageView) itemView.findViewById(R.id.image_tl);
            final ServerFileManager sm = new ServerFileManager();
            final PickIt pickIt = pickItList.get(position);

            // Fill the view
            vHeading = (TextView) itemView.findViewById(R.id.heading);
            vHeading.setText(pickIt.getSubjectHeader());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(profileActivity, Voting_ResultsActivity.class);

                            Globals.pickIt = pickIt;

                            startActivity(intent);
                        }
                    });
                }
            });

            vCategory = (TextView) itemView.findViewById(R.id.category);
            vCategory.setText(pickIt.getCategory());

            vVotingTime = (TextView) itemView.findViewById(R.id.voting_time);
            vVotingTime.setText(pickIt.getLifeString());

            profileActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String filename = pickIt.getChoices().get(0).getFilename();
                    sm.downloadPicture(image_tl, filename);
                }
            });

            return itemView;
        }
    }
}