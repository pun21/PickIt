package com.spun.pickit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spun.pickit.database.handling.DatabaseAccess;
import com.spun.pickit.fileIO.FileManager;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AccountAdminActivity extends Activity {
    //region Class Variables
    private static final String USERNAME_KEY = "usernameAdminKey";
    private static final String PASSWORD_KEY = "passwordAdminKey";
    private static final String CONFIRM_KEY = "confirmAdminKey";
    PickItApp pickItApp;
    FileManager fileManager;

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
    TextView mBirthday;
    ProgressBar loading;

    private String username;
    private String password;
    private String confirmPassword;
    //endregion

    //region Life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_admin);

        pickItApp = (PickItApp)getApplication();
        fileManager = new FileManager(this);

        spin_g = (Spinner) findViewById(R.id.spinner_gender);
        spin_e = (Spinner) findViewById(R.id.spinner_ethnicity);
        spin_r = (Spinner) findViewById(R.id.spinner_religion);
        spin_p = (Spinner) findViewById(R.id.spinner_political);

        mUsernameRepresentation = (EditText)findViewById(R.id.usernameDemoTextbox);
        mPasswordRepresentation = (EditText)findViewById(R.id.passwordDemoTextbox);
        mConfirmPasswordRepresentation = (EditText)findViewById(R.id.confirmDemoTextbox);
        mBirthday = (TextView)findViewById(R.id.textField_bday);
        loading = (ProgressBar)findViewById(R.id.loading);

        loading.setVisibility(View.INVISIBLE);

        setSpinners();
        setEventListeners();
        updateScreen();
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

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

        editor.commit();
    }
    //endregion

    //region Input Handlers
    public void onClickSave(View v) {
        if(isAcceptableData()){
            if(pickItApp.getUserID() == 0){
                saveUser();
            }else{
                updateUser();
            }
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Invalid credentials\nPlease try again";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
        }
    }
    //endregion

    //Helper Methods
    private void saveUser(){
        String tempUsername;
        String tempPassword;
        String tempBirthday;
        String tempGender;
        String tempEthnicity;
        String tempReligion;
        String tempPolitical;

        try{
            if(pickItApp.isGuest()){
                tempUsername = "Guest"+new Date().getTime();
                tempPassword = "guest";
            }else{
                tempUsername = username;
                tempPassword = password;
            }


            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = formatter.parse(mBirthday.getText().toString());
            tempBirthday = new SimpleDateFormat("yyyy-MM-dd").format(date);

            Spinner mGender = (Spinner)findViewById(R.id.spinner_gender);
            tempGender = mGender.getSelectedItem().toString().replace(" ", "");

            Spinner mEthnicity = (Spinner)findViewById(R.id.spinner_ethnicity);
            tempEthnicity = mEthnicity.getSelectedItem().toString().replace(" ", "");

            Spinner mReligion = (Spinner)findViewById(R.id.spinner_religion);
            tempReligion = mReligion.getSelectedItem().toString().replace(" ", "");

            Spinner mPolitical = (Spinner)findViewById(R.id.spinner_political);
            tempPolitical = mPolitical.getSelectedItem().toString().replace(" ", "");
        }catch(Exception e){
            e.printStackTrace();
            Context context = getApplicationContext();
            CharSequence text = "Please enter valid inputs";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
            return;
        }

        DatabaseAccess access = new DatabaseAccess();
        int userID= access.createUser(tempUsername, tempPassword, tempBirthday, tempGender, tempEthnicity, tempReligion, tempPolitical);

        if (userID != 0){
            setUserInformation(userID, tempUsername, tempBirthday, tempGender, tempEthnicity, tempReligion, tempPolitical);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Context context = getApplicationContext();
            CharSequence text = "We're sorry! We were unable to save your information";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
        }
    }
    private void updateUser(){
        String tempUsername;
        String tempPassword;
        String tempBirthday;
        String tempGender;
        String tempEthnicity;
        String tempReligion;
        String tempPolitical;

        try{
            tempUsername = username;
            tempPassword = password;

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = formatter.parse(mBirthday.getText().toString());
            tempBirthday = new SimpleDateFormat("yyyy-MM-dd").format(date);

            Spinner mGender = (Spinner)findViewById(R.id.spinner_gender);
            tempGender = mGender.getSelectedItem().toString().replace(" ", "");

            Spinner mEthnicity = (Spinner)findViewById(R.id.spinner_ethnicity);
            tempEthnicity = mEthnicity.getSelectedItem().toString().replace(" ", "");

            Spinner mReligion = (Spinner)findViewById(R.id.spinner_religion);
            tempReligion = mReligion.getSelectedItem().toString().replace(" ", "");

            Spinner mPolitical = (Spinner)findViewById(R.id.spinner_political);
            tempPolitical = mPolitical.getSelectedItem().toString().replace(" ", "");
        }catch(Exception e){
            e.printStackTrace();
            Context context = getApplicationContext();
            CharSequence text = "Please enter valid inputs";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
            return;
        }

        DatabaseAccess access = new DatabaseAccess();
        boolean pass = access.updateUser(pickItApp.getUserID(), tempUsername, tempPassword, tempBirthday, tempGender, tempEthnicity, tempReligion, tempPolitical);

        if (pass){
            setUserInformation(pickItApp.getUserID(), tempUsername, tempBirthday, tempGender, tempEthnicity, tempReligion, tempPolitical);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Context context = getApplicationContext();
            CharSequence text = "We're sorry! We were unable to save user";
            int duration = Toast.LENGTH_LONG;

            Toast.makeText(context, text, duration).show();
        }
    }
    private void setUserInformation(int userID, String username, String birthday, String gender, String ethnicity, String religion, String political){
        pickItApp.setUserID(userID);
        pickItApp.setUsername(username);
        pickItApp.setBirthday(birthday);
        pickItApp.setGender(gender);
        pickItApp.setEthnicity(ethnicity);
        pickItApp.setReligion(religion);
        pickItApp.setPolitical(political);
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
    private void setEventListeners(){
        mUsernameRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                username = mUsernameRepresentation.getText().toString();
            }
        });

        mPasswordRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                password = mPasswordRepresentation.getText().toString();
            }
        });

        mConfirmPasswordRepresentation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                confirmPassword = mPasswordRepresentation.getText().toString();
            }
        });
    }
    public void updateScreen(){
        if(pickItApp.isGuest()){
            mUsernameRepresentation.setVisibility(View.GONE);
            mPasswordRepresentation.setVisibility(View.GONE);
            mConfirmPasswordRepresentation.setVisibility(View.GONE);
        }else{
            mUsernameRepresentation.setText(pickItApp.getUsername());

            if(pickItApp.getUserID() != 0)
                mUsernameRepresentation.setEnabled(false);


        }
    }
    private void startLoad(){
        enableLayoutChildren(false);
        loading.setEnabled(true);
        loading.setVisibility(View.VISIBLE);

    }
    private void endLoad(){
        enableLayoutChildren(true);
        loading.setEnabled(true);
        loading.setVisibility(View.INVISIBLE);
    }
    private void enableLayoutChildren(boolean enable){
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.accountAdminActivity);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(enable);
        }
    }
}
