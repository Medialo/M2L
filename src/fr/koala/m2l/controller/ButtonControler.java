package fr.koala.m2l.controller;

import fr.koala.m2l.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ButtonControler implements Initializable {


//-fx-background-color:        #38c798          #2C9170


    private static final Admin admin = CoController.getAdmin();
    @FXML
    private Button btnParametre;
    @FXML
    private Button btnUsers;
    @FXML
    private Button btnCompetitions;
    @FXML
    private Button btnEquipes;
    //-- ref == accueil
    @FXML
    private Button refBtnWin;
    @FXML
    private Text prenomText;
    @FXML
    private Text roleText;

    @FXML
    private void onBtnAccueilPressed(MouseEvent event) {
        changeFxmlWindows(FxmlEnum.accueil);
    }

    @FXML
    private void onBtnEquipePressed(MouseEvent event) {
        changeFxmlWindows(FxmlEnum.equipe);
    }

    @FXML
    private void onBtnCompPressed(MouseEvent event) {
        changeFxmlWindows(FxmlEnum.competition);
    }

    @FXML
    private void onBtnUsersPressed(MouseEvent event) {
        changeFxmlWindows(FxmlEnum.users);
    }

    @FXML
    private void onBtnParamPressed(MouseEvent event) {
        changeFxmlWindows(FxmlEnum.parametre);
    }

    private void changeFxmlWindows(FxmlEnum fxmlEnum) {
        try {

            Stage stage;
            Parent root;
            stage = (Stage) refBtnWin.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource(fxmlEnum.getFxmlPath()));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void changeFxmlWindows2(FxmlEnum fxmlEnum){
//        try {
//
//            AnchorPane root = (AnchorPane) refBtnWin.getScene().getWindow().getOnShowing();
//            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlEnum.getFxmlPath()));
//
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        prenomText.setText(admin.getPrenom().getText() + " " + admin.getNom().getText());
        roleText.setText(admin.getRole());
        if (!admin.getRolePerms().isParameters()) {
            btnParametre.setDisable(true);
        }

    }
}
