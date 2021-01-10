package fr.koala.m2l.controller;

import fr.koala.m2l.ConnectionUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

@SuppressWarnings("rawtypes")
public class Controller implements Initializable {

    @FXML
    private AnchorPane include;
    @FXML
    private PieChart chart;
    @FXML
    private PieChart chart2;
    @FXML
    private Text prenomAdmin;

    @FXML
    private Text numberOfCompetition;
    @FXML
    private Text numberOfEquipe;
    @FXML
    private Text numberOfCandidats;
    @FXML
    private Text numberOfUsers;

    // private Connection conn;


    @Override
    //refBtnWin
    public void initialize(URL location, ResourceBundle resources) {
        prenomAdmin.setText(CoController.getAdmin().getPrenom().getText());

//        Admin admin = CoController.getAdmin();

        Connection conn = ConnectionUtils.getConn();
        try {
            int i = 0;
            Statement statement = conn.createStatement();
            String sql = "SELECT COUNT(*) FROM personne";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                i = rs.getInt(1);
                numberOfUsers.setText(String.valueOf(rs.getInt(1)));
            }

            sql = "SELECT COUNT(*) FROM equipe";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                numberOfEquipe.setText(String.valueOf(rs.getInt(1)));
            }

            sql = "SELECT COUNT(*) FROM competition";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                numberOfCompetition.setText(String.valueOf(rs.getInt(1)));
            }


            sql = "SELECT DISTINCT COUNT(*)\n" +
                    "FROM (\n" +
                    "    SELECT DISTINCT appartient.personne_id_personne\n" +
                    "FROM appartient\n" +
                    "INNER JOIN personne\n" +
                    "On personne.id_personne=appartient.personne_id_personne\n" +
                    "INNER JOIN equipecandidat\n" +
                    "ON equipecandidat.idequipe=appartient.equipe_idequipe\n" +
                    "UNION\n" +
                    "SELECT DISTINCT personne.id_personne\n" +
                    "FROM personne\n" +
                    "RIGHT JOIN candidat\n" +
                    "on personne.id_personne=candidat.personne_id_personne\n" +
                    "    ) AS t";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                numberOfCandidats.setText(String.valueOf(rs.getInt(1)));
            }


            sql = "SELECT DISTINCT COUNT(appartient.personne_id_personne)\n" +
                    "FROM appartient\n" +
                    "INNER JOIN personne\n" +
                    "On personne.id_personne=appartient.personne_id_personne\n" +
                    "INNER JOIN equipecandidat\n" +
                    "ON equipecandidat.idequipe=appartient.equipe_idequipe\n";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                chart.getData().add(new PieChart.Data("En équipe", rs.getInt(1)));
            }


            sql = "SELECT DISTINCT COUNT(personne.id_personne)\n" +
                    "FROM personne\n" +
                    "RIGHT JOIN candidat\n" +
                    "on personne.id_personne=candidat.personne_id_personne";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                chart.getData().add(new PieChart.Data("Individuel", rs.getInt(1)));
            }

            sql = "SELECT DISTINCT COUNT(*)\n" +
                    "FROM appartient";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                chart2.getData().add(new PieChart.Data("En équipe", rs.getInt(1)));
                chart2.getData().add(new PieChart.Data("Sans équipe", i - rs.getInt(1)));
            }


//            String sql = "SELECT COUNT(*) FROM personne";
//            ResultSet rs = statement.executeQuery(sql);
//            while (rs.next()){
//                numberOfUsers.setText(String.valueOf(rs.getInt(1)));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
