package fr.koala.m2l;

import javafx.scene.text.Text;


public class Personne {

    private Integer id;
    private Text nom;
    private Text prenom;
    private String mail;
    private String adresse;
    private String num;


    private Personne() {
    }


    public Personne(int id, Text nom, Text prenom, String adresse) {

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
    }


    public Personne(int id, Text nom, Text prenom, String mail, String adresse, String num) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.adresse = adresse;
        this.num = num;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Text getNom() {
        return nom;
    }

    public void setNom(Text nom) {
        this.nom = nom;
    }

    public Text getPrenom() {
        return prenom;
    }

    public void setPrenom(Text prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


}
