package com.devathon.griffindor_backend.models;

import com.devathon.griffindor_backend.enums.PlayerSessionState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player {

    private final String sessionId;

    private String name;

    private String house;

    private PlayerSessionState sessionState;

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

}
