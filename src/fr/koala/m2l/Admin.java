package fr.koala.m2l;

import javafx.scene.text.Text;

public class Admin {

    int id;
    String login;
    Text prenom;
    Text nom;
    String role;
    Role rolePerms;
    String mail;
    String passWord;
    public Admin(Integer id, String login, String prenom, String nom, String role, String mail) {
        this.id = id;
        this.prenom = new Text(prenom);
        this.nom = new Text(nom);
        this.login = login;
        this.mail = mail;
        this.role = role;
    }
    public Admin(Text prenom, Text nom, String role, int id) {
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
        this.id = id;

        switch (role) {
            case "Administrateur":
                this.rolePerms = Role.administrateur;
                break;
            case "Utilisateur":
                this.rolePerms = Role.utilisateur;
                break;
            case "Organisateur":
                this.rolePerms = Role.organisateur;
                break;
            default:
                this.rolePerms = Role.utilisateur;
        }
    }

    public Text getPrenom() {
        return prenom;
    }

    public Text getNom() {
        return nom;
    }

    public String getRole() {
        return role;
    }

    public Role getRolePerms() {
        return rolePerms;
    }

    public String getLogin() {
        return login;
    }

    public String getMail() {
        return mail;
    }

    public int getId() {
        return id;
    }
}
