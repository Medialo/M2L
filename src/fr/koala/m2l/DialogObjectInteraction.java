package fr.koala.m2l;

import fr.koala.m2l.exception.FormMapEmptyException;
import fr.koala.m2l.exception.FormNotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Pair;
import org.controlsfx.control.CheckListView;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.*;

public class DialogObjectInteraction {


    private static Map<String, DialogObjectInteraction> mapSaveFormForLater;
    private final Dialog dialog = new Dialog();
    private GridPane grid = new GridPane();
    private final Map<String, TextField> mapTextField = new HashMap<>();
    private final Map<String, Pair<List<String>, ChoiceBox<String>>> mapChoiceBox = new HashMap<>();
    private final Map<String, DatePicker> mapDatePicker = new HashMap<>();
    private final Map<String, CheckBox> mapCheckBox = new HashMap<>();
    private Map<Pair<String, String>, Pair<List<String>, CheckListView<String>>> mapCheckListView;
    //private String[] dataSqlCheckListView; // = new String[3]  0 = table    1 = sql  2 = id


    private DialogObjectInteraction() {
        grid.getChildren().clear();
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
    }


    public static DialogObjectInteraction create() {
        return new DialogObjectInteraction();
    }

    public static DialogObjectInteraction getForm(String key) throws FormMapEmptyException, FormNotFoundException {
        if (mapSaveFormForLater == null) {
            throw new FormMapEmptyException();
        } else if (mapSaveFormForLater.get(key) == null) {
            throw new FormNotFoundException(key);
        } else {
            return mapSaveFormForLater.get(key);
        }

    }

