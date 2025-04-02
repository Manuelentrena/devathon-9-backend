package com.devathon.griffindor_backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import com.devathon.griffindor_backend.Queues.RoomReadyQueue;
import com.devathon.griffindor_backend.Queues.WaitlistQueue;
import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.enums.RoomVisibility;
import com.devathon.griffindor_backend.events.RoomReadyEvent;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.services.ErrorService;
import com.devathon.griffindor_backend.services.PlayerService;
import com.devathon.griffindor_backend.services.RoomService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AssignRoomController {

    private final PlayerService playerService;
    private final ErrorService errorService;
    private final RoomService roomService;
    private final WaitlistQueue waitlistQueue;
    private final RoomReadyQueue roomReadyQueue;

    @MessageMapping(WebSocketRoutes.DUEL)
    public void assignRoom(SimpMessageHeaderAccessor headerAccessor) {

        String sessionId = headerAccessor.getSessionId();

        if (sessionId == null) {
            errorService.sendErrorToSession("unknown", "SESSION_ERROR", "Session ID is null");
            return;
        }

        if (!playerService.existsBySessionId(sessionId)) {
            errorService.sendErrorToSession(sessionId, "PLAYER_NOT_FOUND", "Session ID not registered");
            return;
        }

        if (!playerService.hasPlayerInfo(sessionId)) {
            errorService.sendErrorToSession(sessionId, "PLAYER_NOT_REGISTER", "Name or House not registered");
            return;
        }

        // 🔐 Synchronized block to avoid race conditions
        synchronized (waitlistQueue) {

            Room room = waitlistQueue.pollAvailableRoom();

            if (room == null) {
                room = roomService.createRoom(RoomVisibility.PUBLIC);
                roomService.joinRoom(room.getRoomId(), sessionId);
                playerService.updatePlayerSessionState(sessionId, PlayerSessionState.WAITING);
                waitlistQueue.addRoom(room);
                return;
            }

            boolean joined = roomService.joinRoom(room.getRoomId(), sessionId);

            if (!joined) {
                errorService.sendErrorToSession(sessionId, "ROOM_FULL", "La sala ya está llena");
                return;
            }

            if (room.isFull()) {
                waitlistQueue.removeRoom(room);

                for (String playerId : room.getPlayerIds()) {
                    playerService.updatePlayerSessionState(playerId, PlayerSessionState.FIGHTING);
                }

                roomReadyQueue.enqueue(new RoomReadyEvent(room, WebSocketRoutes.QUEUE_DUEL));
            }

        }

    }

}
