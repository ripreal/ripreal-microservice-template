package ru.sportmaster.esm.user.service;

public class AuthInfo {

    private final String id;
    private final String name;
    private final long bonuses;

    public AuthInfo(String id, String name, long bonuses) {
        this.id = id;
        this.name = name;
        this.bonuses = bonuses;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getBonuses() {
        return bonuses;
    }
}
