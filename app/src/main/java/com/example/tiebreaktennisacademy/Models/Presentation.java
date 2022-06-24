package com.example.tiebreaktennisacademy.Models;

public class Presentation {
    private int image;
    private String titre,description;

    public Presentation(int image, String titre, String description) {
        this.image = image;
        this.titre = titre;
        this.description = description;
    }

    public Presentation() {
        super();
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
