package com.example.solo.newremindapp.domain;

public class Remind {
    private int id;
    private String date;
    private String time;
    private String remind;

    public Remind() {
    }

    public Remind(int id, String date, String time, String remind) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.remind = remind;
    }

    public Remind(String date, String time, String remind) {
        this.date = date;
        this.time = time;
        this.remind = remind;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }
}
