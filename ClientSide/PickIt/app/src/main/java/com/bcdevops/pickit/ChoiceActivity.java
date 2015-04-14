package com.bcdevops.pickit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bcdevops.pickit.model.Demographics;
import com.bcdevops.pickit.model.PickIt;
import com.bcdevops.pickit.model.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class ChoiceActivity extends Activity {

    private static HashMap<String, Integer> graphValues;
    private static PickIt pickIt;
    private static int choiceID;
    private static int demoCategory;
    private boolean noVotes = false;
    private Button btn_nextGraph;
    String[][] code = new String[][] {{"Male", "Female", "Other"},
            {"African","African-American", "Asian", "Caucasian", "Hispanic", "Latino", "Native American", "Pacific Islander", "Other" },
            {"Buddhism", "Christianity", "Hinduism", "Islam", "Judaism", "Other", "None"},
            {"Democrat", "Independent", "Republican", "Other", "None"}};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        pickIt = Globals.pickIt;
        choiceID = Globals.choice.getChoiceID();
        demoCategory = Globals.demoCategory;

        LinearLayout lv1 = (LinearLayout) findViewById(R.id.linear);
        if (demoCategory < 3) {
            btn_nextGraph = new Button(this);
            btn_nextGraph.setText("See Next Graph");
            btn_nextGraph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callIntent();
                }
            });
            lv1.addView(btn_nextGraph);
        }

        for (int i = 0; i < code[demoCategory].length; i++) {
            TextView tv = new TextView(this);
            tv.setText("textView " + i);
            lv1.addView(tv);
        }
        getGraphValues();

        MyGraphview graphview = createGraph();

        //add graph if there were votes for this choice
        if (!noVotes)
            lv1.addView(graphview);
        else {
            TextView tv = new TextView(this);
            tv.setText("There were no votes for this choice");
            lv1.addView(tv);
        }

        if (demoCategory == 3) {
            Globals.demoCategory = 0;
        } else {
            Globals.demoCategory++;
        }
    }

    public void callIntent() {
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
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
    private ArrayList<Vote> getVotes() {
        ArrayList<Vote> votes = pickIt.getVotes();
        ArrayList<Vote> choiceVotes = new ArrayList<>();
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).getChoiceID() == choiceID) {
                choiceVotes.add(votes.get(i));
            }
        }
        if (choiceVotes.isEmpty())
            noVotes = true;

        return choiceVotes;
    }

    private void getGraphValues() {
        initGraphValues();

        ArrayList<Vote> votes = getVotes();

        for (int i = 0; i < votes.size(); i++) {
            Demographics demo = votes.get(i).getDemographics();
            graphValues.put(demo.getEthnicity(), graphValues.get(demo.getEthnicity())+1);
            graphValues.put(demo.getGender(), graphValues.get(demo.getGender())+1);
            graphValues.put(demo.getReligion(), graphValues.get(demo.getReligion())+1);
            graphValues.put(demo.getPoliticalAffiliation(), graphValues.get(demo.getPoliticalAffiliation())+1);
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
    private MyGraphview createGraph() {
        return new MyGraphview(this, calculateData(getValues(code[demoCategory])));
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
        RectF rectf = new RectF(120, 120, 380, 380);
        float temp = 0;

        public MyGraphview(Context context, float[] values) {
            super(context);
            value_degree = new float[values.length];
            for (int i = 0; i < values.length; i++) {
                value_degree[i] = values[i];
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Random r;
            for (int i = 0; i < value_degree.length; i++) {
                int color;
                if (i == 0) {
                    r = new Random();

                    color = Color.argb(100, r.nextInt(256), r.nextInt(256),
                            r.nextInt(256));
                    while (color == R.color.white) {
                        color = Color.argb(100, r.nextInt(256), r.nextInt(256),
                                r.nextInt(256));
                    }
                    paint.setColor(color);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                } else {
                    temp += value_degree[i - 1];
                    r = new Random();
                    color = Color.argb(255, r.nextInt(256), r.nextInt(256),
                            r.nextInt(256));
                    while (color == R.color.white) {
                        color = Color.argb(100, r.nextInt(256), r.nextInt(256),
                                r.nextInt(256));
                    }
                    paint.setColor(color);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
            }
        }
    }
}