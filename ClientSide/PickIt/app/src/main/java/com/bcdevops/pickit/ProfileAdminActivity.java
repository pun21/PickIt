package com.bcdevops.pickit;

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

import com.bcdevops.pickit.database.handling.DatabaseAccess;
import com.bcdevops.pickit.fileIO.LocalFileManager;
import com.bcdevops.pickit.fileIO.ServerFileManager;
import com.bcdevops.pickit.model.Demographics;
import com.bcdevops.pickit.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileAdminActivity extends Activity {
    //region Class Variables
    PickItApp pickItApp;
    LocalFileManager localFileManager;

    Spinner mGender;
    Spinner mEthnicity;
    Spinner mReligion;
    Spinner mPolitical;
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
        setContentView(R.layout.activity_profile_admin);

        pickItApp = (PickItApp)getApplication();
        localFileManager = new LocalFileManager(this);

        mGender = (Spinner) findViewById(R.id.spinner_gender);
        mEthnicity = (Spinner) findViewById(R.id.spinner_ethnicity);
        mReligion = (Spinner) findViewById(R.id.spinner_religion);
        mPolitical = (Spinner) findViewById(R.id.spinner_political);

        mUsernameRepresentation = (EditText)findViewById(R.id.usernameDemoTextbox);
        mPasswordRepresentation = (EditText)findViewById(R.id.passwordDemoTextbox);
        mConfirmPasswordRepresentation = (EditText)findViewById(R.id.confirmDemoTextbox);
        mBirthday = (EditText)findViewById(R.id.textField_bday);
        loading = (ProgressBar)findViewById(R.id.loading);

        mBirthday.setEnabled(false);

        layoutID = R.id.accountAdminLayout;
    }

    @Override
    protected void onResume(){
        super.onResume();

        setSpinners();
        updateScreen();
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

            tempBirthday = mBirthday.getText().toString();
            tempGender = mGender.getSelectedItem().toString();
            tempEthnicity = mEthnicity.getSelectedItem().toString();
            tempReligion = mReligion.getSelectedItem().toString();
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
        final ProfileAdminActivity activity = this;

        if(notOfAge(tempBirthday)){
            CharSequence text = "Age Requirement: 18+";

            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);

            startActivity(intent);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                new AsyncSave(activity, user, tempPassword).start();
            }
        }).start();
    }

    private boolean notOfAge(String birthday){
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        Date date;
        try {
            date = format.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }

        long eighteenYears = (long)1000*(long)60*(long)60*(long)24*(long)365*(long)18;

        long eighteenYearsAgo = new Date().getTime() - eighteenYears;
        if(date.getTime() > eighteenYearsAgo)
            return true;

        return false;
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
            tempBirthday = mBirthday.getText().toString();
            tempGender = mGender.getSelectedItem().toString();
            tempEthnicity = mEthnicity.getSelectedItem().toString();
            tempReligion = mReligion.getSelectedItem().toString();
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
        final ProfileAdminActivity activity = this;

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
            if(birthdayIsCorrectFormat() && spinnersAreChanged())
                return true;
        }else{
            if(birthdayIsCorrectFormat() && credentialsAreValid() && passwordsMatch() && spinnersAreChanged())
                return true;
        }

        return false;
    }
    private boolean spinnersAreChanged(){
        boolean genderChanged = !mGender.getSelectedItem().toString().equals(getResources().getString(R.string.required));
        boolean ethnicityChanged = !mEthnicity.getSelectedItem().toString().equals(getResources().getString(R.string.required));
        boolean religionChanged = !mReligion.getSelectedItem().toString().equals(getResources().getString(R.string.required));
        boolean politicalChanged = !mPolitical.getSelectedItem().toString().equals(getResources().getString(R.string.required));

        String valuesUnchanged = "";
        if(!genderChanged)
            valuesUnchanged += "\nGender";
        if(!ethnicityChanged)
            valuesUnchanged += "\nEthnicity";
        if(!religionChanged)
            valuesUnchanged += "\nReligion";
        if(!politicalChanged)
            valuesUnchanged += "\nPolitical Affiliation";

        if(!valuesUnchanged.equals("")){
            CharSequence text = "The following need to be changed: " + valuesUnchanged;
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            endLoad();
            return false;
        }

        return true;
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
        }else{
            CharSequence text = "Please enter your birthday";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            endLoad();
        }

        return false;
    }
    public void setSpinners() {
        //Gender options
        adapt_g = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapt_g.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGender.setAdapter(adapt_g);

        //Ethnicity options
        adapt_e = ArrayAdapter.createFromResource(this,R.array.ethnicity_array, android.R.layout.simple_spinner_item);
        adapt_e.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEthnicity.setAdapter(adapt_e);

        //Religion options
        adapt_r = ArrayAdapter.createFromResource(this,R.array.religion_array, android.R.layout.simple_spinner_item);
        adapt_r.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReligion.setAdapter(adapt_r);

        //Political Affiliation options
        adapt_p = ArrayAdapter.createFromResource(this,R.array.political_affiliation_array, android.R.layout.simple_spinner_item);
        adapt_p.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPolitical.setAdapter(adapt_p);

        //Set current values of user
        if(pickItApp.getUserID() > 0){
            String compareValue= pickItApp.getEthnicity();
            if (!compareValue.equals(null)) {
                int spinnerPostion = adapt_e.getPosition(compareValue);
                mEthnicity.setSelection(spinnerPostion);
            }

            compareValue= pickItApp.getGender();
            if (!compareValue.equals(null)) {
                int spinnerPostion = adapt_g.getPosition(compareValue);
                mGender.setSelection(spinnerPostion);
            }

            compareValue= pickItApp.getReligion();
            if (!compareValue.equals(null)) {
                int spinnerPostion = adapt_r.getPosition(compareValue);
                mReligion.setSelection(spinnerPostion);
            }
            compareValue= pickItApp.getPolitical();
            if (!compareValue.equals(null)) {
                int spinnerPostion = adapt_p.getPosition(compareValue);
                mPolitical.setSelection(spinnerPostion);
            }

            compareValue = pickItApp.getBirthday();
            if(!compareValue.equals(null)){
                String birthday = compareValue;
                mBirthday.setText(birthday);
            }
        }
    }
    public void updateScreen(){
        if(pickItApp.isGuest()){
            mUsernameRepresentation.setVisibility(View.GONE);
            mPasswordRepresentation.setVisibility(View.GONE);
            mConfirmPasswordRepresentation.setVisibility(View.GONE);
        }else if(pickItApp.getUserID() != 0){
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
        final ProfileAdminActivity activity;
        final User user;
        final String password;

        public AsyncUpdate(ProfileAdminActivity activity, User user, String password) {
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
        final ProfileAdminActivity activity;
        final User user;
        final String password;

        public AsyncSave(ProfileAdminActivity activity, User user, String password) {
            this.activity = activity;
            this.user = user;
            this.password = password;
        }

        @Override
        public void run(){
            DatabaseAccess access = new DatabaseAccess();
            final int userID = access.createUser(user.getUsername(), password, user.getBirthday(), user.getGender(), user.getEthnicity(), user.getReligion(), user.getPoliticalAffiliation());

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userID != 0){
                        String tempUsername = user.getUsername();

                        setUserInformation(userID, tempUsername, user.getBirthday(), user.getGender(), user.getEthnicity(), user.getReligion(), user.getPoliticalAffiliation());

                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "Error: We were unable to save your information :(";
                        int duration = Toast.LENGTH_LONG;

                        Toast.makeText(context, text, duration).show();
                    }

                    endLoad();
                }
            });
        }
    }
}
