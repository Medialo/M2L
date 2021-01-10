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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CompetitionControler implements Initializable {


    @FXML
    private Button btn;
    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    @FXML
    private Button btn3;
    @FXML
    private Label labelNom;
    @FXML
    private Label labelDateDebut;
    @FXML
    private Label labelDateFin;
    @FXML
    private Label labelDateFinInscription;
    @FXML
    private Label labelType;
    @FXML
    private Label labelSport;
    @FXML
    private Button test;//jsp
    @FXML
    private TableColumn nomCompTable;
    @FXML
    private TableColumn sportCompTable;
    @FXML
    private TableColumn typeCompTable;
    @FXML
    private TableView<Competition> compTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // System.out.println(CoController.getAdmin().getRolePerms());
        if (!CoController.getAdmin().getRolePerms().isCompetition()) {
            btn.setDisable(true);
            btn1.setDisable(true);
            btn2.setDisable(true);
            btn3.setDisable(true);
        }

//              Tooltip tooltip =  new Tooltip("Button of doom");
//                test.setTooltip(tooltip);


        DataMapManager.refreshAll();


        nomCompTable.setCellValueFactory(new PropertyValueFactory<>("nom"));
//        nomCompTable.setCellFactory(param -> new TableCell<Sport,String>(){
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (!empty){
//
//                }
//            }
//
//        });
        sportCompTable.setCellValueFactory(new PropertyValueFactory<>("sport"));
        sportCompTable.setCellFactory(param -> new TableCell<Sport, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    if (item == -1) {
                        setText("Non renseigné");
                    } else {
                        setText((DataMapManager.getSportMap().getOrDefault(item,new Sport(-1,new Text("Sport supprimé"))).getNom().getText()));
                    }
                }
            }

        });

        typeCompTable.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCompTable.setCellFactory(param -> new TableCell<Sport, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Oui" : "Non");
            }
        });
        compTable.getItems().addAll(DataMapManager.getCompetitionMap().values());