    public static Integer getKeys(Map<Integer, Sport> map, String value) {
        for (Map.Entry<Integer, Sport> entry : map.entrySet()) {
            if (entry.getValue().getNom().getText().equalsIgnoreCase(value)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public DialogObjectInteraction saveForm(String key) {
        if (mapSaveFormForLater == null) {
            mapSaveFormForLater = new HashMap<>();
        }
        mapSaveFormForLater.put(key, this);
        return this;
    }

    public DialogObjectInteraction addCheckListView(String key, String table, String id, String label, List<String> labels, List<String> values) {
        addCheckListView(key, table, id, label, labels, values, false);
        return this;
    }

    public DialogObjectInteraction addCheckListView(String key, String table, String id, String label, List<String> labels, List<String> values, boolean edit) {
        if (mapCheckListView == null) {
            mapCheckListView = new HashMap<>();
        }

        CheckListView<String> checkListView = createCheckListView(table + "." + id, key, labels, values);
        addNode(new Label(label + ":::"), checkListView);
        return this;
    }

    public DialogObjectInteraction addTextField(String key, String label) {
        addTextField(key, label, true, "");
        return this;
    }

    public DialogObjectInteraction addTextField(String key, String label, String defaultText) {
        addTextField(key, label, true, defaultText);
        return this;
    }

    public DialogObjectInteraction addTextField(String key, String label, boolean editable, String defaultText) {
        TextField textField = createTextField(key);
        textField.setEditable(editable);
        textField.setText(defaultText);
        Label label1 = new Label(label + ":");
        addNode(label1, textField);
        return this;
    }

    public DialogObjectInteraction addCheckBox(String key, String label, String msg, boolean defaultValue) {
        addCheckBox(key, label, msg, msg, defaultValue);
        return this;
    }

    public DialogObjectInteraction addCheckBox(String key, String label, String msg, String msgTrue, boolean defaultValue) {
        CheckBox checkBox = createCheckBox(key);

        checkBox.setText(msg);
        checkBox.setSelected(defaultValue);
        addNode(new Label(label + ":"), checkBox);
        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkBox.isSelected()) {
                    checkBox.setText(msgTrue);
                } else {
                    checkBox.setText(msg);
                }
            }
        });
        return this;
    }

    public DialogObjectInteraction addChoiceBox(String key, String label, List<String> text, List<String> values) {
        addChoiceBox(key, label, text.get(0), text, values);
        return this;
    }

    public DialogObjectInteraction addChoiceBox(String key, String label, String defaultValue, List<String> text, List<String> values) {//key
        ChoiceBox<String> choiceBox = createChoiceBox(key, values);
        choiceBox.getItems().addAll(text);
        choiceBox.setValue(defaultValue);


        addNode(new Label(label + ":"), choiceBox);
        return this;
    }

    public DialogObjectInteraction addDatePicker(String key, String label) {
        DatePicker datePicker = createDatePicker(key);
        datePicker.setValue(LocalDate.now());
        addNode(new Label(label + ":"), datePicker);
        return this;
    }

    public DialogObjectInteraction addDatePickerBlockedNowTo(String key, String label) {
        DatePicker datePicker = createDatePicker(key);
        datePicker.setValue(LocalDate.now());

        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(LocalDate.now())
                                ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }

                            }
                        };
                    }
                };
        datePicker.setDayCellFactory(dayCellFactory);
        addNode(new Label(label + ":"), datePicker);
        return this;
    }

    public DialogObjectInteraction addDatePicker(String key, String key2, String label, String label2, String message) {
        addDatePicker(key, key2, label, label2, message, new Date(), new Date());
        return this;
    }

    public DialogObjectInteraction addDatePicker(String key, String key2, String label, String label2, String message, Date defaultValue0, Date defaultValue1) {
        //LocalDate ld0 = LocalDate.of( defaultValue0.getYear(),defaultValue0.getMonth()+1,defaultValue0.getDay());
        DatePicker date = createDatePicker(key);
        date.setValue(Instant.ofEpochMilli(defaultValue0.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());

        DatePicker datePicker1 = createDatePicker(key2);
        datePicker1.setValue(Instant.ofEpochMilli(defaultValue1.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                //LocalDate.now()
                                if (item.isBefore(date.getValue())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                } else {
                                    setDisable(false);
                                    setStyle("");
                                }
                                long p = ChronoUnit.DAYS.between(date.getValue(), item);
                                setTooltip(new Tooltip(
                                        message + " " + (p + 1) + " jours")
                                );
                            }
                        };
                    }
                };
        datePicker1.setDayCellFactory(dayCellFactory);
        //  datePicker.setDayCellFactory(dayCellFactory);


        addNode(new Label(label + ":"), date);
        addNode(new Label(label2 + ":"), datePicker1);
        return this;
    }

    public DialogObjectInteraction setTitle(String title) {
        dialog.setTitle(title);
        return this;
    }

    public DialogObjectInteraction setDescription(String description) {
        dialog.setHeaderText(description);
        return this;
    }

    @Deprecated
    public void show() {
        dialog.getDialogPane().setContent(grid);
        dialog.show();
    }

    public void showAndWaitForAdd(String table, boolean firstIsIDNull) {
        dialog.getDialogPane().setContent(grid);
        ButtonType addBtn = new ButtonType("Ajouter");
        ButtonType annuler = new ButtonType("Annuler");
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, annuler);


        boolean vide;
        do {
            Optional optional = dialog.showAndWait();
            Map<String, String> map = getFormData();
            vide = false;
            if (optional.isPresent() && optional.get() == addBtn) {
                for (String s : map.values()) {
                    System.out.println(s);
                    if (s.isEmpty()) {
                        vide = true;
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Champ Vide!");
                        alert.setContentText("Un de vos champs est vide!");
                        alert.showAndWait();
                        break;
                    }
                }
                if (!vide) {
                    try {

                        ResultSet resultSet = ConnectionUtils.select(table);
                        String sql = "";
                        int x;
                        if (firstIsIDNull) {
                            sql = "INSERT INTO `" + table + "` VALUES (" + null + ",";
                            x = 2;
                        } else {
                            sql = "INSERT INTO `" + table + "` VALUES (";
                            x = 1;
                        }
                        Connection conn = ConnectionUtils.getConn();
                        //   Statement statement = conn.createStatement();
                        // System.out.println("key:" + statement.getGeneratedKeys().getInt(1));
                        for (int j = x; j < resultSet.getMetaData().getColumnCount(); j++) {
                            sql = sql.concat("'" + map.get(resultSet.getMetaData().getColumnName(j)) + "',");
                        }
                        sql = sql.concat("'" + map.get(resultSet.getMetaData().getColumnName(resultSet.getMetaData().getColumnCount())) + "')");
                        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        statement.executeUpdate();
                        ResultSet idQuery = statement.getGeneratedKeys();
                        idQuery.next();
                        int idTable = idQuery.getInt(1);
                        if (mapCheckListView != null) {
//                            Set<String> setTabAsso = new TreeSet<>();
//                            for (Map.Entry<Pair<String, String>, Pair<List<String>, CheckListView<String>>> mapAsso : mapCheckListView.entrySet()){
//                                setTabAsso.add(mapAsso.getKey().getKey().split(".")[0].toUpperCase());//table.ID
//                            }
                            for (Map.Entry<Pair<String, String>, Pair<List<String>, CheckListView<String>>> mapAsso : mapCheckListView.entrySet()) {
                                System.out.println(mapAsso.getKey().getKey());
                                String[] tableAndKey = mapAsso.getKey().getKey().split("\\.");
                                System.out.println(tableAndKey.length);
                                String tableAsso = tableAndKey[0];
                                String id = tableAndKey[1];
                                sql = "INSERT INTO " + tableAsso + " (" + id + "," + mapAsso.getKey().getValue() + ") VALUES (" + idTable + ", ? );";
                                PreparedStatement statement1 = conn.prepareStatement(sql);
                                for (Integer i : mapAsso.getValue().getValue().getCheckModel().getCheckedIndices()) {

                                    statement1.setString(1, mapAsso.getValue().getKey().get(i));
                                    statement1.executeUpdate();
                                }


                                //sql = sql.concat(mapAsso.getKey().getValue()+",");
                                //Statement statementAsso = conn.prepareStatement(sql);
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }


            //getFormData().forEach(System.out::println);


        } while (vide);


    }

    public void showAndWaitForEdit(String table, String idTableName, String id, boolean wantID) {
        Connection conn = ConnectionUtils.getConn();
        try {
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM `" + table + "` WHERE `" + table + "`.`" + idTableName + "`= " + id + ";";
            System.out.println(sql);
            ResultSet rs = statement.executeQuery(sql);
            int x = wantID ? 1 : 2;
            while (rs.next()) {
                for (int i = x; i <= rs.getMetaData().getColumnCount(); i++) {

                    grid.add(new Label(rs.getString(i)), 2, wantID ? i : i - 1);

                }
            }


            if (mapCheckListView != null) {


                for (Map.Entry<Pair<String, String>, Pair<List<String>, CheckListView<String>>> mapAsso : mapCheckListView.entrySet()) {
                    String[] tableAndKey = mapAsso.getKey().getKey().split("\\.");
                    String tableAsso = tableAndKey[0];
                    String idTableAsso = tableAndKey[1];
                    PreparedStatement preparedStatement = conn.prepareStatement("SELECT `" + mapAsso.getKey().getValue() + "` FROM `" + tableAsso + "` WHERE " + idTableAsso + " = " + id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {

                        for (int i = 0; i < mapAsso.getValue().getKey().size(); i++) {
                            if (mapAsso.getValue().getKey().get(i).equalsIgnoreCase(String.valueOf(resultSet.getInt(1)))) {
                                mapAsso.getValue().getValue().getCheckModel().check(i);
                            }
                        }
                    }
                    preparedStatement.close();

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        dialog.getDialogPane().setContent(grid);
        ButtonType addBtn = new ButtonType("Modifier");
        ButtonType annuler = new ButtonType("Annuler");
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, annuler);


        Optional optional = dialog.showAndWait();

        if (optional.isPresent() && optional.get() == addBtn) {
            // getFormData().forEach(System.out::println);
            if (mapCheckListView != null) {
                for (Map.Entry<Pair<String, String>, Pair<List<String>, CheckListView<String>>> mapAsso : mapCheckListView.entrySet()) {
                    String[] tableAndKey = mapAsso.getKey().getKey().split("\\.");
                    String tableAsso = tableAndKey[0];
                    String idTableAsso = tableAndKey[1];
                    try {


                        Statement statement = conn.createStatement();
                        statement.executeUpdate("DELETE FROM `" + tableAsso + "` WHERE `" + idTableAsso + "` = " + id);
                        statement.close();
                        System.out.println(mapAsso.getKey().getKey());

                        System.out.println(tableAndKey.length);


                        Statement statement1 = conn.createStatement();
                        for (Integer i : mapAsso.getValue().getValue().getCheckModel().getCheckedIndices()) {
                            String sql = "INSERT INTO `" + tableAsso + "` (`" + idTableAsso + "`,`" + mapAsso.getKey().getValue() + "`) VALUES ('" + id + "', '" + mapAsso.getValue().getKey().get(i) + "');";

                            // System.out.println("SQL: "+"INSERT INTO "+tableAsso+" ("+idTableAsso+","+mapAsso.getKey().getValue()+") VALUES ('"+id+"', '"+mapAsso.getValue().getKey().get(i)+"' );");
                            statement1.executeUpdate(sql);
                        }

//                    System.out.println(sql);
//                    sql = sql.concat(mapAsso.getKey().getValue()+",");
//                    System.out.println(sql);
                        //Statement statementAsso = conn.prepareStatement(sql);


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }


            }
            try {

                Statement statement = conn.createStatement();
                ResultSet resultSet = ConnectionUtils.select(table);
                Map<String, String> map = getFormData();
                String sql = "UPDATE `" + table + "` SET ";

                for (int j = 2; j < resultSet.getMetaData().getColumnCount(); j++) {
                    sql = sql.concat("`" + resultSet.getMetaData().getColumnName(j) + "`='" + map.get(resultSet.getMetaData().getColumnName(j)) + "',");
                }
                sql = sql.concat("`" + resultSet.getMetaData().getColumnName(resultSet.getMetaData().getColumnCount()) + "`='" + map.get(resultSet.getMetaData().getColumnName(resultSet.getMetaData().getColumnCount())) + "' WHERE " + table + "." + resultSet.getMetaData().getColumnName(1) + "=" + id);
                //System.out.println(sql);
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private DialogObjectInteraction addNode(Node label, Node node) {
        int x = positionCounter(grid.getChildren().size());
        grid.add(label, 0, x);
        grid.add(node, 1, x);
        return this;
    }

    private int positionCounter(int i) {
        i = (i / 2) + 1;
        return i;
    }

    private TextField createTextField(String key) {
        //key=key(key);
        TextField textField = new TextField();
        mapTextField.put(key, textField);
        return textField;
    }

    private ChoiceBox<String> createChoiceBox(String key, List<String> values) {
        //  key=key(key);
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        mapChoiceBox.put(key, new Pair<>(values, choiceBox));  //  mapChoiceBox.put(key,choiceBox);
        return choiceBox;
    }

    private DatePicker createDatePicker(String key) {
        // key=key(key);
        DatePicker datePicker = new DatePicker();
        mapDatePicker.put(key, datePicker);
        return datePicker;
    }

    private CheckBox createCheckBox(String key) {
        //  key=key(key);
        CheckBox checkBox = new CheckBox();
        mapCheckBox.put(key, checkBox);
        return checkBox;
    }

    // private String key(String key){
    //     return key.replaceAll("\\s","");
    //  }

    private CheckListView<String> createCheckListView(String table, String key, List<String> list, List<String> values) {
//        ObservableList<String> strings = FXCollections.observableArrayList();
//        strings.addAll(list);
        CheckListView<String> checkListView = new CheckListView<>();
        checkListView.getItems().addAll(list);

        mapCheckListView.put(new Pair<>(table, key), new Pair<>(values, checkListView));
        return checkListView;
    }

    private Map<String, String> getFormData() {
        Map<String, String> stringList = new HashMap<>();
        for (Map.Entry<String, TextField> tf : mapTextField.entrySet()) {
            stringList.put(tf.getKey(), tf.getValue().getText());
        }
//        for (Map.Entry<String, String> s : stringList.entrySet()) {
//            System.out.println(s.getKey() + "   " + s.getValue());
//        }
        for (Map.Entry<String, Pair<List<String>, ChoiceBox<String>>> cb : mapChoiceBox.entrySet()) {
            int i = cb.getValue().getValue().getSelectionModel().getSelectedIndex();
            if (i == -1) {
                stringList.put(cb.getKey(), "-1");
            } else {
                stringList.put(cb.getKey(), cb.getValue().getKey().get(i));
            }
            //String.valueOf(getKeys(DataMapManager.getSportMap(),cb.getValue().getValue()))
        }

//        for (Map.Entry<String, ChoiceBox<String>> cb : mapChoiceBox.entrySet()){
//            stringList.put(cb.getKey(), String.valueOf(getKeys(DataMapManager.getSportMap(),cb.getValue().getValue())));
//        }


        //  stringList.values().forEach(System.out::println);
        for (Map.Entry<String, DatePicker> dp : mapDatePicker.entrySet()) {
            stringList.put(dp.getKey(), dp.getValue().getValue().toString());
        }
        //    stringList.values().forEach(System.out::println);
        for (Map.Entry<String, CheckBox> cb : mapCheckBox.entrySet()) {
            stringList.put(cb.getKey(), cb.getValue().isSelected() ? "1" : "0");
        }
        //    stringList.values().forEach(System.out::println);
        return stringList;
    }


}
