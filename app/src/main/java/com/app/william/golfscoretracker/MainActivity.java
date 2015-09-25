package com.app.william.golfscoretracker;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements Update_Results{

    ListView golfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        golfList = (ListView) findViewById(R.id.score_list);

        String[] dates = getResources().getStringArray(R.array.date_list);
        String[] courses = getResources().getStringArray(R.array.course_list);
        String[] scores = getResources().getStringArray(R.array.golf_scores_test);

        new ConnectToDB(golfList, this).execute("","","");
        //golfList.setAdapter(new GolfScoreAdapter(this, R.layout.golf_score_list_item, scores, dates, courses));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_score) {
            Intent intent = new Intent(this, AddNewScore.class);
            intent.putExtra("EXTRA_UPDATE", "hi");
            startActivity(intent);

            new ConnectToDB(golfList, this).execute("", "", "");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void update(){
        new ConnectToDB(golfList, this).execute("","","");
    }

}
