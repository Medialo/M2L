package fr.koala.m2l.controller;

public enum FxmlEnum {

    accueil("../fxml/acc.fxml", null),
    competition("../fxml/affcomp.fxml", "competition"),
    equipe("../fxml/affequipes.fxml", "equipe"),
    users("../fxml/affjou.fxml", "personne"),
    parametre("../fxml/affpara.fxml", null),
    admin(null, "admin"),
    sport(null, "sport");


    private final String str;
    private final String sql;

    FxmlEnum(String str, String sql) {
        this.sql = sql;
        this.str = str;
    }


    public String getFxmlPath() {
        return str;
    }

    public String getSql() {
        return sql;
    }
}
