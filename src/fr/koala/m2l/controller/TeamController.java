package fr.koala.m2l.controller;

import fr.koala.m2l.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class TeamController implements Initializable {

    @FXML
    private Button btn3;
    @FXML
    private Button btn;
    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    @FXML
    private Label labelTeamName;
    @FXML
    private Label labelSportTeam;
    private Connection conn;

    @FXML
    private Button refBtnWin;
    @FXML
    private TextField usersRecherche;

    @FXML
    private TableView<Equipe> teamTable;
    @FXML
    private TableColumn equipeNameColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!CoController.getAdmin().getRolePerms().isTeam()) {
            btn.setDisable(true);
            btn1.setDisable(true);
            btn2.setDisable(true);
            btn3.setDisable(true);
        }

        DataMapManager.refreshAll();
        this.conn = ConnectionUtils.getConn();
        onRechercherUpdate();

        equipeNameColumn.setCellValueFactory(new PropertyValueFactory<>("Nom"));
    }

    public void onRechercherUpdate(/*KeyEvent keyEvent*/) {
//        for (Personne p : personneMap.values()) {
//            personTablee.getItems().add(p);
//        }
        teamTable.getItems().clear();
        DataMapManager.getEquipeMap().clear();
        String str = usersRecherche.getText().toLowerCase();
        try {
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM equipe WHERE `nom` like \"%" + str + "%\" OR `idequipe` LIKE \"%" + str + "%\";";
            ResultSet rs = statement.executeQuery(sql);
            //System.out.println(usersRecherche.getText());

            while (rs.next()) {
                //Changer le new!!!
                DataMapManager.getEquipeMap().put(rs.getInt(1), new Equipe(rs.getInt(1), new Text(rs.getString(2))));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Equipe p : DataMapManager.getEquipeMap().values()) {
            if (!str.isEmpty() && p.getNom().getText().toLowerCase().contains(str)) {
                p.getNom().setStyle("-fx-font-weight: bold");
            }
            if (!str.isEmpty() && String.valueOf(p.getId()).contains(str)) {
                p.getNom().setStyle("-fx-font-weight: bold");
            }
            //  p.getPrenom().setStyle("-fx-font-weight: bold");
            teamTable.getItems().add(p);
        }


    }

    public void tableUserSelection(MouseEvent event) {
        try {
            Equipe team = teamTable.getSelectionModel().getSelectedItem();
            labelTeamName.setText(team.getNom().getText());

            try {
                String str = "";
                Statement statement = ConnectionUtils.getConn().createStatement();
                ResultSet rs = statement.executeQuery("SELECT `sport_idsport` FROM `sportequipe` WHERE `equipe_idequipe` = " + team.getId());
                while (rs.next()) {
                    str = str.concat(DataMapManager.getSportMap().get(rs.getInt(1)).getNom().getText()) + "\n";
                }
                if (str.isEmpty()) {
                    labelSportTeam.setText("Aucun sport");
                } else {
                    labelSportTeam.setText(str);
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

//            adresseLabel.setText(personne.getAdresse());
//            mailLabel.setText(personne.getMail());
//            numLabel.setText(personne.getNum());
//            idLabel.setText(personne.getId().toString());
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }


    public void addTeam(MouseEvent mouseEvent) {

        List<String> list = new ArrayList<>();
        List<String> listID = new ArrayList<>();
        for (Sport sport : DataMapManager.getSportMap().values()) {
            list.add(sport.getNom().getText());
            listID.add(String.valueOf(sport.getId()));

        }

        // new DialogNewWin(Equipe.class.getDeclaredFields(),FxmlEnum.equipe);

        DialogObjectInteraction.create()
                .setTitle("Ajout")
                .setDescription("Ajout d'équipe")
                .addTextField("nom", "Nom de l'équipe")
                .addCheckListView("sport_idsport", "sportequipe", "equipe_idequipe", "Sport de l'équipe", list, listID)
                .saveForm("equipe")
                .showAndWaitForAdd("equipe", true);

        onRechercherUpdate();
    }

    public void editTeam(MouseEvent event) {

        Pair<List<String>, List<String>> pair = DataMapManager.getSportListsDataPair();

        if (teamTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Erreur");
            alert.setContentText("Aucune équipe sélectionnée");
            alert.showAndWait();
        } else {
            DialogObjectInteraction.create()
                    .setTitle("Edition")
                    .setDescription("Edition d'équipe")
                    .addTextField("nom", "Nom de l'équipe", teamTable.getSelectionModel().getSelectedItem().getNom().getText())
                    .addCheckListView("sport_idsport", "sportequipe", "equipe_idequipe", "Sport de l'équipe", pair.getValue(), pair.getKey())
                    .showAndWaitForEdit("equipe", "idequipe", String.valueOf(teamTable.getSelectionModel().getSelectedItem().getId()), false);
        }
        onRechercherUpdate();
    }

    public void delTeam(MouseEvent event) {

        Equipe equipe = teamTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        try {
            alert.setTitle("Suppression d'équipe");
            alert.setHeaderText("Etes vous sur de vouloir supprimer l'équipe suivante : ?");
            alert.setContentText(equipe.getNom().getText() + " " + equipe.getNom().getText() + " UUID:[" + equipe.getId() + "]");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                //System.out.println("ok");
                try {
                    Statement statement = conn.createStatement();
                    int i = equipe.getId();
                    statement.executeUpdate("DELETE FROM `equipe` WHERE `equipe`.`idequipe` = " + i);
                    statement.executeUpdate("DELETE FROM `sportequipe` WHERE `sportequipe`.`equipe_idequipe` = " + i);
                    statement.close();
                    DataMapManager.getEquipeMap().remove(i);
                    teamTable.getItems().clear();//TODO: Verifier le coppier coller de delXXX pour la corspondance avec les bonnes maps
                    teamTable.getItems().addAll(DataMapManager.getEquipeMap().values());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (option.get() == ButtonType.CANCEL) {

            } else {
                // this.label.setText("-");
            }
        } catch (NullPointerException ex) {
            Alert alertt = new Alert(Alert.AlertType.WARNING);
            alertt.setTitle("Suppression d'équipe");
            alertt.setHeaderText(null);
            alertt.setContentText("Veuillez sélectionner une équipe pour la supprimer !");

            alertt.showAndWait();
        }
        // onRechercherUpdate();
    }

    public void manTeam(MouseEvent event) {

        if (teamTable.getSelectionModel().getSelectedItem() != null) {
            ManangeEquipeControler.setEquipe(teamTable.getSelectionModel().getSelectedItem());
            Dialog dialog = new Dialog();
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("../fxml/manEquipe.fxml"));
                dialog.getDialogPane().setContent(parent);

//            dialog.getDialogPane().getButtonTypes()
                dialog.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.showAndWait();


        }
    }
}