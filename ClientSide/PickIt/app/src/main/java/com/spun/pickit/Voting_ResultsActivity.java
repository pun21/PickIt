package com.spun.pickit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.spun.pickit.fileIO.ServerFileManager;
import com.spun.pickit.model.PickIt;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


public class Voting_ResultsActivity extends ActionBarActivity {

    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    PagerTabStrip mTabStrip;
    static GraphicalView mChart;
    RelativeLayout layout;
    private PickItApp pickItApp;
    private PickIt pickIt;
    static Voting_ResultsActivity voting_resultsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_results);
        voting_resultsActivity = this;
        pickItApp = (PickItApp)getApplication();

        pickIt = Globals.pickIt;

        mChart = openChart(1, 1);
        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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
    public class CustomPagerAdapter extends FragmentStatePagerAdapter {

        protected Context mContext;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        // This method returns the fragment associated with
        // the specified position.
        //
        // It is called when the Adapter needs a fragment
        // and it does not exists.
        public Fragment getItem(int position) {

            switch(position) {
                case 0: VoteFragment frag0 = new VoteFragment();
                    Fragment a = frag0.newInstance("VoteFragment, Instance 1");
                    return a;
                case 1: DemoFragment frag1 = new DemoFragment();
                    Fragment b = frag1.newInstance("DemoFragment, Instance 1");
                    return b;
                case 2: DemoFragment frag2 = new DemoFragment();
                    Fragment c = frag2.newInstance("DemoFragment, Instance 2");
                    return c;
                case 3: DemoFragment frag3 = new DemoFragment();
                    Fragment d = frag3.newInstance("DemoFragment, Instance 3");
                    return d;
                case 4: DemoFragment frag4 = new DemoFragment();
                    Fragment e = frag4.newInstance("DemoFragment, Instance 4");
                    return e;
                default: DemoFragment frag5 = new DemoFragment();
                    Fragment f = frag5.newInstance("DemoFragment, Instance 1");
                    return f;
            }

        }

        @Override
        public int getCount() {
            return 5;
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

    public static class DemoFragment extends Fragment {
        View rootView;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout resource that'll be returned

            rootView = inflater.inflate(R.layout.fragment_demo, container, false);
            // Get the arguments that was supplied when
            // the fragment was instantiated in the
            // CustomPagerAdapter

//            if(mChart.getParent()!=null)
//                ((View)mChart.getParent()).removeView(mChart);

            ((RelativeLayout) rootView).addView(mChart);

            return rootView;
        }

        public DemoFragment newInstance(String text) {

            DemoFragment f = new DemoFragment();

            Bundle b = new Bundle();
            b.putString("msg", text);

            f.setArguments(b);

            return f;
        }
    }

    public static class VoteFragment extends Fragment {
        View v;
        VoteFragment vot = this;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.fragment_vote, container, false);

            ServerFileManager sm = new ServerFileManager();
            //to get to the ImageViews
            RelativeLayout b = (RelativeLayout)v;
            TableRow tr = (TableRow)b.getChildAt(0);
            EditText ed = (EditText)tr.getChildAt(0);
            //todo set subjectHeader
            //ed.setText(pickIt.getSubjectHeader());

            ScrollView a = (ScrollView) b.getChildAt(1);
            TableLayout tl = (TableLayout)a.getChildAt(0);
            GridLayout gl = (GridLayout)tl.getChildAt(0);

            //ImageViews on Vote page
            FrameLayout fl_0 = (FrameLayout)gl.getChildAt(0);
            ImageView r0c0 = (ImageView)fl_0.getChildAt(0);
//            voting_resultsActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String filename = pickIt.getChoices().get(0).getFilename();
//                    sm.downloadPicture(r0c0, filename);
//                }
//            });

            FrameLayout fl_1 = (FrameLayout)gl.getChildAt(1);
            ImageView r0c1 = (ImageView)fl_1.getChildAt(0);
            //todo set ImageView
//            voting_resultsActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String filename = pickIt.getChoices().get(1).getFilename();
//                    sm.downloadPicture(r0c1, filename);
//                }
//            });

            FrameLayout fl_2 = (FrameLayout)gl.getChildAt(2);
            ImageView r1c0 = (ImageView)fl_1.getChildAt(0);
            //todo setImageView
//            voting_resultsActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String filename = pickIt.getChoices().get(2).getFilename();
//                    sm.downloadPicture(r1c0, filename);
//                }
//            });

            FrameLayout fl_3 = (FrameLayout)gl.getChildAt(3);
            ImageView r1c1 = (ImageView)fl_1.getChildAt(0);
            //todo setImageView
//            voting_resultsActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String filename = pickIt.getChoices().get(3).getFilename();
//                    sm.downloadPicture(r1c1, filename);
//                }
//            });


            return v;
        }

        static VoteFragment newInstance(String text) {

            VoteFragment f = new VoteFragment();
            Bundle b = new Bundle();
            b.putString("msg", text);

            f.setArguments(b);

            return f;
        }
    }

    private GraphicalView openChart(int choice, int demoCategory){
        //also take in a json object or something to get the votes by demographic

        // Pie Chart Section Names
        String[][] code = new String[][] {{"Male", "Female", "Other"},
            {"African","African-American", "Asian", "Caucasian", "Hispanic", "Latino", "Native American", "Pacific Islander", "Other" },
            {"Buddhism", "Christianity", "Hinduism", "Islam", "Judaism", "Other", "None"},
            {"Democrat", "Independent", "Republican", "Other", "None"}};

        String[] categoryTitle = new String[] {"Gender", "Ethnicity", "Religion", "Political Affiliation"};

        // Pie Chart Section Value
        double[] distribution /*= { 3.9, 12.9 }*/ = new double[2];
        distribution[0] = 2;
        distribution[1] = 4;

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

        return ChartFactory.getPieChartView(getBaseContext(), distributionSeries, defaultRenderer);

    }
}
