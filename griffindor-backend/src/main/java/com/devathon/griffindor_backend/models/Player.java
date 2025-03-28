package com.devathon.griffindor_backend.models;

import com.devathon.griffindor_backend.enums.PlayerSessionState;

public class Player {

    private final String sessionId;

    private String name;

    private String house;

    private PlayerSessionState sessionState;

    public Player(String sessionId, String name, String house, PlayerSessionState sessionState) {
        this.sessionId = sessionId;
        this.name = name;
        this.house = house;
        this.sessionState = sessionState;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public String getHouse() {
        return house;
    }

    public PlayerSessionState getSessionStatus() {
        return sessionState;
    }

    public boolean isActive() {
        return sessionState == PlayerSessionState.ACTIVE;
    }

    public boolean isWaiting() {
        return sessionState == PlayerSessionState.WAITING;
    }

    public boolean isFigthing() {
        return sessionState == PlayerSessionState.FIGHTING;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setSessionState(PlayerSessionState sessionState) {
        this.sessionState = sessionState;
    }
}
