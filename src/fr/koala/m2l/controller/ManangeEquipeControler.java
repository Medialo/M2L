package fr.koala.m2l.controller;

import fr.koala.m2l.ConnectionUtils;
import fr.koala.m2l.DataMapManager;
import fr.koala.m2l.Equipe;
import fr.koala.m2l.Personne;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManangeEquipeControler implements Initializable {

    static private Equipe equipe;
    public TableView tableLeft;
    public TableColumn colL1;
    public TableColumn colL2;
    public TableColumn colL3;
    public TableView tableRight;
    public TableColumn colR1;
    public TableColumn colR2;
    public TableColumn colR3;

    @FXML
    private Button btn;
    @FXML
    private CheckBox mode;


    //todo: a faire
    public static void setEquipe(Equipe equipe) {
        ManangeEquipeControler.equipe = equipe;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Statement statement = ConnectionUtils.getConn().createStatement();
            ResultSet rs = statement.executeQuery("SELECT `personne_id_personne` FROM `appartient` WHERE `equipe_idequipe` = " + equipe.getId());
            while (rs.next()) {
                tableRight.getItems().add(DataMapManager.getPersonneMap().get(rs.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        colL1.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colL2.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colL3.setCellValueFactory(new PropertyValueFactory<>("id"));
        colR1.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colR2.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colR3.setCellValueFactory(new PropertyValueFactory<>("id"));

        leftUpdate(false);
        Tooltip tooltip = new Tooltip("Le mode intelligent vous permet d'afficher uniquement les équipes \n" +
                "et utilisateurs qui pratique le sport de la compétition sélectionnée");
        mode.setTooltip(tooltip);
    }

    public void leftToRight(MouseEvent keyEvent) {

        if (tableLeft.getSelectionModel().getSelectedItem() != null) {

            Object o = tableLeft.getSelectionModel().getSelectedItem();
            tableLeft.getItems().remove(o);
            tableRight.getItems().add(o);
        }
    }

    public void rightToLeft(MouseEvent keyEvent) {
        if (tableRight.getSelectionModel().getSelectedItem() != null) {

            Object o = tableRight.getSelectionModel().getSelectedItem();
            tableRight.getItems().remove(o);
            tableLeft.getItems().add(o);
        }
    }

    public void compSet(MouseEvent event) {

        try {
            Connection conn = ConnectionUtils.getConn();
            Statement statement = conn.createStatement();

            statement.executeUpdate("DELETE FROM `appartient` WHERE `appartient`.`equipe_idequipe` = " + equipe.getId());
            for (Object personne : tableRight.getItems()) {

                statement.executeUpdate("INSERT INTO `appartient` (`personne_id_personne`, `equipe_idequipe`) VALUES ('" + ((Personne) personne).getId() + "', '" + equipe.getId() + "')");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage s = (Stage) btn.getScene().getWindow();
        s.close();
    }


    public void cancel(MouseEvent event) {
        Stage s = (Stage) btn.getScene().getWindow();
        s.close();
    }


    public void smartMode(MouseEvent event) {
        tableLeft.getItems().clear();
//        tableLeft.refresh();
//        tableRight.refresh();
        leftUpdate(mode.isSelected());
    }


    private void leftUpdate(boolean selected) {
        // tableLeft.getItems().clear();
        //tableRight.getItems().clear();

        if (selected) {

            for (Personne personne : DataMapManager.getPersonneMap().values()) {
                try {
                    Statement statement = ConnectionUtils.getConn().createStatement();
                    ResultSet rsSport = statement.executeQuery("SELECT `sport_idsport` FROM `sportequipe` WHERE `equipe_idequipe`= " + equipe.getId());
                    Statement statement1 = ConnectionUtils.getConn().createStatement();
                    ResultSet rs = statement1.executeQuery("SELECT `sport_idsport` FROM `sportpers` WHERE `personne_id_personne`= " + personne.getId());

                    while (rs.next()) {
                        while (rsSport.next()) {


                            if (rs.getInt(1) == rsSport.getInt(1)) {
                                if (!tableRight.getItems().contains(personne)) {
                                    tableLeft.getItems().add(personne);
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Personne personne : DataMapManager.getPersonneMap().values())
                if (!tableRight.getItems().contains(personne)) {
                    tableLeft.getItems().add(personne);
                }

        }

    }

}
