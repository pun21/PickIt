package com.spun.pickit;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.spun.pickit.database.handling.DatabaseAccess;
import com.spun.pickit.fileIO.LocalFileManager;
import com.spun.pickit.fileIO.ServerFileManager;
import com.spun.pickit.model.Demographics;
import com.spun.pickit.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountAdminActivity extends Activity {
    //region Class Variables
    PickItApp pickItApp;
    LocalFileManager localFileManager;

    Spinner spin_g;
    Spinner spin_e;
    Spinner spin_r;
    Spinner spin_p;
    ArrayAdapter<CharSequence> adapt_g;
    ArrayAdapter<CharSequence> adapt_e;
    ArrayAdapter<CharSequence> adapt_r;
    ArrayAdapter<CharSequence> adapt_p;
    EditText mUsernameRepresentation;
    EditText mPasswordRepresentation;
    EditText mConfirmPasswordRepresentation;
    EditText mBirthday;
    ProgressBar loading;

    private static int layoutID;
    //endregion

    //region Life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_admin);

        pickItApp = (PickItApp)getApplication();
        localFileManager = new LocalFileManager(this);

        spin_g = (Spinner) findViewById(R.id.spinner_gender);
        spin_e = (Spinner) findViewById(R.id.spinner_ethnicity);
        spin_r = (Spinner) findViewById(R.id.spinner_religion);
        spin_p = (Spinner) findViewById(R.id.spinner_political);

        mUsernameRepresentation = (EditText)findViewById(R.id.usernameDemoTextbox);
        mPasswordRepresentation = (EditText)findViewById(R.id.passwordDemoTextbox);
        mConfirmPasswordRepresentation = (EditText)findViewById(R.id.confirmDemoTextbox);
        mBirthday = (EditText)findViewById(R.id.textField_bday);
        loading = (ProgressBar)findViewById(R.id.loading);

        layoutID = R.id.accountAdminLayout;

        setSpinners();
        updateScreen();
    }

    @Override
    protected void onResume(){
        super.onResume();

        endLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_admin, menu);
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }
    //endregion

    //region Input Handlers
    public void onClickSave(View v) {
        startLoad();

        if(isAcceptableData()){
            if(!(pickItApp.getUserID() > 0)){
                saveUser();
            }else{
                updateUser();
            }
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Invalid credentials\nPlease try again";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();

            endLoad();
        }
    }
    //endregion

    //region Helper Methods
    private void saveUser(){
        String tempUsername;
        final String tempPassword;
        String tempBirthday;
        String tempGender;
        String tempEthnicity;
        String tempReligion;
        String tempPolitical;

        try{
            if(pickItApp.isGuest()){
                tempUsername = "Guest"+(new Date().getTime() % 1427000000);
                tempPassword = "guest";
            }else{
                tempUsername = mUsernameRepresentation.getText().toString();
                tempPassword = mPasswordRepresentation.getText().toString();
            }


            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = formatter.parse(mBirthday.getText().toString());
            tempBirthday = new SimpleDateFormat("yyyy-MM-dd").format(date);

            Spinner mGender = (Spinner)findViewById(R.id.spinner_gender);
            tempGender = mGender.getSelectedItem().toString();

            Spinner mEthnicity = (Spinner)findViewById(R.id.spinner_ethnicity);
            tempEthnicity = mEthnicity.getSelectedItem().toString();

            Spinner mReligion = (Spinner)findViewById(R.id.spinner_religion);
            tempReligion = mReligion.getSelectedItem().toString();

            Spinner mPolitical = (Spinner)findViewById(R.id.spinner_political);
            tempPolitical = mPolitical.getSelectedItem().toString();
        }catch(Exception e){
            e.printStackTrace();
            Context context = getApplicationContext();
            CharSequence text = "Please enter valid inputs";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
            return;
        }

        final User user = new User(0, tempUsername, tempBirthday, tempGender, tempEthnicity, tempReligion, tempPolitical);
        final AccountAdminActivity activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                new AsyncSave(activity, user, tempPassword).start();
            }
        }).start();
    }
    private void updateUser(){
        String tempUsername;
        String tempBirthday;
        String tempGender;
        String tempEthnicity;
        String tempReligion;
        String tempPolitical;

        try{
            tempUsername = mUsernameRepresentation.getText().toString();

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = formatter.parse(mBirthday.getText().toString());
            tempBirthday = new SimpleDateFormat("yyyy-MM-dd").format(date);

            Spinner mGender = (Spinner)findViewById(R.id.spinner_gender);
            tempGender = mGender.getSelectedItem().toString();

            Spinner mEthnicity = (Spinner)findViewById(R.id.spinner_ethnicity);
            tempEthnicity = mEthnicity.getSelectedItem().toString();

            Spinner mReligion = (Spinner)findViewById(R.id.spinner_religion);
            tempReligion = mReligion.getSelectedItem().toString();

            Spinner mPolitical = (Spinner)findViewById(R.id.spinner_political);
            tempPolitical = mPolitical.getSelectedItem().toString();
        }catch(Exception e){
            e.printStackTrace();
            Context context = getApplicationContext();
            CharSequence text = "Please enter valid inputs";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
            return;
        }

        final User user = new User(pickItApp.getUserID(), tempUsername, tempBirthday, tempGender, tempEthnicity, tempReligion, tempPolitical);
        final AccountAdminActivity activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                new AsyncUpdate(activity, user, mPasswordRepresentation.getText().toString()).start();
            }
        }).start();
    }
    private void setUserInformation(int userID, String username, String birthday, String gender, String ethnicity, String religion, String political){
        Demographics demo = new Demographics(birthday, gender, ethnicity, religion, political);
        pickItApp.setUserID(userID);
        pickItApp.setUsername(username);
        pickItApp.setDemographics(demo);

        saveDemographicsLocally(demo);

        String demoFilePath = localFileManager.getDemographicsFilePath();

        ServerFileManager sm = new ServerFileManager(this, demoFilePath, pickItApp.getUsername()+"_demographics.json");
        sm.uploadDemographics();
    }
    private void saveDemographicsLocally(Demographics demographics){
        localFileManager.saveDemographics(demographics.getBirthday(), demographics.getGender(), demographics.getEthnicity(), demographics.getReligion(), demographics.getPoliticalAffiliation());
    }
    private boolean isAcceptableData(){
        if(pickItApp.isGuest()){
            if(birthdayIsCorrectFormat())
                return true;
        }else{
            if(birthdayIsCorrectFormat() && credentialsAreValid() && passwordsMatch())
                return true;
        }

        return false;
    }
    private boolean passwordsMatch(){
        return mPasswordRepresentation.getText().toString().equals(mConfirmPasswordRepresentation.getText().toString());
    }
    private boolean credentialsAreValid(){
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher1 = pattern.matcher(mUsernameRepresentation.getText().toString());
        Matcher matcher2 = pattern.matcher(mPasswordRepresentation.getText().toString());
        boolean usernameFine = !matcher1.find();
        boolean passwordFine = !matcher2.find();

        return !mUsernameRepresentation.getText().toString().equals("") && !mPasswordRepresentation.getText().toString().equals("")
                && !mConfirmPasswordRepresentation.getText().toString().equals("") && usernameFine && passwordFine;
    }
    private boolean birthdayIsCorrectFormat(){
        String temp = mBirthday.getText().toString();

        if(temp.length() == 10){
            try{
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date date = formatter.parse(temp);
                return true;
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return false;
    }
    public void setSpinners() {
        //Gender options
        adapt_g = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapt_g.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_g.setAdapter(adapt_g);

        //Ethnicity options
        adapt_e = ArrayAdapter.createFromResource(this,R.array.ethnicity_array, android.R.layout.simple_spinner_item);
        adapt_e.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_e.setAdapter(adapt_e);

        //Religion options
        adapt_r = ArrayAdapter.createFromResource(this,R.array.religion_array, android.R.layout.simple_spinner_item);
        adapt_r.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_r.setAdapter(adapt_r);

        //Political Affiliation options
        adapt_p = ArrayAdapter.createFromResource(this,R.array.political_affiliation_array, android.R.layout.simple_spinner_item);
        adapt_p.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_p.setAdapter(adapt_p);
    }
    public void updateScreen(){
        if(pickItApp.isGuest()){
            mUsernameRepresentation.setVisibility(View.GONE);
            mPasswordRepresentation.setVisibility(View.GONE);
            mConfirmPasswordRepresentation.setVisibility(View.GONE);
        }else {
            mUsernameRepresentation.setText(pickItApp.getUsername());
            mUsernameRepresentation.setEnabled(false);
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
        RelativeLayout layout = (RelativeLayout) findViewById(layoutID);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if(child.getId() != R.id.loading)
                child.setEnabled(enabled);
        }
    }
    public void selectDate(View view) {
        DialogFragment newFragment = new SelectBirthdayFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }
    public void populateSetDate(int year, int monthInput, int dayInput) {
        String month;
        String day;

        if(monthInput<10)
            month = "0"+monthInput;
        else
            month = ""+monthInput;

        if(dayInput<10)
            day = "0"+dayInput;
        else
            day = ""+dayInput;


        mBirthday.setText(month + "/" + day + "/" + year);
    }
    //endregion

    class AsyncUpdate extends Thread {
        final AccountAdminActivity activity;
        final User user;
        final String password;

        public AsyncUpdate(AccountAdminActivity activity, User user, String password) {
            this.activity = activity;
            this.user = user;
            this.password = password;
        }

        @Override
        public void run(){
            DatabaseAccess access = new DatabaseAccess();
            final boolean pass = access.updateUser(user.getID(), user.getUsername(), password, user.getBirthday(), user.getGender(), user.getEthnicity(), user.getReligion(), user.getPoliticalAffiliation());

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pass){
                        setUserInformation(user.getID(), user.getUsername(), user.getBirthday(), user.getGender(), user.getEthnicity(), user.getReligion(), user.getPoliticalAffiliation());

                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "We're sorry! We were unable to update your profile information";
                        int duration = Toast.LENGTH_LONG;

                        Toast.makeText(context, text, duration).show();
                    }
                }
            });
        }
    }
    class AsyncSave extends Thread {
        final AccountAdminActivity activity;
        final User user;
        final String password;

        public AsyncSave(AccountAdminActivity activity, User user, String password) {
            this.activity = activity;
            this.user = user;
            this.password = password;
        }

        @Override
        public void run(){
            DatabaseAccess access = new DatabaseAccess();
            final int userID= access.createUser(user.getUsername(), password, user.getBirthday(), user.getGender(), user.getEthnicity(), user.getReligion(), user.getPoliticalAffiliation());

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userID != 0){
                        setUserInformation(userID, user.getUsername(), user.getBirthday(), user.getGender(), user.getEthnicity(), user.getReligion(), user.getPoliticalAffiliation());

                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "We're sorry! We were unable to save your information";
                        int duration = Toast.LENGTH_LONG;

                        Toast.makeText(context, text, duration).show();
                    }

                    endLoad();
                }
            });
        }
    }
}
