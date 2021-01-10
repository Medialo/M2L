package fr.koala.m2l;

import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class Equipe {

    private int id;
    private final Text nom;

    public Equipe(int id, Text nom) {
        this.id = id;
        this.nom = nom;
    }

    public Equipe(Text nom, Statement statement) {
        this.nom = nom;
        try {
            statement.executeUpdate("INSERT INTO `equipe` (`idequipe`, `nom`) VALUES ('" + Types.INTEGER + "', '" + nom + "')");
            ResultSet rs = statement.executeQuery("Select `idequipe` from `equipe` WHERE `nom`=" + nom);
            this.id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public Text getNom() {
        return nom;
    }

}
