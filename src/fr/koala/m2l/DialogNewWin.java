package fr.koala.m2l;

import fr.koala.m2l.controller.FxmlEnum;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public final class DialogNewWin {
    private Connection conn;
    private TextField[] tfTab;
    private TextField mdpTf;
    private TextField loginTextField;
    private TextField nomTextField;
    private TextField prenomTextField;
    private final String[] tabLogin = {"", ""};


    private DialogNewWin() {
    }

//
//    public String[] dialogGetData(){
//        String[] listData = new  String[tfTab.length];
//        for(int i = 0; i < tfTab.length;i++){
//            listData[i]=tfTab[i].getText();
//        }
//        return listData;
//    }

    /**
     * @param fxmlEnum
     */
    @Deprecated
    public DialogNewWin(FxmlEnum fxmlEnum) {
        conn = ConnectionUtils.getConn();
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        String[] strTab = {"ID", "Login", "Prenom", "Nom", "Role", "mail", "mdp"};

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter...");
        dialog.setHeaderText("test");

        ButtonType addButton = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        tfTab = new TextField[strTab.length];
        for (int i = 0; i < strTab.length; i++) {
            if (strTab[i].equalsIgnoreCase("role")) {
                choiceBox = new ChoiceBox<>();
                choiceBox.getItems().addAll("Administrateur", "Organisateur", "Utilisateur");
                choiceBox.setValue("Administrateur");
                grid.add(new Label(strTab[i] + ": "), 0, i);
                grid.add(choiceBox, 1, i);
                tfTab[i] = new TextField();
            } else {


                TextField textField = new TextField();
                textField.setPrefWidth(250);
                textField.setPromptText(strTab[i]);
                if (strTab[i].equalsIgnoreCase("id")) {
                    textField.setEditable(false);
                    textField.setText("ID automatique");
                }

                if (fxmlEnum.equals(FxmlEnum.admin)) {
                    if (strTab[i].contains("role")) {
                        textField.setEditable(false);
                        textField.setText("Administrateur");
                    }
                    if (strTab[i].equalsIgnoreCase("mdp")) {
                        mdpTf = textField;
                        textField.setEditable(false);
                        textField.setText("Mot de passe automatique");
                    }
                    if (strTab[i].equalsIgnoreCase("login")) {
                        loginTextField = textField;
                        textField.setEditable(false);
                        textField.setText("Login automatique");
                    }
                    if (strTab[i].equalsIgnoreCase("nom")) {
                        nomTextField = textField;
                        nomTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                //String str = prenomTextField.getText().toLowerCase().substring(0,1) + "" + nomTextField.getText().toLowerCase().substring(1, nomTextField.getText().length());
                                tabLogin[0] = nomTextField.getText().toLowerCase();
                                loginTextField.setText(tabLogin[1] + tabLogin[0]);
                                mdpTf.setText(tabLogin[1] + tabLogin[0]);
                            }
                        });
                    }
                    if (strTab[i].equalsIgnoreCase("prenom")) {
                        prenomTextField = textField;
                        prenomTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                if (prenomTextField.getText().length() >= 1) {
                                    tabLogin[1] = prenomTextField.getText().toLowerCase().substring(0, 1);
                                }
                                loginTextField.setText(tabLogin[1] + tabLogin[0]);
                                mdpTf.setText(tabLogin[1] + tabLogin[0]);

                                //String str = prenomTextField.getText().toLowerCase().substring(0,1) + "" + nomTextField.getText().toLowerCase().substring(1, nomTextField.getText().length());

                            }
                        });
                    }

                }

                //-------------


                grid.add(new Label(strTab[i] + ": "), 0, i);
                grid.add(textField, 1, i);
                tfTab[i] = textField;
                //grid.add(null,0,0);
            }

        }
        // System.out.println(grid.getChildren().size());
        dialog.getDialogPane().setContent(grid);
        boolean empty = false;
        do {
            if (empty) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Erreur champ");
                alert.setContentText("Un de vos champs est vide !");
                alert.setTitle("Attention");
                alert.showAndWait();
            }
            empty = false;
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == addButton) {
                tfTab[4].setText(choiceBox.getValue());
                int i = 0;
                for (TextField tf : tfTab) {

                    if (!tf.getText().equals("ID automatique") && (tf.getText().isEmpty() || tf.getText() == null)) {
                        System.out.println(i);
                        empty = true;

                    }
                    i++;
                }

                if (!empty) {

                    try {
                        String sql = "INSERT INTO `" + fxmlEnum.getSql() + "` VALUES (" + null + ",";
                        Statement statement = conn.createStatement();
                        for (int j = 1; j < tfTab.length - 1; j++) {
                            sql = sql.concat("'" + tfTab[j].getText() + "',");
                        }
                        sql = sql.concat("'" + tfTab[tfTab.length - 1].getText() + "')");

                        statement.executeUpdate(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
//                    for(int i = 1; i < tfTab.length; i++){
//                        System.out.println(tfTab[i].getText());
//                    }
                }
            }
        } while (empty);

    }


}
