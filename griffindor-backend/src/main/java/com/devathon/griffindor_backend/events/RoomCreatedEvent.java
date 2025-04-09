package com.devathon.griffindor_backend.events;


import org.springframework.context.ApplicationEvent;

import com.devathon.griffindor_backend.dtos.RoomResponseDto;

import lombok.Getter;

@Getter
public class RoomCreatedEvent extends ApplicationEvent {
  
    private final RoomResponseDto roomResponse;
    
    public RoomCreatedEvent(Object source, RoomResponseDto roomResponse) {
      super(source); 
      this.roomResponse = roomResponse;
    }

    public RoomResponseDto getRoomResponse() {
        return roomResponse;
    }
}
