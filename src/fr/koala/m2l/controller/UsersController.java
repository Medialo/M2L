//https://o7planning.org/fr/11529/tutoriel-javafx-alert-dialog
package fr.koala.m2l.controller;

import fr.koala.m2l.ConnectionUtils;
import fr.koala.m2l.DataMapManager;
import fr.koala.m2l.DialogObjectInteraction;
import fr.koala.m2l.Personne;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@SuppressWarnings("rawtypes")
public class UsersController implements Initializable {


    @FXML
    private Button btn;
    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    @FXML
    private Label labelUserSport;
    private Connection conn;


    @FXML
    private TableView<Personne> personTablee;
    @FXML
    private TableColumn firstNameColumne;
    @FXML
    private TableColumn lastNameColumne;
    @FXML
    private Label nomLabel;
    @FXML
    private Label prenomLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label mailLabel;
    @FXML
    private Label numLabel;
    @FXML
    private Label idLabel;
    @FXML
    private TextField usersRecherche;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!CoController.getAdmin().getRolePerms().isUsers()) {
            btn.setDisable(true);
            btn1.setDisable(true);
            btn2.setDisable(true);
        }
        DataMapManager.refreshAll();
        this.conn = ConnectionUtils.getConn();
        onRechercherUpdate();
//        try {
//            Statement statement = conn.createStatement();
//            String sql = "Select * from personne";
//            ResultSet rs = statement.executeQuery(sql);
//            while (rs.next()){
//               personneMap.put(rs.getInt(1),new Personne(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
//            }
//            rs.close();
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        firstNameColumne.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        lastNameColumne.setCellValueFactory(new PropertyValueFactory<>("prenom"));
//        for (Personne p : personneMap.values()) {
//            personTablee.getItems().add(p);
//        }

    }

    public void tableUserSelection(MouseEvent event) {

        try {
            Personne personne = personTablee.getSelectionModel().getSelectedItem();
            nomLabel.setText(personne.getNom().getText());
            prenomLabel.setText(personne.getPrenom().getText());
            adresseLabel.setText(personne.getAdresse());
            mailLabel.setText(personne.getMail());
            numLabel.setText(personne.getNum());
            idLabel.setText(personne.getId().toString());

            try {
//                DataMapManager.refreshSport();
                String str = "";
                Statement statement = ConnectionUtils.getConn().createStatement();
                ResultSet rs = statement.executeQuery("SELECT `sport_idsport` FROM `sportpers` WHERE `personne_id_personne` = " + personne.getId());
                while (rs.next()) {
                    str = str.concat(DataMapManager.getSportMap().get(rs.getInt(1)).getNom().getText()) + "\n";
                }

                if (str.isEmpty()) {
                    labelUserSport.setText("Aucun sport");
                } else {
                    labelUserSport.setText(str);
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void delUser(MouseEvent event) {
        Personne personne = personTablee.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        try {
            alert.setTitle("Suppression d'utilisateur");
            alert.setHeaderText("Etes vous sur de vouloir supprimer l'utilisateur suivant : ?");
            alert.setContentText(personne.getNom().getText() + " " + personne.getPrenom().getText() + " UUID:[" + personne.getId() + "]");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {

                try {
                    Statement statement = conn.createStatement();
                    int i = personne.getId();
                    String sql = "DELETE FROM `personne` WHERE `personne`.`id_personne` = " + i;//TODO: Supprimer aussi le sport de l'utilsaateur et de l'équipe lord du delete!
                    statement.executeUpdate(sql);
                    statement.executeUpdate("DELETE FROM `sportpers` WHERE `sportpers`.`personne_id_personne` = " + i);
                    statement.executeUpdate("DELETE FROM `candidat` WHERE `candidat`.`personne_id_personne` = " + i);
                    statement.executeUpdate("DELETE FROM `appartient` WHERE `appartient`.`personne_id_personne` = " + i);
                    DataMapManager.getPersonneMap().remove(i);
                    personTablee.getItems().clear();
                    personTablee.getItems().addAll(DataMapManager.getPersonneMap().values());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (option.get() == ButtonType.CANCEL) {

            } else {
                // this.label.setText("-");
            }
        } catch (NullPointerException ex) {
            Alert alertt = new Alert(Alert.AlertType.WARNING);
            alertt.setTitle("Suppression utilisateur");
            alertt.setHeaderText(null);
            alertt.setContentText("Veuillez sélectionner un utilisateur pour le supprimer !");

            alertt.showAndWait();
        }


    }


    public void onRechercherUpdate(/*KeyEvent keyEvent*/) {
//        for (Personne p : personneMap.values()) {
//            personTablee.getItems().add(p);
//        }
        personTablee.getItems().clear();
        DataMapManager.getPersonneMap().clear();
        String str = usersRecherche.getText().toLowerCase();
        try {
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM personne WHERE `prenom` like \"%" + str + "%\" OR `nom` LIKE \"%" + str + "%\";";
            ResultSet rs = statement.executeQuery(sql);
            //System.out.println(usersRecherche.getText());

            while (rs.next()) {
                DataMapManager.getPersonneMap().put(rs.getInt(1), new Personne(rs.getInt(1), new Text(rs.getString(2)), new Text(rs.getString(3)), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Personne p : DataMapManager.getPersonneMap().values()) {
            if (!str.isEmpty() && p.getNom().getText().toLowerCase().contains(str)) {
                p.getNom().setStyle("-fx-font-weight: bold");
            }
            if (!str.isEmpty() && p.getPrenom().getText().toLowerCase().contains(str)) {
                p.getPrenom().setStyle("-fx-font-weight: bold");
            }
            //  p.getPrenom().setStyle("-fx-font-weight: bold");
            personTablee.getItems().add(p);
        }


    }

    public void addUser(MouseEvent mouseEvent) {
        // new DialogNewWin(Personne.class.getDeclaredFields(),FxmlEnum.users);
        Pair<List<String>, List<String>> pair = DataMapManager.getSportListsDataPair();
        DialogObjectInteraction.create()
                .setTitle("Ajout")
                .setDescription("Ajout d'utilisateur")
                .addTextField("nom", "Nom")
                .addTextField("prenom", "Prénom")
                .addTextField("mail", "Mail")
                .addTextField("adresse", "Adresse")
                .addTextField("num", "Numéros")
                .addCheckListView("sport_idsport", "sportpers", "personne_id_personne", "Sport(s)", pair.getValue(), pair.getKey())
                .showAndWaitForAdd("personne", true);
        onRechercherUpdate();
    }

    public void editUser(MouseEvent event) {


        Personne personne = personTablee.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        try {
            Pair<List<String>, List<String>> pair = DataMapManager.getSportListsDataPair();
            DialogObjectInteraction.create()
                    .setDescription("Modification d'utilisateur")
                    .setTitle("Modification")
                    .addTextField("nom", "Nom", personne.getNom().getText())
                    .addTextField("prenom", "Prénom", personne.getPrenom().getText())
                    .addTextField("mail", "Mail", personne.getMail())
                    .addTextField("adresse", "Adresse", personne.getAdresse())
                    .addTextField("num", "Numéros", personne.getNum())
                    .addCheckListView("sport_idsport", "sportpers", "personne_id_personne", "Sport(s)", pair.getValue(), pair.getKey(), true)
                    .showAndWaitForEdit("personne", "id_personne", String.valueOf(personTablee.getSelectionModel().getSelectedItem().getId()), false);

        } catch (NullPointerException ex) {
            Alert alertt = new Alert(Alert.AlertType.WARNING);
            alertt.setTitle("Modification d'utilisateur");
            alertt.setHeaderText(null);
            alertt.setContentText("Veuillez selectioner un utilisateur pour le modifier !");

            alertt.showAndWait();
        }
        onRechercherUpdate();
    }
}