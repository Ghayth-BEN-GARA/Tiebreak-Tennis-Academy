package com.example.tiebreaktennisacademy.Models;

public class Journal {
    private String phone;
    private String action;
    private String date;
    private String time;

    public Journal(String phone, String action, String date, String time) {
        this.phone = phone;
        this.action = action;
        this.date = date;
        this.time = time;
    }

    public Journal() {
        super();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String device) {
        this.phone = device;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

}
