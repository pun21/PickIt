package com.spun.pickit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spun.pickit.fileIO.FileManager;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        username = getPreferences(MODE_PRIVATE).getString(USERNAME_KEY, "");
        password = getPreferences(MODE_PRIVATE).getString(PASSWORD_KEY, "");
        confirmPassword = getPreferences(MODE_PRIVATE).getString(CONFIRM_KEY, "");

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

        editor.putString(USERNAME_KEY, username);
        editor.putString(PASSWORD_KEY, password);
        editor.putString(CONFIRM_KEY, confirmPassword);

        editor.commit();
    }
    //endregion

    //region Input Handlers
    public void onClickSave(View v) {
        startLoad();

        if(isAcceptableData()){
            if(pickItApp.getUserID() == -1){
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

        endLoad();
    }
    //endregion

    //Helper Methods
    private void saveUser(){

    }
    private void updateUser(){

    }
    private boolean isAcceptableData(){
        if(pickItApp.isGuest()){
            if(birthdayIsCorrectFormat())
                return true;
        }else{
            if(birthdayIsCorrectFormat() && credentialsAreFilled() && passwordsMatch())
                return true;
        }

        return false;
    }
    private boolean passwordsMatch(){
        return mPasswordRepresentation.getText().toString().equals(mConfirmPasswordRepresentation.getText().toString());
    }
    private boolean credentialsAreFilled(){
        return !mUsernameRepresentation.getText().toString().equals("") && !mPasswordRepresentation.getText().toString().equals("") && !mConfirmPasswordRepresentation.getText().toString().equals("");
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
            mUsernameRepresentation.setHeight(0);
            mUsernameRepresentation.setWidth(0);
            mUsernameRepresentation.setVisibility(View.GONE);

            mPasswordRepresentation.setHeight(0);
            mPasswordRepresentation.setWidth(0);
            mPasswordRepresentation.setVisibility(View.GONE);

            mConfirmPasswordRepresentation.setHeight(0);
            mConfirmPasswordRepresentation.setWidth(0);
            mConfirmPasswordRepresentation.setVisibility(View.GONE);
        }else{
            mUsernameRepresentation.setText(username);
            mPasswordRepresentation.setText(password);
            mConfirmPasswordRepresentation.setText(confirmPassword);
        }
    }
    private void startLoad(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.VISIBLE);

                enableLayoutChildren(false);
            }
        });
    }
    private void endLoad(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.INVISIBLE);

                enableLayoutChildren(true);
            }
        });
    }
    private void enableLayoutChildren(boolean enable){
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.accountAdminActivity);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(enable);
        }
    }
}
