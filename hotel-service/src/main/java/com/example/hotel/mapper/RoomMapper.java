package com.example.hotel.mapper;

import com.example.hotel.dto.RoomDto;
import com.example.hotel.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public RoomDto toDto(Room room) {
        return new RoomDto(room.getId(), room.getHotel().getId(), room.getNumber(), room.isAvailable(), room.getTimesBooked());
    }
}
