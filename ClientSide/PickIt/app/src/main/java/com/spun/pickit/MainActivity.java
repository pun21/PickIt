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

import com.spun.pickit.model.Demographics;
import com.spun.pickit.model.PickIt;

import java.util.ArrayList;

public class MainActivity extends Activity {
    //region Class Variables
    PickItApp pickItApp;
    Demographics demo;
    //endregion
    private static final int MOST_RECENT = 10;
    private static final int LEAST_RECENT = 11;
    private static final int MOST_TRENDING = 20;
    private static final int LEAST_TRENDING = 21;
    private static final int LEAST_TIME_REMAINING = 30;
    private static final int MOST_TIME_REMAINING = 31;
    private int mSortingType = MOST_RECENT;

    ArrayList<PickIt> pickItList = new ArrayList<PickIt>();
    //region Life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickItApp = (PickItApp)getApplication();

        setUsername();
        
      //  populatePickItList(/*size*/);
       // populateListView();

    }
    //endregion

    private void populatePickItList(int size) {
        //size = get number of pickIts in database

        //for i = 0 to number of pickIts in the database
        for (int i = 0;i < size;i++) {
           // pickItList.add(/*nthPickIt*/);
        }
    }

    private void populateListView() {
        CustomListAdapter adapter = new CustomListAdapter();
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

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
        ToggleButton tog = (ToggleButton) findViewById(R.id.toggle_recency);
        //toggle button default is sorting by most recent
        if (!tog.isChecked()) {
            //sorting by most recent
            Toast.makeText(this, "Recency Off", Toast.LENGTH_SHORT).show();
        }
        else {
            //sorting by least recent
            Toast.makeText(this, "Recency On", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickTrendingToggle(View v) {
        ToggleButton tog = (ToggleButton) findViewById(R.id.toggle_trending);

        if (!tog.isChecked()) {
            //sorting by most trending
            Toast.makeText(this, "Trending Off", Toast.LENGTH_SHORT).show();
        }
        else {
            //sorting by least trending
            Toast.makeText(this, "Trending On", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickTimeRemainingToggle(View v) {
        ToggleButton tog = (ToggleButton) findViewById(R.id.toggle_time_remaining);

        if (!tog.isChecked()) {
            //sorting by least voting time remaining
            Toast.makeText(this, "Time Remaining Off", Toast.LENGTH_SHORT).show();
        }
        else {
            //sorting by most voting time remaining
            Toast.makeText(this, "Time Remaining On", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region Helper Methods
    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);

        if(pickItApp.isGuest()){
            username.setEnabled(false);
        }

        username.setText(pickItApp.getUsername());
    }
    //endregion
}