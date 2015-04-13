package com.spun.pickit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.spun.pickit.model.Choice;
import com.spun.pickit.model.Demographics;
import com.spun.pickit.model.PickIt;
import com.spun.pickit.model.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class ChoiceActivity extends Activity {

    private static HashMap<String, Integer> legend;
    private static HashMap<String, Integer> graphValues;
    private static PickIt pickIt;
    private static Choice choice;
    private int[] colors;
    private float distribution[];
    private LinearLayout lv1;
    private int[] position;
    float[] values;
    String[][] code = new String[][] {{"Male", "Female", "Other"},
            {"African","African-American", "Asian", "Caucasian", "Hispanic", "Latino", "Native American", "Pacific Islander", "Other" },
            {"Buddhism", "Christianity", "Hinduism", "Islam", "Judaism", "Other", "None"},
            {"Democrat", "Independent", "Republican", "Other", "None"}};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_one);

        pickIt = Globals.pickIt;
        choice = Globals.choice;
        colors = this.getResources().getIntArray(R.array.legend);

        int numVotes = pickIt.getChoices().size();
        values = {};

        getLegend();
        getGraphValues();
        lv1 = (LinearLayout) findViewById(R.id.linear);

        createGraphs();
        populateListView();
    }

    private void getGraphValues() {
        graphValues = new HashMap<>();
        ArrayList<Vote> votes = pickIt.getVotes();

        for (int i = 0; i < votes.size(); i++) {
            Demographics demo = votes.get(i).getDemographics();

            graphValues.put(demo.getEthnicity(), graphValues.get(demo.getEthnicity())+1);
            graphValues.put(demo.getGender(), graphValues.get(demo.getGender())+1);
            graphValues.put(demo.getReligion(), graphValues.get(demo.getReligion())+1);
            graphValues.put(demo.getPoliticalAffiliation(), graphValues.get(demo.getPoliticalAffiliation())+1);

        }
    }
    private void createGraphs() {
        int category = 3;
        values = calculateData(values);
        MyGraphview graphview = new MyGraphview(this, values, code[category]);
        lv1.addView(graphview);

    }
    private void getLegend() {
        legend = new HashMap<>();
        int k = 0;
        for (int i = 0; i < code.length; i++) {
            for ( int j = 0; j < code[i].length; j++) {
                legend.put(code[i][j], colors[k++]);
            }
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
        private String[] category;

        public MyGraphview(Context context, float[] values, String[] category) {
            super(context);
            value_degree = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                value_degree[i] = values[i];
            }
            this.category = category;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            DisplayMetrics metrics = getResources().getDisplayMetrics();

            float density  = getResources().getDisplayMetrics().density;
            float dpHeight = metrics.heightPixels / density;
            float dpWidth  = metrics.widthPixels / density;
            RectF rectf = new RectF(dpHeight, dpHeight, dpWidth*2, dpWidth*2);
            float temp = 0;

            Random r;
            for (int i = 0; i < value_degree.length; i++) {
                int color = legend.get(category[i]);
                if (i == 0) {
                    paint.setColor(color);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                } else {
                    temp += value_degree[i - 1];
                    r = new Random();
                    color = legend.get(category[i]);
                    paint.setColor(color);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }
    }

    private void populateListView() {
        CustomListAdapter adapter = new CustomListAdapter(this);
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }
    private class CustomListAdapter extends ArrayAdapter<Choice> {
        private ChoiceActivity choiceActivity;

        public CustomListAdapter(ChoiceActivity choiceActivity) {
            super(choiceActivity, R.layout.graph_layout, choice);
            this.choiceActivity = choiceActivity;
        }

        @Override
        public View getView(final int position,View view,ViewGroup parent) {
            super.getView(position, view, parent);

            // Make sure we have a view to work with (may have been given null)
            View itemView = view;

            if (itemView == null) {
                itemView = choiceActivity.getLayoutInflater().inflate(R.layout.graph_layout,
                        parent, false);
            }


            return itemView;
        }
    }
}