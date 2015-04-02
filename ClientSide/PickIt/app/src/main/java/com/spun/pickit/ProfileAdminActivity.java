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

import com.spun.pickit.fileIO.ServerFileManager;
import com.spun.pickit.model.PickIt;

import java.util.ArrayList;


public class ProfileAdminActivity extends Activity {
    //region Class Variables
    private static final int MAX_NUMBER_GRID_ROWS = 10;

    PickItApp pickItApp;
    private static ArrayList<PickIt> pickItList;
    private ProgressBar loading;
    //endregion

    //region Activity Life-cycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_admin);



        pickItApp = (PickItApp)getApplication();
        loading = (ProgressBar)findViewById(R.id.loading);


        populatePickItList();
        populateListView();

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

    public void onClickUploaded(View v) {

    }
    //endregion

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
        TableLayout layout = (TableLayout) findViewById(R.id.main_activity_table);
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
        private ProfileAdminActivity mainActivity;
        private TextView vHeading, vUsername, vCategory, vVotingTime;

        public CustomListAdapter(ProfileAdminActivity mainActivity) {
            super(mainActivity.getApplicationContext(), R.layout.pickit_row, R.id.heading, pickItList);
            this.mainActivity = mainActivity;
        }

        @Override
        public View getView(int position,View view,ViewGroup parent) {
            super.getView(position, view, parent);

            // Make sure we have a view to work with (may have been given null)
            View itemView = view;
            if (itemView == null) {
                itemView = mainActivity.getLayoutInflater().inflate(R.layout.pickit_row,
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

            vUsername = (TextView) itemView.findViewById(R.id.username);
            vUsername.setText(pickIt.getUsername());
//                vUsername.setId(ID_ADDITIVE+2000+pickIt.getPickItID());

            vCategory = (TextView) itemView.findViewById(R.id.category);
            vCategory.setText(pickIt.getCategory());
//                vCategory.setId(ID_ADDITIVE+3000+pickIt.getPickItID());

            vVotingTime = (TextView) itemView.findViewById(R.id.voting_time);
//                vVotingTime.setId(ID_ADDITIVE+4000+pickIt.getPickItID());

            vVotingTime.setText(pickIt.getLifeString());

            //endregion

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


}