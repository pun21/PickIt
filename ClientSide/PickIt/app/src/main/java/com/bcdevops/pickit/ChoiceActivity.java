package com.bcdevops.pickit;

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
import android.widget.TableRow;
import android.widget.TextView;

import com.bcdevops.pickit.model.Choice;
import com.bcdevops.pickit.model.Demographics;
import com.bcdevops.pickit.model.PickIt;
import com.bcdevops.pickit.model.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class ChoiceActivity extends Activity {

    private static HashMap<String, Integer> legend;
    private static HashMap<String, Integer> graphValues;
    private static PickIt pickIt;
    private static Choice choice;
    private ArrayList<MyGraphview> graphs;
    private int[] colors;
    private float distribution[];
    private LinearLayout lv1;
    private TextView textView;
    private int[] position;
    String[][] code = new String[][] {{"Male", "Female", "Other"},
            {"African","African-American", "Asian", "Caucasian", "Hispanic", "Latino", "Native American", "Pacific Islander", "Other" },
            {"Buddhism", "Christianity", "Hinduism", "Islam", "Judaism", "Other", "None"},
            {"Democrat", "Independent", "Republican", "Other", "None"}};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        pickIt = Globals.pickIt;
        choice = Globals.choice;
        colors = this.getResources().getIntArray(R.array.legend);

        int numVotes = pickIt.getChoices().size();
        textView = new TextView(this);

        getLegend();
        //initGraphValues();
        getGraphValues();

        createGraphs();
        populateListView();
    }

//    private void initGraphValues() {
//        for (int i = 0; i < code.length; i++) {
//            for ( int j = 0; j < code[i].length; j++) {
//                graphValues.put(code[i][j], 0);
//            }
//        }
//    }
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

    //get the votes for each element in a demographic category
    private float[] getValues(String[] category) {
        float[] values = new float[category.length];
        for (int i = 0; i < category.length; i++) {
            values[i] = graphValues.get(category[i]);
        }
        return values;
    }
    private void createGraphs() {

        for (int i = 0; i < 4; i++) {
            MyGraphview graph = new MyGraphview(this, calculateData(getValues(code[i])), code[i]);
            graphs.add(graph);
        }
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
        private float[] values;
        private String[] category;

        public MyGraphview(Context context, float[] values, String[] category) {
            super(context);
            this.values = values;
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
            for (int i = 0; i < values.length; i++) {
                //get color corresponding to the demographic category
                int color = legend.get(category[i]);
                if (i == 0) {
                    paint.setColor(color);
                    canvas.drawArc(rectf, 0, values[i], true, paint);
                } else {
                    temp += values[i - 1];
                    r = new Random();
                    color = legend.get(category[i]);
                    paint.setColor(color);
                    canvas.drawArc(rectf, temp, values[i], true, paint);
                }
            }
        }
    }

    private void populateListView() {
        CustomListAdapter adapter = new CustomListAdapter(this);
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }
    private class CustomListAdapter extends ArrayAdapter<MyGraphview> {
        private ChoiceActivity choiceActivity;

        public CustomListAdapter(ChoiceActivity choiceActivity) {
            //if error here, might need an additional TextView id parameter for constructor
            super(choiceActivity, R.layout.graph_layout, graphs);
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
            TableRow tr = (TableRow) itemView.findViewById(R.id.graph_row);
            tr.addView(graphs.get(position));

            TableRow tl = (TableRow) itemView.findViewById(R.id.legend_row);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);

            int color;
            for (int i = 0; i < code[position].length; i++) {
                color = legend.get(code[position][i]);
                TextView color_square = new TextView(getContext());
                color_square.setBackgroundColor(color);
                lp.weight = 1;
                color_square.setLayoutParams(lp);

                TextView key_word = new TextView(getContext());
                key_word.setText(code[position][i]);
                lp.weight = 8;
                key_word.setLayoutParams(lp);

                tl.addView(color_square);
                tl.addView(key_word);
            }
            return itemView;
        }
    }
}