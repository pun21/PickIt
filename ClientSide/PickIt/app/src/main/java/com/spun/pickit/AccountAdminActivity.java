package com.spun.pickit;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AccountAdminActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_admin);

        setSpinners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_admin, menu);
        return true;
    }

    public void setSpinners() {

        //Gender options
        Spinner spin_g = (Spinner) findViewById(R.id.spinner_gender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapt_g = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapt_g.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spin_g.setAdapter(adapt_g);

        //Ethnicity options
        Spinner spin_e = (Spinner) findViewById(R.id.spinner_ethnicity);
        ArrayAdapter<CharSequence> adapt_e = ArrayAdapter.createFromResource(this,
                R.array.ethnicity_array, android.R.layout.simple_spinner_item);
        adapt_e.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_e.setAdapter(adapt_e);

        //Religion options
        Spinner spin_r = (Spinner) findViewById(R.id.spinner_religion);
        ArrayAdapter<CharSequence> adapt_r = ArrayAdapter.createFromResource(this,
                R.array.ethnicity_array, android.R.layout.simple_spinner_item);
        adapt_r.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_e.setAdapter(adapt_r);

        //Political Affiliation options
        Spinner spin_p = (Spinner) findViewById(R.id.spinner_political);
        ArrayAdapter<CharSequence> adapt_p = ArrayAdapter.createFromResource(this,
                R.array.political_affiliation_array, android.R.layout.simple_spinner_item);
        adapt_p.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_p.setAdapter(adapt_r);
    }

    public void onClickSave(View v) {

    }

}
