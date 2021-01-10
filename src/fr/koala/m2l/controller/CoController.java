package fr.koala.m2l.controller;

import fr.koala.m2l.Admin;
import fr.koala.m2l.ConnectionUtils;
import fr.koala.m2l.DialogNewWin;
import fr.koala.m2l.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class CoController implements Initializable {


    static private Admin adminObject;
    private final Connection connection = ConnectionUtils.getConn();
    private boolean admin;
    @FXML
    private PasswordField userPassword;
    @FXML
    private TextField userLogin;
    @FXML
    private Button btn;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Label helpMessage;

    static public Admin getAdmin() {
        return adminObject;
    }

    @FXML
    private void userConnection(MouseEvent mouseEvent) {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT COUNT(*) FROM `admin` WHERE `role` = \"Administrateur\"";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                admin = resultSet.getInt(1) != 0;

            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (userLogin.getText().equals("root") && userPassword.getText().equals("root")) {
            if (!admin) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Connexion Root!");
                alert.setContentText("La connexion root permet uniquement de créer un utilisateur administrateur. " +
                        "Après la création du nouvel utilisateur, l'accés root sera bloqué !");
                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                    new DialogNewWin(FxmlEnum.admin);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Connexion root");
                alert.setContentText("La connexion root a déjà était utilisé ! Merci de vous connecter avec vos identifiants.");
                alert.show();
            }


//        } else  if (userLogin.getText().equals("") && userPassword.getText().equals("")) { //TODO: Doit être enlever avant la publication
//            adminObject = new Admin(new Text("null"),new Text("null"),"null",-1);
//            try {
//                Stage stage;
//                Parent root;
//                stage = (Stage) btn.getScene().getWindow();
//                root = FXMLLoader.load(getClass().getResource("../fxml/acc.fxml"));
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//
//            } catch (Exception e){
//                e.printStackTrace();
//            }
        } else {
            loading.setVisible(true);
            helpMessage.setVisible(false);

            String mdp = userPassword.getText();
            boolean equal = false;
            try {
                Statement statement = connection.createStatement();
                String sql = "SELECT mdp FROM `admin` WHERE login=\"" + userLogin.getText() + "\";";
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    equal = rs.getString(1).equals(mdp);
                }
                rs.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            if (equal) {

                try {
                    Statement statement = connection.createStatement();
                    String sql = "SELECT `prenom`,`nom`,`role`,`id` FROM `admin` WHERE login=\"" + userLogin.getText() + "\";";
                    ResultSet rs = statement.executeQuery(sql);
                    while (rs.next()) {
                        adminObject = new Admin(new Text(rs.getString(1)), new Text(rs.getString(2)), rs.getString(3), rs.getInt(4));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Notifications.create()
                        .title("Bienvenue")
                        .text(adminObject.getPrenom().getText() + " " + adminObject.getNom().getText() + "     " + adminObject.getRole())
                        .showInformation();
                try {
                    Stage stage;
                    Parent root;
                    stage = (Stage) btn.getScene().getWindow();
                    root = FXMLLoader.load(Main.class.getResource("fxml/acc.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userLogin.getText().equalsIgnoreCase(userPassword.getText())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Bienvenue sur votre interface de gestion M2L. Avant de commencer veuillez changer votre mot de passe.");
                    alert.setHeaderText("Bienvenue");
                    Optional<ButtonType> r = alert.showAndWait();
                    if (r.isPresent() && r.get() == ButtonType.OK) {
                        TextInputDialog textInputDialog = new TextInputDialog();
                        textInputDialog.setHeaderText("");
                        textInputDialog.setTitle("Modification mot de passe");
                        textInputDialog.setContentText("Nouveau mot de passe :");
                        textInputDialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
                        Optional<String> r2 = textInputDialog.showAndWait();
                        if (r2.isPresent()) {
                            try {
                                Statement statement = connection.createStatement();
                                statement.executeUpdate("UPDATE `admin` SET `mdp` = '" + r2.get() + "' WHERE `admin`.`id` = " + adminObject.getId());
                                statement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            } else {
                loading.setVisible(false);
                helpMessage.setVisible(true);
                Notifications.create()
                        .title("Erreur")
                        .text("Votre login ou votre mot de passe est faux")
                        .showWarning();

            }


        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loading.setVisible(false);
    }
}
