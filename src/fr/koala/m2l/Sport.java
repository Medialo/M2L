package fr.koala.m2l;

import javafx.scene.text.Text;

public class Sport {

    int id;
    Text nom;

    public Sport(int id, Text nom) {
        this.nom = nom;
        this.id = id;
    }

    public Text getNom() {
        return nom;
    }

    public int getId() {
        return id;
    }


}
