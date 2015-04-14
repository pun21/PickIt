package com.bcdevops.pickit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Pair;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bcdevops.pickit.fileIO.ServerFileManager;
import com.bcdevops.pickit.model.Choice;
import com.bcdevops.pickit.model.Demographics;
import com.bcdevops.pickit.model.PickIt;
import com.bcdevops.pickit.model.Vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ChoiceActivity extends Activity {
    //region Class variables

    private static HashMap<String, Integer> graphValues;
    private static HashMap<String, Integer> legendColors;
    private ArrayList<MyGraphview> graphList;
    private ArrayList<Vote> votes;
    private PickItApp pickItApp;
    private Choice choice;
    private PickIt pickIt;
    private int demoCategory;
    private ImageView image;
    private LinearLayout graph_layout;
    private TableLayout legend_layout;
    private TextView total_stats, gender_stats, ethnicity_stats, political_stats, religion_stats, title;
    private int[] colors;
    private String[][] code = new String[][] {{"Male", "Female", "Other"},
            {"African","African-American", "Asian", "Caucasian", "Hispanic", "Latino", "Native American", "Pacific Islander", "Other" },
            {"Buddhism", "Christianity", "Hinduism", "Islam", "Judaism", "Other", "None"},
            {"Democrat", "Independent", "Republican", "Other", "None"}};
    private String[] chart_title = new String[] {"Gender", "Ethnicity", "Religion", "Political Affiliation"};

    //endregion

    //region Activity life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        pickItApp = (PickItApp)getApplication();
        colors = this.getResources().getIntArray(R.array.legend);

        demoCategory = 0;
        choice = Globals.choice;
        pickIt = Globals.pickIt;
        votes = pickIt.getVotes();
        votes = getVotesForChoiceID(choice.getChoiceID());
        getLegendColors();

        image = (ImageView)findViewById(R.id.choice_imageView);
        total_stats = (TextView)findViewById(R.id.total_vote_stats);
        gender_stats = (TextView)findViewById(R.id.gender_stats);
        ethnicity_stats = (TextView)findViewById(R.id.ethnicity_stats);
        political_stats = (TextView)findViewById(R.id.political_stats);
        religion_stats = (TextView)findViewById(R.id.religion_stats);

        title = (TextView)findViewById(R.id.graph_title);
        graph_layout = (LinearLayout) findViewById(R.id.linear);
        legend_layout = (TableLayout) findViewById(R.id.legend_table);

        setUsername();
        setChoiceImage();
        setStatistics();

        getGraphValues();
        createGraphs();
        addViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choice, menu);
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

    //region UI handlers
    public void onClickAppName(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onClickUsername(View v) {
        // When we go to the ProfileActivity,  set the nextUserName as
        // the profile page that we are intending to view
        if(pickItApp.isGuest())
            return;

        Globals.nextUsername = pickItApp.getUsername();

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    public void onClickNextGraph(View v) {
        if (demoCategory < 3)
            demoCategory++;
        else
            demoCategory = 0;

        addViews();
    }
    //endregion

    //region Helper methods
    private void createGraphs() {
        graphList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            graphList.add(new MyGraphview(this, calculateData(getValues(code[i]))));
        }
    }
    private void addViews() {

        if (votes.isEmpty() & demoCategory == 0) {
            Button btn = (Button) findViewById(R.id.btn_next);
            btn.setVisibility(View.GONE);
            if (graph_layout.getChildCount() == 0) {
                TextView tv = new TextView(this);
                tv.setText("There were no votes for this choice");
                tv.setTextSize(18);
                graph_layout.addView(tv);
            }
        }
        else if (!votes.isEmpty()){
            setLegend();
            title.setText(chart_title[demoCategory]);
            if (graph_layout.getChildAt(0) != null)
                graph_layout.removeAllViews();


            graph_layout.addView(new MyGraphview(this, calculateData(getValues(code[demoCategory]))));
        }
    }
    private ArrayList<Vote> getVotesForChoiceID(int choiceID){
        ArrayList<Vote> votesTemp = new ArrayList<>();
        for(int a = 0; a < votes.size(); a++){
            if(votes.get(a).getChoiceID() == choiceID)
                votesTemp.add(votes.get(a));
        }

        return votesTemp;
    }
    private void setChoiceImage(){
        new ServerFileManager().downloadPicture(image, choice.getFilename());
    }
    private void setStatistics(){
        setTotalStatsView();

        setGenderStatsView();
        setEthnicityStatsView();
        setPoliticalStatsView();
        setReligionStatsView();
    }
    private void setGenderStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i< votes.size();i++){
            Demographics demo = votes.get(i).getDemographics();
            values.add(demo.getGender());
        }

        String statsString = "Gender";
        statsString += getStatsForDemo(values);
        gender_stats.setText(statsString);
    }
    private void setEthnicityStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < votes.size();i++){
            Demographics demo = votes.get(i).getDemographics();
            values.add(demo.getEthnicity());
        }

        String statsString = "Ethnicity";
        statsString += getStatsForDemo(values);
        ethnicity_stats.setText(statsString);
    }
    private void setPoliticalStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i< votes.size();i++){
            Demographics demo = votes.get(i).getDemographics();
            values.add(demo.getPoliticalAffiliation());
        }

        String statsString = "Political";
        statsString += getStatsForDemo(values);
        political_stats.setText(statsString);
    }
    private void setReligionStatsView(){
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i< votes.size();i++){
            Demographics demo = votes.get(i).getDemographics();
            values.add(demo.getReligion());
        }

        String statsString = "Religion";
        statsString += getStatsForDemo(values);
        religion_stats.setText(statsString);
    }
    private String getStatsForDemo(ArrayList<String> values){
        ArrayList<Pair<String, Integer>> pairs = new ArrayList<>();

        if(values.size() > 0){
            Collections.sort(values);

            if ((values.size() > 0)) {
                String firstValue = values.get(0);
                int count = 0;
                for (int i = 0; i < values.size(); i++) {
                    if (firstValue.equals(values.get(i))) {
                        count += 1;
                    }
                    else {
                        pairs.add(new Pair(firstValue, count));
                        firstValue = values.get(i);
                        count = 1;
                    }
                }

                pairs.add(new Pair(firstValue, count));

                String demoString = "";
                for (int i = 0; i < pairs.size();i++){
                    String demo = pairs.get(i).first;
                    Integer votes = pairs.get(i).second;
                    double percentage = (double) votes / (double)values.size();
                    String votePercentage = String.valueOf(percentage*100);
                    votePercentage = votePercentage.length() > 4 ? votePercentage.substring(0, 5) : votePercentage.substring(0, votePercentage.length());
                    demoString += "\n" + demo + ": " + votePercentage + "%";
                }
                return demoString;
            }
        }

        return "\nNo votes.";
    }
    private void setTotalStatsView(){
        if(votes.size() == 0){
            String totalStatsString = "\nTotal Votes: 0, 0.00%";
            total_stats.setText(totalStatsString);
            return;
        }

        double percent = (double)getVotesForChoice(choice.getChoiceID()) / (double)votes.size();
        String votePercentage = String.valueOf(percent * 100);
        votePercentage = votePercentage.length() > 4 ? votePercentage.substring(0, 4) : votePercentage.substring(0, votePercentage.length());
        String totalStatsString = "\nTotal Votes: " + getVotesForChoice(choice.getChoiceID()) +  ", " + votePercentage + "%";
        total_stats.setText(totalStatsString);
    }
    private int getVotesForChoice(int choiceID){
        int sum = 0;
        for(int a = 0; a < votes.size(); a++){
            if(pickIt.getVotes().get(a).getChoiceID() == choiceID)
                sum++;
        }

        return sum;
    }
    private void setUsername(){
        TextView username = (TextView)findViewById(R.id.textView_username);

        if(pickItApp.isGuest())
            pickItApp.setUsername("Guest");

        username.setText(pickItApp.getUsername());
    }
    //put all the colors for all the demographic strings into a hashmap
    private void getLegendColors() {

        legendColors = new HashMap<>();
        int k = 0;
        for (int i = 0; i < code.length; i++) {
            for ( int j = 0; j < code[i].length; j++) {
                legendColors.put(code[i][j], colors[k++]);
            }
        }
    }
    private void setLegend() {

        switch (demoCategory) {
            case 0:
                if (legend_layout.getChildAt(8).getVisibility() == View.VISIBLE) {
                    for (int i = 0; i < 6; i++) {
                        legend_layout.getChildAt(8 - i).setVisibility(View.GONE);
                    }
                } else {
                    for (int i = 0; i < 2; i++) {
                        legend_layout.getChildAt(4 - i).setVisibility(View.GONE);
                    }
                }
                for (int i = 0; i < 3; i++) {
                    ((ViewGroup)legend_layout.getChildAt(i)).getChildAt(0).setBackgroundColor(legendColors.get(code[demoCategory][i]));
                    ((TextView)((ViewGroup)legend_layout.getChildAt(i)).getChildAt(1)).setText(code[demoCategory][i]);
                }
                break;
            case 1:
                for (int i = 0; i < 6; i++) {
                    legend_layout.getChildAt(i+3).setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < 9; i++) {
                    ((ViewGroup)legend_layout.getChildAt(i)).getChildAt(0).setBackgroundColor(legendColors.get(code[demoCategory][i]));
                    ((TextView)((ViewGroup)legend_layout.getChildAt(i)).getChildAt(1)).setText(code[demoCategory][i]);
                }
                break;
            case 2:
                for (int i = 0; i < 2; i++) {
                    legend_layout.getChildAt(8-i).setVisibility(View.GONE);
                }
                for (int i = 0; i < 7; i++) {
                    ((ViewGroup)legend_layout.getChildAt(i)).getChildAt(0).setBackgroundColor(legendColors.get(code[demoCategory][i]));
                    ((TextView)((ViewGroup)legend_layout.getChildAt(i)).getChildAt(1)).setText(code[demoCategory][i]);
                }
                break;
            case 3:
                for (int i = 0; i < 2; i++) {
                    legend_layout.getChildAt(6-i).setVisibility(View.GONE);
                }
                for (int i = 0; i < 5; i++) {
                    ((ViewGroup)legend_layout.getChildAt(i)).getChildAt(0).setBackgroundColor(legendColors.get(code[demoCategory][i]));
                    ((TextView)((ViewGroup)legend_layout.getChildAt(i)).getChildAt(1)).setText(code[demoCategory][i]);
                }
                break;

        }
    }
    //get the votes for each element in a demographic category
    private float[] getValues(String[] category) {
        float[] values = new float[category.length];
        for (int i = 0; i < category.length; i++) {
            values[i] = graphValues.get(category[i]);
        }
        return values;
    }
    //put all the keys in and set to zero
    private void initGraphValues() {
        graphValues = new HashMap<>();
        for (int i = 0; i < code.length; i++) {
            for ( int j = 0; j < code[i].length; j++) {
                graphValues.put(code[i][j], 0);
            }
        }
    }
    private void getGraphValues() {
        initGraphValues();

        for (int i = 0; i < votes.size(); i++) {
            Demographics demo = votes.get(i).getDemographics();
            graphValues.put(demo.getEthnicity(), graphValues.get(demo.getEthnicity())+1);
            graphValues.put(demo.getGender(), graphValues.get(demo.getGender())+1);
            graphValues.put(demo.getReligion(), graphValues.get(demo.getReligion())+1);
            graphValues.put(demo.getPoliticalAffiliation(), graphValues.get(demo.getPoliticalAffiliation())+1);
        }
    }
    private float[] calculateData(float[] data) {
        float total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = 360 * (data[i] / total);
        }
        return data;
    }

    public class MyGraphview extends View {
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        RectF rectf = new RectF(10, 10, 400, 400);
        float temp = 0;
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight()/2;


        public MyGraphview(Context context, float[] values) {
            super(context);
            value_degree = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                value_degree[i] = values[i];
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.setMeasuredDimension(width/2, height/2);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            for (int i = 0; i < value_degree.length; i++) {
                int color;
                if (i == 0) {
                    //get color corresponding to the demographic category
                    color = legendColors.get(code[demoCategory][i]);
                    paint.setColor(color);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                } else {
                    temp += value_degree[i - 1];
                    color = legendColors.get(code[demoCategory][i]);
                    paint.setColor(color);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }
    }
    //endregion
}
