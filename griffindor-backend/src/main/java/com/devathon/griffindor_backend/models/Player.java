package com.devathon.griffindor_backend.models;

import com.devathon.griffindor_backend.enums.PlayerSessionState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player {

    private String sessionId;

    private String name;

    private String house;

    private PlayerSessionState sessionState;

    private String tokenId;

    public PlayerSessionState getSessionStatus() {
        return sessionState;
    }

    public boolean isConnect() {
        return sessionState == PlayerSessionState.CONNECT;
    }

    public boolean isDisconnect() {
        return sessionState == PlayerSessionState.DISCONNECT;
    }

    public boolean isWaiting() {
        return sessionState == PlayerSessionState.WAITING;
    }

    public boolean isFigthing() {
        return sessionState == PlayerSessionState.FIGHTING;
    }

}
