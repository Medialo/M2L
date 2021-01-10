//package fr.koala.m2l.controller;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//
//public abstract class ChangeFxml {
//
//
//    @FXML
//    private void onBtnAccueilPressed(MouseEvent event) {
//        changeFxmlWindows(FxmlEnum.accueil);
//    }
//
//    @FXML
//    private void onBtnEquipePressed(MouseEvent event) {
//        changeFxmlWindows(FxmlEnum.equipe);
//    }
//
//    @FXML
//    private void onBtnCompPressed(MouseEvent event) {
//        changeFxmlWindows(FxmlEnum.competition);
//    }
//
//    @SuppressWarnings("unchecked")
//    @FXML
//    private void onBtnUsersPressed(MouseEvent event) { changeFxmlWindows(FxmlEnum.users); }
//
//    @FXML
//    private void onBtnParamPressed(MouseEvent event) {
//        changeFxmlWindows(FxmlEnum.parametre);
//    }
//
//
//    private void changeFxmlWindows(FxmlEnum fxmlEnum) {
//        try {
//            Stage stage;
//            Parent root;
//            stage = (Stage) refBtnWin.getScene().getWindow();
//            root = FXMLLoader.load(getClass().getResource(fxmlEnum.getFxmlPath()));
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
