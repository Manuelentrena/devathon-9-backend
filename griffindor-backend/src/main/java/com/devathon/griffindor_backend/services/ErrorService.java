package com.devathon.griffindor_backend.services;

public interface ErrorService {
    void sendErrorToSession(String sessionId, String type, String message);
}
