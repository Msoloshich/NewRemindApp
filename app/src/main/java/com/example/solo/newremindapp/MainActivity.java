package com.example.solo.newremindapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.solo.newremindapp.domain.Remind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Remind> remindList = new ArrayList<Remind>();
    RequestQueue queue;
    String sample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addRemindIntent = new Intent(getApplicationContext(), CreateRemind.class);
                addRemindIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(addRemindIntent);
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();


        final ArrayAdapter<Remind> adapter;
        adapter = new customAdapter();
        adapter.clear();

        final ListView showRemindNotes = (ListView) findViewById(R.id.showNotes);




        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                "http://192.168.1.190:8080/get",
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray remindItems = response.getJSONArray("reminder");

                            for (int i = 0; i < remindItems.length(); i++) {
                                JSONObject temp = remindItems.getJSONObject(i);

                                int id =  temp.getInt("id");
                                String date = temp.getString("date");
                                String time = temp.getString("time");
                                String remind = temp.getString("remind");
                                remindList.add(new Remind(id, date, time,remind));
                            }
                            showRemindNotes.setAdapter(adapter);
                        }catch (JSONException e) {
                            Log.i("myTag",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag1", error.toString());
                    }
                });

        request.setRetryPolicy( new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        queue.add(request);



    }


    public class customAdapter extends ArrayAdapter<Remind> {
        public customAdapter() {
            super(MainActivity.this, R.layout.remind_layout, remindList);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.remind_layout, parent, false);
            }

            Remind currentItem = remindList.get(position);

            TextView date = (TextView) convertView.findViewById(R.id.showDate);
            TextView time = (TextView) convertView.findViewById(R.id.showTime);
            TextView remind = (TextView) convertView.findViewById(R.id.showRemind);

            date.setText(currentItem.getDate());
            time.setText(currentItem.getTime());
            remind.setText(currentItem.getRemind());


            return convertView;
        }
    }



}
