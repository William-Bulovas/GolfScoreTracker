package com.app.william.golfscoretracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;


public class AddNewScore extends ActionBarActivity implements TaskCallBack{
    EditText score;
    TextView date;
    Spinner course_spinner;
    TextView par;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_new_score);

            //String dbcourses = getCourses();

            score = (EditText) findViewById(R.id.score);
            date = (TextView) findViewById(R.id.date);
            course_spinner = (Spinner) findViewById(R.id.course_spinner);
            par = (TextView) findViewById (R.id.par);

            new ConnectToDB(this, course_spinner, par).execute("getCourses", "", "");

            //ArrayAdapter<CharSequence> c_s_adapter = ArrayAdapter.createFromResource(this,
            //        R.array.course_options, android.R.layout.simple_spinner_item);

            //c_s_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //course_spinner.setAdapter(c_s_adapter);



            Calendar cal=Calendar.getInstance();

            SimpleDateFormat month_date = new SimpleDateFormat("MM");
            String month = month_date.format(cal.getTime());;

            SimpleDateFormat day_date = new SimpleDateFormat("d");
            String day = day_date.format(cal.getTime());;

            SimpleDateFormat year_date = new SimpleDateFormat("yyyy");
            String year = year_date.format(cal.getTime());;

            //par.setText("70");

            date.setText(year + '-' + month + "-" + day);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }



    public void onNothingSelected(AdapterView<?> parent){
        //
    }

    public void done(){

        //finish();
    }

    public void addScore (View view){
        String new_score = score.getText().toString();
        String new_date = date.getText().toString();
        String new_course = course_spinner.getSelectedItem().toString();


        if(new_score != null) {
            new ConnectToDB(this).execute(new_score, new_date, new_course);
        }
        NavUtils.navigateUpFromSameTask(this);
    }

    public void showDatePickerDialog(View v){
        DialogFragment newFragment= new DatePickerFragment();

        newFragment.show(getFragmentManager(),"datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet (DatePicker view, int year, int month, int day){
            TextView date = (TextView) getActivity().findViewById(R.id.date);

            String sYear = String.valueOf(year);

            String sMonth;
            if(month<10){
                sMonth = String.valueOf(month+1);
                sMonth = ("0" + sMonth);
            } else {
                sMonth = String.valueOf(month+1);
            }

            String sDay;
            if(day<10){
                sDay = String.valueOf(day);
                sDay = ("0" + sDay);
            } else {
                sDay = String.valueOf(day);
            }

            date.setText(sYear + '-' + sMonth + "-" + sDay);
        }
    }

}
