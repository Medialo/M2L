package fr.koala.m2l;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Optional;
import java.util.prefs.Preferences;

public class Main extends Application {

    private static Preferences userPreferences;

    public static Preferences getUserPreferences() {
        return userPreferences;
    }

//    private static String host = preferences.get("dbhost","localhost");
//    private static String dbName = preferences.get("dbname","");
//    private static String dbUserName = preferences.get("dbusername","");
//    private static String dbPassword = preferences.get("dbpw","");

    public static void main(String[] args) {
        userPreferences = Preferences.userRoot().node("user/m2l");
//        System.out.println(userPreferences.get("dbhost",""));
//        System.out.println(userPreferences.get("dbname",""));
//        System.out.println(userPreferences.get("dbusername",""));
//        System.out.println(userPreferences.get("dbpw",""));


        try {
            ConnectionUtils.CreateConnection();
//        Platform.runLater(() -> {
//            Scene scene = new Scene(new GridPane());
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.show();
//
//        });
//            Platform.runLater(() -> {
//                DialogObjectInteraction.crate()
//                        .addTextField("nom", "nom")
//                        .addTextField("prenom", "prénom")
//                        .addTextField("mail", "Email")
//                        .addTextField("adresse", "Adresse")
//                        .addTextField("num", "Numéro de tel")
//                        .showAndWaitForEdit("personne", "id_personne", "1",false);
//            });


            launch(args);


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String s = sw.toString();
            launch("bdderror", s);
        }


    }

//    Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("bonjour");
//            alert.setHeaderText("test2");
//            alert.showAndWait();

    @Override
    public void start(Stage primaryStage) throws Exception {
//        URL url = getClass().getResource("co.fxml");
//        FXMLLoader fxmlLoader = new FXMLLoader(url);
        // System.out.println(getParameters().getRaw());
        if (!getParameters().getRaw().isEmpty()) {
            if (getParameters().getRaw().get(0).equals("bdderror")) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Error: Communications link failure");
                alert.setContentText("La base de données est inaccessible, vérifier vos paramètres internet, ou vérifier les informations de la base de donnée.");
                ButtonType parametre = new ButtonType("Paramètre");

                VBox vBox = new VBox();
                Dialog<ButtonType> dialog = new Dialog<>();
                Label label = new Label("Erreur:");
                TextArea textArea = new TextArea(getParameters().getRaw().get(1));
                vBox.getChildren().addAll(label, textArea);


                // alert.getDialogPane().setContent(vBox);
                alert.getDialogPane().setExpandableContent(vBox);
                alert.getButtonTypes().addAll(parametre);

                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.isPresent() && optional.get() == ButtonType.OK) {
                    Platform.exit();
                } else if (optional.isPresent() && optional.get().equals(parametre)) {
                    Stage stage = new Stage();
                    stage.setTitle("Paramètres BDD");
                    GridPane gridPane = new GridPane();
                    Label host = new Label("Host :");
                    Label dbname = new Label("Nom de la BDD :");
                    Label dbUserName = new Label("Nom d'utilisateur :");
                    Label dbPassWord = new Label("Mot de passe :");
                    TextField hostTextField = new TextField();
                    hostTextField.setPromptText(ConnectionUtils.getHost());
                    hostTextField.setPrefWidth(235);
                    TextField dbnameTextField = new TextField();
                    dbnameTextField.setPromptText(ConnectionUtils.getDbName());
                    dbnameTextField.setPrefWidth(235);
                    TextField dbUserNameTextField = new TextField();
                    dbUserNameTextField.setPromptText(ConnectionUtils.getDbUserName());
                    dbUserNameTextField.setPrefWidth(235);

                    PasswordField dbPassWordTextField = new PasswordField();
                    dbPassWordTextField.setPromptText(ConnectionUtils.getDbPassword());
                    dbPassWordTextField.setPrefWidth(235);
                    gridPane.add(host, 0, 0);
                    gridPane.add(dbname, 0, 1);
                    gridPane.add(dbUserName, 0, 2);
                    gridPane.add(dbPassWord, 0, 3);
                    gridPane.add(hostTextField, 1, 0);
                    gridPane.add(dbnameTextField, 1, 1);
                    gridPane.add(dbUserNameTextField, 1, 2);
                    gridPane.add(dbPassWordTextField, 1, 3);
                    Label informationText = new Label("Une fois les paramètres appliqués, l'application aura besoin d'être relancé.");
                    informationText.setWrapText(true);
                    informationText.setPadding(new Insets(0, 0, 0, 6));
                    VBox boxText = new VBox();
                    gridPane.setPadding(new Insets(10, 10, 10, 10));
                    gridPane.setVgap(6);
                    Label titre = new Label("Paramètre BDD:");
                    titre.setPadding(new Insets(0, 0, 0, 6));
                    titre.setStyle("-fx-font-size: 1.5em;");
                    boxText.getChildren().addAll(titre, gridPane, informationText);
                    HBox buttonBox = new HBox();
                    Button ok = new Button("Appliquer");
                    ok.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            userPreferences.put("dbhost", hostTextField.getText());
                            userPreferences.put("dbname", dbnameTextField.getText());
                            userPreferences.put("dbusername", dbUserNameTextField.getText());
                            userPreferences.put("dbpw", dbPassWordTextField.getText());
                            Platform.exit();
                        }
                    });
                    Button annuler = new Button("Annuler");
                    annuler.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Platform.exit();
                        }
                    });


                    buttonBox.setSpacing(10);
                    buttonBox.setPadding(new Insets(0, 0, 0, 200));
                    buttonBox.getChildren().addAll(ok, annuler);
                    boxText.getChildren().addAll(buttonBox);
                    boxText.setSpacing(8);
                    Scene paraScene = new Scene(boxText, 350, 250);

                    stage.setScene(paraScene);
                    stage.setResizable(false);
                    stage.show();
                }
            }
        } else {
            AnchorPane root = FXMLLoader.load(getClass().getResource("fxml/co.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setResizable(false);
            primaryStage.setTitle("M2L");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        // System.out.println(Arrays.toString(getClass().getDeclaredFields()));
    }
}
