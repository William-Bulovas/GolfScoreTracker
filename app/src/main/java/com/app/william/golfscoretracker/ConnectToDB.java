package com.app.william.golfscoretracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by William on 9/5/2015.
 */
public class ConnectToDB  extends AsyncTask<String, String, String>{
    Context context;
    Spinner spinner;
    TextView textView;
    String coursearray[];
    String coursepararray[];
    String courseid[];
    String state;
    TaskCallBack callback;
    ListView listView;

    public ConnectToDB(Context context, Spinner spinner, TextView textView){
            this.context = context;
            this.spinner = spinner;
            this.textView = textView;
            state = "getCourses";
    }

    public ConnectToDB(TaskCallBack callback){
        this.callback = callback;
        state = "new";
    }

    public ConnectToDB(ListView listView, Context context){
        this.listView = listView;
        this.context = context;
        state = "getScores";
    }

    @Override
    protected String doInBackground(String... arg0){
        if(state == "new"){
            try{
                String link = "https://radiant-bastion-3274.herokuapp.com/index.php?request=new&date="+arg0[1]+"&score="+arg0[0]+"&course="+arg0[2];
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));

                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


                StringBuffer sb = new StringBuffer("");
                String line = "";

                // Read Server Response
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();

                return sb.toString();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        } else if(state == "getCourses"){
            try {
                String link = "https://radiant-bastion-3274.herokuapp.com/index.php?request=getCourses";
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));

                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


                StringBuffer sb = new StringBuffer("");
                String line = "";

                // Read Server Response
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();

                return sb.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                String link = "https://radiant-bastion-3274.herokuapp.com/index.php?request=scores";
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));

                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


                StringBuffer sb = new StringBuffer("");
                String line = "";

                // Read Server Response
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();

                return sb.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (state == "getCourses") {
            try {
                JSONArray array = new JSONArray(result);
                coursearray = new String[array.length()];
                coursepararray = new String[array.length()];
                courseid = new String[array.length()];

                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    coursearray[i] = row.getString("name");
                    coursepararray[i] = row.getString("par");
                    courseid[i] = row.getString("idcourses");
                }

                ArrayAdapter<String> c_s_adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, coursearray);

                c_s_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(c_s_adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        textView.setText(coursepararray[position]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                textView.setText(coursepararray[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (state == "getScores") {
            try {
                JSONArray array = new JSONArray(result);
                coursearray = new String[array.length()];
                coursepararray = new String[array.length()];
                courseid = new String[array.length()];
                String score[] = new String[array.length()];

                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    coursearray[i] = row.getString("name");
                    coursepararray[i] = row.getString("par");
                    courseid[i] = row.getString("date");
                    score[i] = row.getString("score");
                }

                listView.setAdapter(new GolfScoreAdapter(context, R.layout.golf_score_list_item, score, courseid, coursearray, coursepararray));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else {


            callback.done();
        }
    }

    public static class GolfScoreAdapter extends ArrayAdapter<String> {
        private String[] list_of_scores;
        private String[] list_of_dates;
        private String[] list_of_courses;
        private String[] list_of_pars;

        public GolfScoreAdapter(Context context, int resource, String[] score, String[] date, String[] course, String[] par) {
            super(context, resource, score);

            list_of_scores = score;
            list_of_dates = date;
            list_of_courses = course;
            list_of_pars = par;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.golf_score_list_item, null);
            }
            String score = list_of_scores[position];
            String date = list_of_dates[position];
            String course = list_of_courses[position];
            String par = list_of_pars[position];

            if(score != null){
                TextView mScore = (TextView) v.findViewById(R.id.score);
                TextView mDate = (TextView) v.findViewById(R.id.date);
                TextView mCourse = (TextView) v.findViewById(R.id.course);
                TextView mPar = (TextView) v.findViewById(R.id.par);
                mScore.setText(score);
                mDate.setText(date);
                mCourse.setText(course);
                mPar.setText(par);
            }


            return v;
        }
    }
}
