package fr.koala.m2l;

import javafx.scene.text.Text;

import java.util.Date;

public class Competition {

    int id;
    Text nom;
    Date date;
    Date dateFin;
    Date dateClotureInscription;
    int sport;
    boolean type;

    private Competition() {
    }

    public Competition(int id, Text nom, Date date, Date dateFin, Date dateClotureInscription, int sport, boolean type) {
        this.id = id;
        this.nom = nom;
//        System.out.println(date);
//        System.out.println(new Date());
//        System.out.println(date.before(new Date()));
        if (date.before(new Date())) {
            this.nom.setStyle("-fx-fill: red;");
        }


        this.date = date;
        this.dateFin = dateFin;
        this.dateClotureInscription = dateClotureInscription;
        this.sport = sport;
        this.type = type;
    }

    public Text getNom() {
        return nom;
    }

    public int getSport() {
        return sport;
    }

    public boolean isType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public boolean getType() {
        return type;
    }

    public String getTypeString() {
        if (type) {
            return "oui";
        } else {
            return "non";
        }
    }

    public String getTypeStringComplet() {
        if (type) {
            return "Individuel";
        } else {
            return "Collective";
        }
    }

    public String getType01() {
        if (type) {
            return "1";
        } else {
            return "0";
        }
    }

    public Date getDate() {
        return date;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public Date getDateClotureInscription() {
        return dateClotureInscription;
    }
}