//        DataMapManager.getCompetitionMap().values().forEach(comp -> {
//            System.out.println(comp.getNom().getText() + " " +comp.getSport());
//        });


    }

    public void editComp(MouseEvent event) {


        Competition competition = compTable.getSelectionModel().getSelectedItem();
        if (competition == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Erreur");
            alert.setContentText("Aucune équipe sélectionnée");
            alert.showAndWait();
        } else {
            Pair<List<String>, List<String>> pair = DataMapManager.getSportListsDataPair();
            String str = null;
            try {
                str = DataMapManager.getSportMap().get(competition.getSport()).getNom().getText();
            } catch (NullPointerException ignored) {

            }


            DialogObjectInteraction.create()
                    .setTitle("Edition")
                    .setDescription("Modification d'équipe")
                    .addTextField("nom", "Nom", competition.getNom().getText())
                    .addDatePicker("date", "datefin", "Debut compétition", "Fin competition", "La compétition durera", competition.getDate(), competition.getDateFin())
                    .addDatePicker("date_cloture_inscr", "Date fin d'inscription")
                    .addCheckBox("individuel", "Type", "Equipe", "Individuel", competition.getType())
                    .addChoiceBox("id_sport", "Sport", str, pair.getValue(), pair.getKey())

                    .showAndWaitForEdit("competition", "id_competition", String.valueOf(competition.getId()), false);
        }

        compTable.getItems().clear();
        compTable.getItems().addAll(DataMapManager.getCompetitionMap().values());
        compTable.refresh();

    }

    public void addComp(MouseEvent event) {
        Pair<List<String>, List<String>> pair = DataMapManager.getSportListsDataPair();

//        boolean sport = false;
//        try {
//            Statement statement = ConnectionUtils.getConn().createStatement();
//            String sql = "SELECT COUNT(*) FROM `sport`";
//            ResultSet resultSet = statement.executeQuery(sql);
//            while (resultSet.next()) {
//                sport = resultSet.getInt(1) == 0;
//
//            }
//            statement.close();
//            resultSet.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        if(pair.getKey().size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Erreur");
            alert.setContentText("Vous devez déjà ajouter des sports dans la liste !");
            alert.showAndWait();
        } else {
            DialogObjectInteraction.create()
                    .setTitle("Ajout")
                    .setDescription("Ajout d'équipe")
                    .addTextField("nom", "Nom")
                    .addDatePicker("date", "datefin", "Debut compétition", "Fin competition", "La compétition durera")
                    .addDatePicker("date_cloture_inscr", "Date fin d'inscription")
                    .addCheckBox("individuel", "Type", "Equipe", "Individuel", false)
                    .addChoiceBox("id_sport", "Sport", pair.getValue(), pair.getKey())
                    // .addCheckListView("ok","table","Sport",list)
                    .showAndWaitForAdd("competition", true);
            compTable.getItems().clear();
            DataMapManager.refreshCompetition();
            compTable.getItems().addAll(DataMapManager.getCompetitionMap().values());
            compTable.refresh();
        }


    }

    public void delComp(MouseEvent event) {
        Competition competition = compTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        try {
            alert.setTitle("Suppression de compétition");
            alert.setHeaderText("Etes vous sur de vouloir supprimer la compétition suivante : ?");
            alert.setContentText(competition.getNom().getText() + " UUID:[" + competition.getId() + "]");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                //System.out.println("ok");
                try {
                    Statement statement = ConnectionUtils.getConn().createStatement();
                    int i = competition.getId();
                    String sql = "DELETE FROM `competition` WHERE `competition`.`id_competition` = " + i;
                    statement.executeUpdate(sql);
                    if (competition.getType()) {
                        statement.executeUpdate("DELETE FROM `candidat` WHERE `candidat`.`competition_id_competition` = " + i);
                    } else {
                        statement.executeUpdate("DELETE FROM `equipecandidat` WHERE `equipecandidat`.`idcompetition` = " + i);
                    }

                    statement.close();
                    DataMapManager.getCompetitionMap().remove(i);
                    compTable.getItems().clear();
                    compTable.getItems().addAll(DataMapManager.getCompetitionMap().values());
                    compTable.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (option.get() == ButtonType.CANCEL) {

            } else {
                // this.label.setText("-");
            }
        } catch (NullPointerException ex) {
            Alert alertt = new Alert(Alert.AlertType.WARNING);
            alertt.setTitle("Suppression de compétition");
            alertt.setHeaderText(null);
            alertt.setContentText("Veuillez sélectionner une compétition pour la supprimer !");

            alertt.showAndWait();
        }
        //    onRechercherUpdate();
    }

    public void manComp(MouseEvent event) {


        if (compTable.getSelectionModel().getSelectedItem() != null) {
            ManangeCompetitionControler.setCompetition(compTable.getSelectionModel().getSelectedItem());
            Dialog dialog = new Dialog();
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("../fxml/manCom.fxml"));
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


//        Stage dialog = new Stage();
//        try {
//            Parent parent = FXMLLoader.load(getClass().getResource("../fxml/manCom.fxml"));
//            Scene root = new Scene(parent);
//            dialog.setScene(root);
//
////            dialog.getDialogPane().getButtonTypes()
//            dialog.showAndWait();


    }

    public void tableCompSelection(MouseEvent event) {
        try {
            Competition competition = compTable.getSelectionModel().getSelectedItem();
            labelNom.setText(competition.getNom().getText());
            labelDateDebut.setText(competition.getDate().toString());
            labelDateFin.setText(competition.getDateFin().toString());
            labelDateFinInscription.setText(competition.getDateClotureInscription().toString());
            labelType.setText(competition.getTypeStringComplet());
            try {
                labelSport.setText(DataMapManager.getSportMap().get(competition.getSport()).getNom().getText());
            } catch (Exception ignored) {
                labelSport.setText("Non renseigné");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
