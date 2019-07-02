package com.example.solo.newremindapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.solo.newremindapp.domain.Remind;

import org.json.JSONException;
import org.json.JSONObject;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class CreateRemind extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, SetRemindDialog.SetRemindDialogListener  {

    private TextView dateText;
    private TextView timeText;
    private TextView remidText;
    private JSONObject remindJsonSender = new JSONObject();
    private Remind remindSender = new Remind();
    private String jsonString;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_remind);

        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        remidText = findViewById(R.id.remindText);

        findViewById(R.id.setTime).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            showTimePickerDialog();
            }
            });

            findViewById(R.id.setDate).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            showDatePickerDialog();
            }
            });


            findViewById(R.id.setRemind).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            showRemindDialog();
            }
            });

            findViewById(R.id.ready).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            sendRemind();
            }
            });

    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                getInstance().get(HOUR_OF_DAY),
                getInstance().get(MINUTE),
                true
        );
        timePickerDialog.show();
    }



    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                getInstance().get(YEAR),
                getInstance().get(MONTH),
                getInstance().get(DAY_OF_MONTH)

        );
        datePickerDialog.show();
    }

    public void showRemindDialog () {
        SetRemindDialog setRemindDialog = new SetRemindDialog();
        setRemindDialog.show(getSupportFragmentManager(),"remind_set_dialog");

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month+1) + "-" + year;
        remindSender.setDate(date);
        dateText.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time = hour+":"+minute;
        remindSender.setTime(time);
        timeText.setText(time);
    }


    @Override
    public void applyRemind(String remind) {
        remindSender.setRemind(remind);
        remidText.setText(remind);
    }

    private void sendRemind() {
        try {
            remindJsonSender.putOpt("date", remindSender.getDate());
            remindJsonSender.putOpt("time", remindSender.getTime());
            remindJsonSender.putOpt("remind", remindSender.getRemind());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                "http://192.168.1.190:8080/set",
                remindJsonSender,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        queue.add(request);
        finish();
    }
}
