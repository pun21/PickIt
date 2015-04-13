package com.spun.pickit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.spun.pickit.fileIO.ServerFileManager;
import com.spun.pickit.model.PickIt;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Voting_ResultsActivity extends FragmentActivity {
    //region Class variables
    private static final int GENDER = 0;
    private static final int ETHNICITY = 1;
    private static final int RELIGION = 2;
    private static final int POLITICAL_AFFILIATION = 3;

    private static HashMap<Integer, ImageView> map;
    private static HashMap<String, GraphicalView> chartMap;
    private static PickIt pickIt;

    private CustomPagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;
    private PickItApp pickItApp;
    private ServerFileManager sm;
    private boolean hasVoted = false;
    private static ViewGroup box;
    private static int graphID = 33;
    //endregion

    //region Activity life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_results);
        pickItApp = (PickItApp)getApplication();

        getCharts();

        List<Fragment> fragments = getFragments();
        mCustomPagerAdapter = new CustomPagerAdapter(this, getSupportFragmentManager(), getApplicationContext(), fragments);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_results, menu);
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
    public void onClickNavHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickNavUpload(View v) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

    public void onClickUpload(View v) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

    public void onClickUsername(View v) {
        //go to Profile Admin Activity
        //Everytime we go to a ProfileActivity,  set the nextUserName as
        // the profile page that we are viewing
        Globals.nextUsername = pickItApp.getUsername();

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onClickSignOut(View v) {
        pickItApp.resetUser();

        //go to login page after signing out
        Intent intent = new Intent(this, AppLoginActivity.class);
        startActivity(intent);
    }

    public void onClickVote(View v) {
        //todo
    }

    public void onClickChoice(View v) {
        int source = v.getId();

        int current = mViewPager.getCurrentItem();
        switch (source) {
            case R.id.row0column0:  mViewPager.setCurrentItem(1);
                break;
            case R.id.row0column1:  mViewPager.setCurrentItem(2);
                break;
            case R.id.row1column0:  mViewPager.setCurrentItem(3);
                break;
            case R.id.row1column1:  mViewPager.setCurrentItem(4);
                break;
            default:
        }
    }
    //endregion

    //region Helper methods
    private void getBitmaps(){
        map = new HashMap<>();

        for(int a = 0; a < pickIt.getChoices().size(); a++){
            ImageView view = new ImageView(this);
            sm.downloadPicture(view, pickIt.getChoices().get(a).getFilename());

            map.put(a, view);
        }
    }

    private void getCharts() {
        chartMap = new HashMap<>();

        double[][] distribution = {{2, 2, 2},
                {2, 4, 8, 16, 32, 64, 128, 254, 512},
                {1, 2, 3, 4, 5, 6, 7},
                {3, 3, 1, 5, 2}};

//        for(int i = 0; i < pickIt.getChoices().size(); i++) {
//            for(int j = 0; j < 4; j++) {
//                chartMap.put(i+"-"+j, openChart(i+1, j, distribution[j]));
//            }
//        }
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                chartMap.put(i+"-"+j, openChart(i+1, j, distribution[j]));
            }
        }

    }

    private List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<Fragment>();

        list.add(VoteFragment.newInstance("Fragment 0"));

        //todo i < number of choices
        for (int i = 0; i< 4; i++) {
            list.add(DemoFragment.newInstance("Fragment " + (i + 1)));
        }
        return list;
    }

    private GraphicalView openChart(int choice, int demoCategory, double[] distribution){
        //distribution is the section of the pie chart that each grouping has
        // ex. for demoCategory GENDER, distribution = {10, 10, 10}
        //meaning Male, Female, and Other each have a third of the chart

        // Pie Chart Section Names
        String[][] code = new String[][] {{"Male", "Female", "Other"},
                {"African","African-American", "Asian", "Caucasian", "Hispanic", "Latino", "Native American", "Pacific Islander", "Other" },
                {"Buddhism", "Christianity", "Hinduism", "Islam", "Judaism", "Other", "None"},
                {"Democrat", "Independent", "Republican", "Other", "None"}};

        String[] categoryTitle = new String[] {"Gender", "Ethnicity", "Religion", "Political Affiliation"};

        int Fuschia = this.getResources().getColor(R.color.Fuchsia);
        int Purple = this.getResources().getColor(R.color.MediumPurple);
        // Color of each Pie Chart Sections
        int[] colors = { Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY, Color.CYAN, Color.MAGENTA, Fuschia, Purple};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries(" Android version distribution as on October 1, 2012");
        for(int i=0 ;i < distribution.length;i++){
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[demoCategory][i], distribution[i]);
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

        defaultRenderer.setChartTitle("Votes for Choice "+choice+" by "+categoryTitle[demoCategory]);
        defaultRenderer.setChartTitleTextSize(40);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setDisplayValues(true);
        defaultRenderer.setLabelsColor(Color.BLACK);


        return ChartFactory.getPieChartView(getBaseContext(), distributionSeries, defaultRenderer);

    }
    //endregion

    //Adapter
    public class CustomPagerAdapter extends FragmentPagerAdapter {
        private Voting_ResultsActivity activity;
        protected Context mContext;
        private List<Fragment> fragments;

        public CustomPagerAdapter(Voting_ResultsActivity activity, FragmentManager fm, Context context, List<Fragment> fragments) {
            super(fm);
            this.activity = activity;
            mContext = context;
            this.fragments = fragments;
        }

        // This method returns the fragment associated with
        // the specified position. It is called when the Adapter needs a fragment
        // and it does not exists.
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Vote";
                case 1: return "Choice 1";
                case 2: return "Choice 2";
                case 3: return "Choice 3";
                case 4: return "Choice 4";
                default: return "";
            }
        }

    }

    //Demo Fragment
    public static class DemoFragment extends Fragment {
        View rootView;
        static int page = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout resource that'll be returned

            rootView = inflater.inflate(R.layout.fragment_demo, container, false);

            RelativeLayout b = (RelativeLayout) rootView.findViewById(R.id.relative_layout_graphs);
            RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            TableLayout tl = (TableLayout)rootView.findViewById(R.id.table_graphs);

            //remove any parent view from the GraphicalView before adding it to a View
//                if (chartMap.get(page + "-" + 0).getParent() != null)
//                    ((ViewGroup) chartMap.get(page + "-" + 0).getParent()).removeView((View)chartMap.get(page + "-" + i));

            TableRow tr = (TableRow)rootView.findViewById(R.id.first_row);
            tr.addView(chartMap.get(page + "-" + 0));

            tr = (TableRow)rootView.findViewById(R.id.second_row);
            tr.addView(chartMap.get(page + "-" + 1));

            tr = (TableRow)rootView.findViewById(R.id.third_row);
            tr.addView(chartMap.get(page + "-" + 2));

            tr = (TableRow)rootView.findViewById(R.id.fourth_row);
            tr.addView(chartMap.get(page + "-" + 3));

            page++;

            return rootView;
        }

        public static final DemoFragment newInstance(String text) {

            DemoFragment f = new DemoFragment();
            Bundle b = new Bundle();
            b.putString("msg", text);

            f.setArguments(b);

            return f;
        }
    }



    //Vote Fragment
    public static class VoteFragment extends Fragment {
        private View v;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.fragment_vote, container, false);


            final TextView ed = (TextView)v.findViewById(R.id.upload_description);
//            ed.setText(pickIt.getSubjectHeader());
//
//            final ImageView r0c0 = (ImageView)v.findViewById(R.id.row0column0);
//
//            final ServerFileManager serverFileManager = new ServerFileManager();
//            serverFileManager.downloadPicture(r0c0, pickIt.getChoices().get(0).getFilename());

            //the other ImageViews will be set here
            return v;
        }

        public static VoteFragment newInstance(String text) {
            VoteFragment f = new VoteFragment();
            Bundle b = new Bundle();
            b.putString("msg", text);

            f.setArguments(b);

            return f;
        }
    }
}
