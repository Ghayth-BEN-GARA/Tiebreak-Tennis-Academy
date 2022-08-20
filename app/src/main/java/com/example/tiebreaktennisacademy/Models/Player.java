package com.example.tiebreaktennisacademy.Models;

public class Player {
    private String photo;
    private String fullname;
    private String email;

    public Player() {
        super();
    }

    public Player(String photo, String fullname, String email) {
        this.photo = photo;
        this.fullname = fullname;
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
