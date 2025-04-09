package com.devathon.griffindor_backend.controllers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.RoomResponseDto;
import com.devathon.griffindor_backend.enums.RoomVisibility;
import com.devathon.griffindor_backend.events.RoomCreatedEvent;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.services.ErrorService;
import com.devathon.griffindor_backend.services.PlayerService;
import com.devathon.griffindor_backend.services.RoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;
  private final ErrorService errorService;
  private final PlayerService playerService;
  private final ApplicationEventPublisher event;

  @MessageMapping(WebSocketRoutes.CREATE_ROOM)
  @SendToUser(WebSocketRoutes.TOPIC_ROOM)
  public RoomResponseDto createRoom(@Payload SimpMessageHeaderAccessor headerAccessor) {
    String sessionId = headerAccessor.getSessionId();

    if (sessionId == null) {
      errorService.sendErrorToSession("unknown", "SESSION_ERROR", "Session ID is null");
      return null;
    }

    if (!playerService.existsBySessionId(sessionId)) {

      errorService.sendErrorToSession(sessionId, "PLAYER_NOT_FOUND", "Session ID not registered");
      return null;
    }

    
    Room newRoom = roomService.createRoomWithName(RoomVisibility.PUBLIC); 
    roomService.joinRoom(newRoom.getRoomId(), sessionId); 
    
    RoomResponseDto response = new RoomResponseDto();
    response.setRoomId(newRoom.getRoomId());
    response.setName(newRoom.getName()); 
    
    event.publishEvent(new RoomCreatedEvent(this, response));

    return response;
  }
  
}
