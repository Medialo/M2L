package fr.koala.m2l;

import fr.koala.m2l.controller.FxmlEnum;

import java.sql.*;
import java.util.prefs.Preferences;


public final class ConnectionUtils {

    private static final Preferences preferences = Main.getUserPreferences();
    //    private static String host = "localhost";
//    private static String dbName = "m2l";
//    private static String dbUserName = "root";
//    private static String dbPassword = "";
    private static String host = preferences.get("dbhost", "localhost");
    private static String dbName = preferences.get("dbname", "");
    private static String dbUserName = preferences.get("dbusername", "");
    private static String dbPassword = preferences.get("dbpw", "");
    private static Connection conn;

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        ConnectionUtils.host = host;
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        ConnectionUtils.dbName = dbName;
    }

    public static String getDbUserName() {
        return dbUserName;
    }

    public static void setDbUserName(String dbUserName) {
        ConnectionUtils.dbUserName = dbUserName;
    }

    public static String getDbPassword() {
        return "******";
    }

    public static void setDbPassword(String dbPassword) {
        ConnectionUtils.dbPassword = dbPassword;
    }

    public static Connection getConn() {
        return conn;
    }

    public static void CreateConnection() throws SQLException, ClassNotFoundException {
        conn = getDBConnextion();
    }

    private static Connection getDBConnextion() throws SQLException, ClassNotFoundException {
        return getDBConnextion(host, dbName, dbUserName, dbPassword);
    }

    private static Connection getDBConnextion(String host, String dbName, String dbUserName, String dbPassword) throws SQLException, ClassNotFoundException {
        // Class.forName("com.mysql.jdbc.Driver");
        System.out.println("--------------------");

        System.out.println("host:" + host + " dbname:" + dbName + " dbusername:" + dbUserName + " dbpassword:" + dbPassword);

        String coURL = "jdbc:mysql://" + host + ":3306/" + dbName + "?userSSl=false&serverTimezone=UTC";

//        Statement statement = DriverManager.getConnection(coURL, dbUserName, dbPassword).createStatement();
//        String sql = "Use m2l";
//        statement.executeQuery(sql);
        //System.out.println(i);


        return DriverManager.getConnection(coURL, dbUserName, dbPassword);
    }


    public static ResultSet select(FxmlEnum fxmlEnum) {
        return select(fxmlEnum.getSql());

    }

    public static ResultSet select(String table) {
        try {
            Statement statement = conn.createStatement();
            String sql = "Select * from " + table;
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

}
/*

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colIsbn.setResizable(false);
		colIsbn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
		colAuteur.setCellValueFactory(new PropertyValueFactory<>("Auteur"));
		colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
		colDispo.setCellValueFactory(new PropertyValueFactory<>("dispo"));
		for (Livre l : Accueil.getAl()) {
			table.getItems().add(l);
		}

	}

 */