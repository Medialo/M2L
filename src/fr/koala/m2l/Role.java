package fr.koala.m2l;

public enum Role {

    administrateur("Administrateur", true, true, true, true),
    utilisateur("Utilisateur", false, false, false, false),
    organisateur("Organisateur", true, true, true, false),
    ;

    String nomRole;
    boolean competition;
    boolean users;
    boolean team;
    boolean parameters;

    Role(String nomRole, boolean competition, boolean users, boolean team, boolean parameter) {
        this.nomRole = nomRole;
        this.competition = competition;
        this.users = users;
        this.team = team;
        this.parameters = parameter;
    }

    public boolean isCompetition() {
        return competition;
    }

    public boolean isUsers() {
        return users;
    }

    public boolean isTeam() {
        return team;
    }

    public boolean isParameters() {
        return parameters;
    }
}
