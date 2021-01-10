package fr.koala.m2l.controller;

import fr.koala.m2l.*;
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

public class ManangeCompetitionControler implements Initializable {

    static private Competition competition;
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


    public static void setCompetition(Competition competition) {
        ManangeCompetitionControler.competition = competition;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (competition.getType()) {
            try {
                Statement statement = ConnectionUtils.getConn().createStatement();
                ResultSet rs = statement.executeQuery("SELECT `personne_id_personne` FROM `candidat` WHERE `competition_id_competition` = " + competition.getId());
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

        } else {
            try {
                Statement statement = ConnectionUtils.getConn().createStatement();
                ResultSet rs = statement.executeQuery("SELECT `idequipe` FROM `equipecandidat` WHERE `idcompetition` = " + competition.getId());
                while (rs.next()) {
                    tableRight.getItems().add(DataMapManager.getEquipeMap().get(rs.getInt(1)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            colL1.setCellValueFactory(new PropertyValueFactory<>("nom"));
            //  colL2.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            colL2.setVisible(false);
            colL3.setCellValueFactory(new PropertyValueFactory<>("id"));
            colR1.setCellValueFactory(new PropertyValueFactory<>("nom"));
            //     colR2.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            colR2.setVisible(false);
            colR3.setCellValueFactory(new PropertyValueFactory<>("id"));
        }
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
            if (competition.getType()) {
                statement.executeUpdate("DELETE FROM `candidat` WHERE `candidat`.`competition_id_competition` = " + competition.getId());
                for (Object personne : tableRight.getItems()) {

                    statement.executeUpdate("INSERT INTO `candidat` (`personne_id_personne`, `competition_id_competition`) VALUES ('" + ((Personne) personne).getId() + "', '" + competition.getId() + "')");
                }
            } else {
                statement.executeUpdate("DELETE FROM `equipecandidat` WHERE `equipecandidat`.`idcompetition` = " + competition.getId());
                for (Object equipe : tableRight.getItems()) {
                    // System.out.println(((Equipe) equipe).getNom().getText());
                    statement.executeUpdate("INSERT INTO `equipecandidat` (`idcompetition`, `idequipe`) VALUES ('" + competition.getId() + "', '" + ((Equipe) equipe).getId() + "')");
                }
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
        if (competition.getType()) {
            if (selected) {
                for (Personne personne : DataMapManager.getPersonneMap().values()) {
                    try {
                        Statement statement = ConnectionUtils.getConn().createStatement();
                        ResultSet rs = statement.executeQuery("SELECT `sport_idsport` FROM `sportpers` WHERE `personne_id_personne`= " + personne.getId());
                        while (rs.next()) {
                            if (rs.getInt(1) == competition.getSport()) {
                                if (!tableRight.getItems().contains(personne)) {
                                    tableLeft.getItems().add(personne);
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
        } else {


            if (selected) {
                for (Equipe equipe : DataMapManager.getEquipeMap().values()) {
                    try {
                        Statement statement = ConnectionUtils.getConn().createStatement();
                        ResultSet rs = statement.executeQuery("SELECT `sport_idsport` FROM `sportequipe` WHERE `equipe_idequipe`= " + equipe.getId());
                        while (rs.next()) {
                            if (rs.getInt(1) == competition.getSport()) {
                                if (!tableRight.getItems().contains(equipe)) {
                                    tableLeft.getItems().add(equipe);
                                }

                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (Equipe equipe : DataMapManager.getEquipeMap().values()) {

                    if (!tableRight.getItems().contains(equipe)) {
                        tableLeft.getItems().add(equipe);
                    }

                }
            }


        }
    }

}
