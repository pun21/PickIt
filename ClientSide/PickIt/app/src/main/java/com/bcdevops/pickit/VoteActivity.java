package com.bcdevops.pickit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcdevops.pickit.database.handling.DatabaseAccess;
import com.bcdevops.pickit.fileIO.ServerFileManager;
import com.bcdevops.pickit.model.PickIt;
import com.bcdevops.pickit.model.Vote;

import org.achartengine.GraphicalView;

import java.util.HashMap;

public class VoteActivity extends FragmentActivity {
    //region Class variables
    private static HashMap<Integer, ImageView> map;
    private static PickIt pickIt;

    private PickItApp pickItApp;
    private ServerFileManager sm;

    private ImageView choice1;
    private ImageView choice2;
    private ImageView choice3;
    private ImageView choice4;

    private TextView username;
    //endregion

    //region Activity life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        pickItApp = (PickItApp)getApplication();

        pickIt = Globals.pickIt;
        pickIt.setVotes(new DatabaseAccess().retrievePickItVotes(pickIt.getPickItID()));

        for(int a = 0; a < pickIt.getVotes().size(); a++){
            if(pickIt.getVotes().get(a).getUserID() == pickItApp.getUserID())
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }

        sm = new ServerFileManager();
        getBitmaps();

        username = (TextView)findViewById(R.id.textView_username);

        String tempUsername = pickItApp.getUsername();
        if(tempUsername.contains("Guest"))
            tempUsername = "Guest";

        username.setText(tempUsername + "'s Upload");

        setPickItValues();
    }

    @Override
    protected void onResume(){
        super.onResume();

        pickIt = Globals.pickIt;
        pickIt.setVotes(new DatabaseAccess().retrievePickItVotes(pickIt.getPickItID()));

        for(int a = 0; a < pickIt.getVotes().size(); a++){
            if(pickIt.getVotes().get(a).getUserID() == pickItApp.getUserID())
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
    //endregion

    //region UI handlers
    public void onClickChoice(View v) {
        int source = v.getId();

        choice1 = (ImageView)findViewById(R.id.row0column0);
        choice2 = (ImageView)findViewById(R.id.row0column1);
        choice3 = (ImageView)findViewById(R.id.row1column0);
        choice4 = (ImageView)findViewById(R.id.row1column1);

        switch (source) {
            case R.id.row0column0:
                choice1.setBackground(getResources().getDrawable(R.drawable.custom_table_border));
                choice2.setBackground(null);
                choice3.setBackground(null);
                choice4.setBackground(null);
                break;
            case R.id.row0column1:
                choice1.setBackground(null);
                choice2.setBackground(getResources().getDrawable(R.drawable.custom_table_border));
                choice3.setBackground(null);
                choice4.setBackground(null);
                break;
            case R.id.row1column0:
                choice1.setBackground(null);
                choice2.setBackground(null);
                choice3.setBackground(getResources().getDrawable(R.drawable.custom_table_border));
                choice4.setBackground(null);
                break;
            case R.id.row1column1:
                choice1.setBackground(null);
                choice2.setBackground(null);
                choice3.setBackground(null);
                choice4.setBackground(getResources().getDrawable(R.drawable.custom_table_border));
                break;
            default:
        }
    }

    public void onClickUsername(View v) {
        //go to Profile Admin Activity
        Globals.nextUsername = pickItApp.getUsername();

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onClickVote(View v) {
        choice1 = (ImageView)findViewById(R.id.row0column0);
        choice2 = (ImageView)findViewById(R.id.row0column1);
        choice3 = (ImageView)findViewById(R.id.row1column0);
        choice4 = (ImageView)findViewById(R.id.row1column1);

        int choiceID = 0;

        if(choice1.getBackground() != null){
            choiceID = pickIt.getChoices().get(0).getChoiceID();
        }else if(choice2.getBackground() != null){
            choiceID = pickIt.getChoices().get(1).getChoiceID();
        }else if(choice3.getBackground() != null){
            choiceID = pickIt.getChoices().get(2).getChoiceID();
        }else if(choice4.getBackground() != null){
            choiceID = pickIt.getChoices().get(3).getChoiceID();
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Make a choice!";
            int time = Toast.LENGTH_LONG;

            Toast.makeText(context, text, time).show();
            return;
        }

        DatabaseAccess access = new DatabaseAccess();
        access.sendVote(new Vote(pickIt.getPickItID(), choiceID, pickItApp.getUserID()));

        final VoteActivity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Globals.pickIt = pickIt;

                Intent intent = new Intent(activity, ResultsActivity.class);
                startActivity(intent);
            }
        });
    }
    //endregion

    //region Helper methods
    private void setPickItValues(){
        final VoteActivity activity = this;
        final ImageView r0c0 = (ImageView)findViewById(R.id.row0column0);
        final ImageView r0c1 = (ImageView)findViewById(R.id.row0column1);
        final ImageView r1c0 = (ImageView)findViewById(R.id.row1column0);
        final ImageView r1c1 = (ImageView)findViewById(R.id.row1column1);
        final TextView description = (TextView)findViewById(R.id.upload_description);

        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                                r1c1.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            r1c0.setVisibility(View.INVISIBLE);
                            r1c1.setVisibility(View.INVISIBLE);
                        }

                        if(pickIt.getSubjectHeader().trim().length() == 0)
                            description.setVisibility(View.GONE);
                        else
                            description.setText(pickIt.getSubjectHeader());
                    }
                });

            }
        }).start();
    }
    private void getBitmaps(){
        map = new HashMap<>();

        for(int a = 0; a < pickIt.getChoices().size(); a++){
            ImageView view = new ImageView(this);
            sm.downloadPicture(view, pickIt.getChoices().get(a).getFilename());

            map.put(a, view);
        }
    }
    //endregion

}
