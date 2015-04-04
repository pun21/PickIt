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
import com.spun.pickit.model.PickIt;

import java.util.ArrayList;


public class ProfileActivity extends Activity {
    private static final int USER_NAME = 78445;
    private static final int VOTED_ON = 4588;
    //region Class Variables
    private static final int MAX_NUMBER_GRID_ROWS = 10;

    private int viewSortingType = USER_NAME;

    PickItApp pickItApp;
    private static ArrayList<PickIt> pickItList;
    private ProgressBar loading;
    //endregion

    //region Activity Life-cycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        pickItApp = (PickItApp)getApplication();
        loading = (ProgressBar)findViewById(R.id.loading);

        setUsername();
        populatePickItList();
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
    //endregion

    //region Input Handlers
    public void onClickNavHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickUsername(View v) {
        boolean nextUserEqualsThisUser = pickItApp.getUsername() == pickItApp.getNextUsername();
        boolean nextUserNotEqualToThisUser = !nextUserEqualsThisUser;

        // Highest Priority:  if the profile is NextUsers, then setNextUser
        // If it is the users Profile Page, then set the UsersProfile page
        // else if it is a guest dont enable the thing

        if (nextUserNotEqualToThisUser){
            // does Nothing,  you cant not click it
        }
        else if (nextUserEqualsThisUser){
            // if the user is you, then do something
            Intent intent = new Intent(this, AccountAdminActivity.class);
            startActivity(intent);
        }
        else if(pickItApp.isGuest()){
            // if its a Guest do nothing
        }
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

    public void onClickUploaded(View v) {

    }
    //endregion


    private void setToggles(){
        ToggleButton usernameToggle = (ToggleButton) findViewById(R.id.toggle_username);
        ToggleButton votedOnToggle = (ToggleButton) findViewById(R.id.toggle_voted_on);

        switch(viewSortingType){
            case USER_NAME:
                usernameToggle.setChecked(true);
                votedOnToggle.setChecked(false);
                break;
            case VOTED_ON:
                usernameToggle.setChecked(false);
                votedOnToggle.setChecked(true);
                break;
            default:
                break;
        }
    }




    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);
        boolean nextUserEqualsThisUser = pickItApp.getUsername() == pickItApp.getNextUsername();
        boolean nextUserNotEqualToThisUser = !nextUserEqualsThisUser;

           // Highest Priority:  if the profile is NextUsers, then setNextUser
           // If it is the users Profile Page, then set the UsersProfile page
           // else if it is a guest dont enable the thing


        if (nextUserNotEqualToThisUser){
            username.setEnabled(true);
            username.setText(pickItApp.getNextUsername());
        }
        else if (nextUserEqualsThisUser){
            username.setEnabled(true);
            username.setText(pickItApp.getUsername());
        }
        else if(pickItApp.isGuest()){
            username.setEnabled(false);
            username.setText(pickItApp.getNextUserID());
        }
    }

    private void populatePickItList() {
        startLoad();

        ServerFileManager sm = new ServerFileManager();
        pickItList = new ArrayList<>();
        pickItList = sm.downloadMostRecentPickIts(MAX_NUMBER_GRID_ROWS);
        try {
            throw new Exception("Invalid sorting type");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void setEnabled(boolean enabled){
        TableLayout layout = (TableLayout) findViewById(R.id.profile_activity_table);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if(child.getId() != R.id.loading)
                child.setEnabled(enabled);
        }
    }

    public void endLoad(){
        setEnabled(true);
        loading.setVisibility(View.INVISIBLE);
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

//            ImageView temp = (ImageView) itemView.findViewById(R.id.image_tl);
//            temp.setId(ID_ADDITIVE+temp.getId());

            final ImageView image_tl= (ImageView) itemView.findViewById(R.id.image_tl);
            final ServerFileManager sm = new ServerFileManager();
            final PickIt pickIt = pickItList.get(position);

            // Fill the view
            vHeading = (TextView) itemView.findViewById(R.id.heading);
            vHeading.setText(pickIt.getSubjectHeader());
//                vHeading.setId(ID_ADDITIVE+1000+pickIt.getPickItID());

//            vUsername = (TextView) itemView.findViewById(R.id.username);
//            vUsername.setText(pickIt.getUsername());
//            vUsername.setId(ID_ADDITIVE+2000+pickIt.getPickItID());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(profileActivity, ResultsActivity.class);
                            pickItApp.setResultPickItID(pickItList.get(position).getPickItID());

                            startActivity(intent);
                        }
                    });
                }
            });

            vCategory = (TextView) itemView.findViewById(R.id.category);
            vCategory.setText(pickIt.getCategory());
//                vCategory.setId(ID_ADDITIVE+3000+pickIt.getPickItID());

            vVotingTime = (TextView) itemView.findViewById(R.id.voting_time);
//                vVotingTime.setId(ID_ADDITIVE+4000+pickIt.getPickItID());

            vVotingTime.setText(pickIt.getLifeString());

            //endregion

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