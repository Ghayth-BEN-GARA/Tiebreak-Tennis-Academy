package com.example.tiebreaktennisacademy.Models;

public class Reservation {
    private String coache;
    private String court;
    private String date;
    private String to;
    private String from;
    private String email;

    public Reservation() {
        super();
    }

    public Reservation(String coache, String court, String date, String to, String from, String email) {
        this.coache = coache;
        this.court = court;
        this.date = date;
        this.to = to;
        this.from = from;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoache() {
        return coache;
    }

    public void setCoache(String coache) {
        this.coache = coache;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
