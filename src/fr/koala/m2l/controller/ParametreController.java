package fr.koala.m2l.controller;

import fr.koala.m2l.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class ParametreController implements Initializable {


    @FXML
    private TextField textFieldBDDServerName;
    @FXML
    private TextField textFieldBDDName;
    @FXML
    private TextField textFieldBDDUserName;

    @FXML
    private PasswordField textFieldBDDPassWord;
    @FXML
    private Label labelBDDName;
    @FXML
    private Label labelBDDPassWord;
    @FXML
    private Label labelBDDServer;
    @FXML
    private Label labelBDDUserName;
    @FXML
    private Label labelNomAdmin;
    @FXML
    private Label labelPrenomAdmin;
    @FXML
    private Label labelLoginAdmin;
    @FXML
    private Label labelRoleAdmin;
    @FXML
    private Label labelMailAdmin;
    @FXML
    private TableView<Admin> tableAdmin;
    @FXML
    private TableColumn columnNomAdmin;
    @FXML
    private TableColumn columnPrenomAdmin;
    @FXML
    private TextField sportTextField;
    @FXML
    private TableColumn sportIDTable;
    @FXML
    private TableColumn sportNomTable;
    @FXML
    private TableView<Sport> sportTable;
    private SplitMenuButton sportBtnAdd;


    private final Connection conn = ConnectionUtils.getConn();


    //TODO: Besoin de sport dans la competion hors la hash est rempli que dans parametre

    //  PARAMETRE       TAB - Sport

    public void addSport(ActionEvent actionEvent) {
        if (!sportTextField.getText().isEmpty()) {
            String recherche = sportTextField.getText();
            boolean exist = false;
            for (Sport sport : DataMapManager.getSportMap().values()) {

                exist = sport.getNom().getText().equalsIgnoreCase(recherche);
                if (exist) {
                    break;
                }
            }
            if (!exist) {


                try {
                    Statement statement = conn.createStatement();
                    String sql = "INSERT INTO `sport` VALUES (" + null + ", '" + sportTextField.getText() + "')";
                    statement.executeUpdate(sql);
                    Notifications.create()
                            .title("Confirmation")
                            .text("Le sport: " + sportTextField.getText() + " a bien était ajouté.")
                            .showConfirm();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                refreshSport();
            } else {
                Notifications.create()
                        .title("Erreur")
                        .text("Le sport: " + sportTextField.getText() + " exite déjà dans la base de données.")
                        .showWarning();
            }
        }

    }

    public void editSport(ActionEvent actionEvent) {

        refreshSport();
    }

    public void deleteSport(ActionEvent actionEvent) {


        try {
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM `sport` WHERE `sport`.`idsport` = " + sportTable.getSelectionModel().getSelectedItem().getId();
            statement.executeUpdate(sql);
            statement.executeUpdate("DELETE FROM `sportpers` WHERE `sportpers`.`sport_idsport` = " + sportTable.getSelectionModel().getSelectedItem().getId());
            statement.executeUpdate("DELETE FROM `sportequipe` WHERE `sportequipe`.`sport_idsport` = " + sportTable.getSelectionModel().getSelectedItem().getId());
            statement.executeUpdate("UPDATE `competition` SET `id_sport` = '-1' WHERE `competition`.`id_competition` = " + sportTable.getSelectionModel().getSelectedItem().getId());
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshSport();
    }

    public void SportSelected(MouseEvent event) {
        if (sportTable.getSelectionModel().getSelectedItem() != null) {
            String str = sportTable.getSelectionModel().getSelectedItem().getNom().getText();
            sportTextField.setText(str);
        }


    }


    //  PARAMETRE       TAB - Administration

    public void addAdmin(ActionEvent actionEvent) {
        //new DialogNewWin(Admin.class.getDeclaredFields(),FxmlEnum.admin);
//        List<String> list = new ArrayList<>();
//        list.add("Administrateur");
//        list.add("Organisateur");
//        list.add("Utilisateur");
//
//        DialogObjectInteraction.create()
//                .setTitle("tet")
//                .setDescription("des")
//                .addTextField("login","Login")
//                .addTextField("prenom","Prénom")
//                .addTextField("nom","Nom")
//                .addChoiceBox("role","Role",list,list)
//                .addTextField("mail","Mail")
//                .addTextField("mdp","Mot de passe")
//                // .addCheckListView("ok","table","Sport",list)
//                .showAndWaitForAdd("admin",true);
        new DialogNewWin(FxmlEnum.admin);
    }


    public void editAdmin(ActionEvent actionEvent) {
        List<String> list = new ArrayList<>();
        list.add("Administrateur");
        list.add("Organisateur");
        list.add("Utilisateur");
        DialogObjectInteraction.create()
                .setTitle("tet")
                .setDescription("des")
                .addTextField("login", "Login")
                .addTextField("prenom", "Prénom")
                .addTextField("nom", "Nom")
                .addChoiceBox("role", "Role", list, list)
                .addTextField("mail", "Mail")
                .addTextField("mdp", "Mot de passe")
                // .addCheckListView("ok","table","Sport",list)
                .showAndWaitForEdit("admin", "id", String.valueOf(tableAdmin.getSelectionModel().getSelectedItem().getId()), false);

    }

    public void delAdmin(ActionEvent actionEvent) {
        Admin admin = tableAdmin.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        try {
            alert.setTitle("Suppression d'utilisateur supérieur");
            alert.setHeaderText("Etes vous sur de vouloir supprimer l'utilisateur supérieur suivant : ?");
            alert.setContentText(admin.getNom().getText() + " " + admin.getPrenom().getText() + " UUID:[" + admin.getId() + "]");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {

                try {
                    Statement statement = conn.createStatement();
                    int i = admin.getId();
                    String sql = "DELETE FROM `admin` WHERE `admin`.`id` =" + i;
                    statement.executeUpdate(sql);

                    DataMapManager.getPersonneMap().remove(i);
                    tableAdmin.getItems().clear();
                    tableAdmin.getItems().addAll(DataMapManager.getAdminMap().values());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (option.get() == ButtonType.CANCEL) {

            } else {
                // this.label.setText("-");
            }
        } catch (NullPointerException ex) {
            Alert alertt = new Alert(Alert.AlertType.WARNING);
            alertt.setTitle("Suppression d'utilisateur supérieur'");
            alertt.setHeaderText(null);
            alertt.setContentText("Veuillez sélectionner un utilisateur pour le supprimer !");

            alertt.showAndWait();
        }
    }

    public void selectAdmin(MouseEvent event) {
        if (tableAdmin.getSelectionModel().getSelectedItem() != null) {
            Admin admin = tableAdmin.getSelectionModel().getSelectedItem();
            labelLoginAdmin.setText(admin.getLogin());
            labelMailAdmin.setText(admin.getMail());
            labelPrenomAdmin.setText(admin.getPrenom().getText());
            labelNomAdmin.setText(admin.getNom().getText());
            labelRoleAdmin.setText(admin.getRole());

        }


    }


    //  PARAMETRE       TAB - DBB

    public void setBDDData(MouseEvent event) {
        System.out.println(textFieldBDDName.getText());
        System.out.println(textFieldBDDPassWord.getText());
        System.out.println(textFieldBDDServerName.getText());
        System.out.println(textFieldBDDUserName.getText());
        Preferences userPreferences = Preferences.userRoot().node("user/m2l");
        labelBDDName.setText(userPreferences.get("dbusername", ""));
        labelBDDServer.setText(userPreferences.get("dbhost", ""));
        labelBDDUserName.setText(userPreferences.get("dbname", ""));
        labelBDDPassWord.setText("********");

        userPreferences.put("dbusername", textFieldBDDUserName.getText());
        userPreferences.put("dbname", textFieldBDDName.getText());
        userPreferences.put("dbhost", textFieldBDDServerName.getText());
        userPreferences.put("dbpw", textFieldBDDPassWord.getText());

    }

    public void testBDDDate(MouseEvent event) {
        try {
            Connection connectionTest = DriverManager.getConnection("jdbc:mysql://" + textFieldBDDServerName.getText() + ":3306/" + textFieldBDDName.getText() + "?userSSl=false&serverTimezone=UTC", textFieldBDDUserName.getText(), textFieldBDDPassWord.getText());
        } catch (SQLException e) {
            System.out.println("error");
            return;
        }


    }


    //---------------------------------------------------------------------------------------
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences userPreferences = Preferences.userRoot().node("user/m2l");
        labelBDDName.setText(userPreferences.get("dbusername", ""));
        labelBDDServer.setText(userPreferences.get("dbhost", ""));
        labelBDDUserName.setText(userPreferences.get("dbname", ""));
        labelBDDPassWord.setText("********");


        refreshSport();
        refreshAdmin();
        sportIDTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        sportNomTable.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        columnNomAdmin.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnPrenomAdmin.setCellValueFactory(new PropertyValueFactory<>("prenom"));

//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                Dialog dialog = new Dialog();
//                ChoiceBox<Sport> choiceBox = new ChoiceBox<>();
//                for (Sport sport : DataMapManager.getSportMap().values()){
//                    choiceBox.getItems().add(sport);
//                    choiceBox.
//                }
//                dialog.getDialogPane().setContent(choiceBox);
//                dialog.show();
//            }
//        });

    }

    private void refreshSport() {
        DataMapManager.refreshSport();
        sportTable.getItems().clear();

        for (Sport sport : DataMapManager.getSportMap().values()) {
            sportTable.getItems().add(sport);
        }
    }

    private void refreshAdmin() {
        DataMapManager.refreshAdmin();
        tableAdmin.getItems().clear();

        for (Admin admin : DataMapManager.getAdminMap().values()) {
            tableAdmin.getItems().add(admin);
        }
    }


}
