package fr.koala.m2l;

import fr.koala.m2l.controller.FxmlEnum;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataMapManager {


    private static final Map<Integer, Personne> personneMap = new HashMap<>();
    private static final Map<Integer, Competition> competitionMap = new HashMap<>();
    private static final Map<Integer, Personne> candidatMap = new HashMap<>(); //jsp komment faire
    private static final Map<Integer, Equipe> equipeMap = new HashMap<>();
    private static final Map<Integer, Sport> sportMap = new HashMap<>();
    private static final Map<Integer, Admin> adminMap = new HashMap<>();
    private static final List<String> list = new ArrayList<>();
    private static final List<String> listID = new ArrayList<>();

    private DataMapManager() throws AssertionError {
        throw new AssertionError();
    }

    public static Map<Integer, Personne> getPersonneMap() {
        return personneMap;
    }

    public static Map<Integer, Equipe> getEquipeMap() {
        return equipeMap;
    }

    public static Map<Integer, Competition> getCompetitionMap() {
        return competitionMap;
    }

    public static Map<Integer, Sport> getSportMap() {
        return sportMap;
    }

    public static Map<Integer, Admin> getAdminMap() {
        return adminMap;
    }

    public static void refreshAll() {
        refreshCompetition();
        refreshSport();
        refreshTeam();
        refreshUsers();

    }

    public static Pair<List<String>, List<String>> getSportListsDataPair() {
        refreshSport();
        return new Pair<>(listID, list);
    }

    public static void refreshSport() {
        list.clear();
        listID.clear();
        DataMapManager.getSportMap().clear();
        ResultSet resultSet = ConnectionUtils.select(FxmlEnum.sport);
        try {
            while (resultSet.next()) {
                DataMapManager.getSportMap().put(resultSet.getInt(1), new Sport(resultSet.getInt(1), new Text(resultSet.getString(2))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Sport sport : DataMapManager.getSportMap().values()) {

            list.add(sport.getNom().getText());
            listID.add(String.valueOf(sport.getId()));

        }
    }

    public static void refreshUsers() {
        DataMapManager.getPersonneMap().clear();
        ResultSet resultSet = ConnectionUtils.select(FxmlEnum.users);
        try {
            while (resultSet.next()) {
                DataMapManager.getPersonneMap().put(resultSet.getInt(1), new Personne(resultSet.getInt(1), new Text(resultSet.getString(2)), new Text(resultSet.getString(3)), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void refreshTeam() {
        DataMapManager.getEquipeMap().clear();
        ResultSet resultSet = ConnectionUtils.select(FxmlEnum.equipe);
        try {
            while (resultSet.next()) {
                DataMapManager.getEquipeMap().put(resultSet.getInt(1), new Equipe(resultSet.getInt(1), new Text(resultSet.getString(2))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void refreshCompetition() {
        DataMapManager.getCompetitionMap().clear();
        ResultSet resultSet = ConnectionUtils.select(FxmlEnum.competition);
        try {
            while (resultSet.next()) {
                DataMapManager.getCompetitionMap().put(resultSet.getInt(1), new Competition(resultSet.getInt(1), new Text(resultSet.getString(2)), resultSet.getDate(3), resultSet.getDate(4), resultSet.getDate(5), resultSet.getInt(7), resultSet.getBoolean(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void refreshAdmin() {
        DataMapManager.getAdminMap().clear();
        ResultSet resultSet = ConnectionUtils.select(FxmlEnum.admin);
        try {
            while (resultSet.next()) {
                DataMapManager.getAdminMap().put(resultSet.getInt(1), new Admin(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
