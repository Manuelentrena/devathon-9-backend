package com.devathon.griffindor_backend.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.devathon.griffindor_backend.events.RoomCreatedEvent;

@Component
public class RoomCreatedListener implements ApplicationListener<RoomCreatedEvent> {
  private final SimpMessagingTemplate messagingemplate;
  public RoomCreatedListener(SimpMessagingTemplate messagingemplate) {
    this.messagingemplate = messagingemplate;
  }

  @Override
  public void onApplicationEvent(RoomCreatedEvent event) {
    String destination = "/topic/room"; //TODO add constant for this
    messagingemplate.convertAndSend(destination, event.getRoomResponse());

  }
}
