package com.devathon.griffindor_backend.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class RoomResponseDto {
  private UUID roomId;       
  private String name;  

  @Override
  public String toString() {
    return "RoomResponseDto{" +
    "roomId='" + roomId + '\'' +
    ", name='" + name + '\'' +
    '}';
  }
}
