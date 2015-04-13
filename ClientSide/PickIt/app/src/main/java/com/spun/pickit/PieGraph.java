package com.spun.pickit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class PieGraph {

    public Intent getIntent(Context context) {

        double[][] distribution = {{2, 2, 2},
                {2, 4, 8, 16, 32, 64, 128, 254, 512},
                {1, 2, 3, 4, 5, 6, 7},
                {3, 3, 1, 5, 2}};
        String[][] code = new String[][] {{"Male", "Female", "Other"},
                {"African","African-American", "Asian", "Caucasian", "Hispanic", "Latino", "Native American", "Pacific Islander", "Other" },
                {"Buddhism", "Christianity", "Hinduism", "Islam", "Judaism", "Other", "None"},
                {"Democrat", "Independent", "Republican", "Other", "None"}};

        String[] categoryTitle = new String[] {"Gender", "Ethnicity", "Religion", "Political Affiliation"};

//        int Fuschia = this.getResources().getColor(R.color.Fuchsia);
//        int Purple = this.getResources().getColor(R.color.MediumPurple);
        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY, Color.CYAN, Color.MAGENTA};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(" Android version distribution as on October 1, 2012");
        for(int i=0 ;i <3;i++){
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[0][i], distribution[0][i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distribution.length;i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Votes for Choice "+0+" by "+"Category");
        defaultRenderer.setChartTitleTextSize(40);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setDisplayValues(true);
        defaultRenderer.setLabelsColor(Color.BLACK);

        Intent intent = ChartFactory.getPieChartIntent(context, distributionSeries, defaultRenderer, "Pie");
        return intent;
    }
}
