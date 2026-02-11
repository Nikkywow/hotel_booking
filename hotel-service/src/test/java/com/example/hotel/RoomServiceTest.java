package com.example.hotel;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.hotel.dto.ConfirmAvailabilityRequest;
import com.example.hotel.dto.CreateHotelRequest;
import com.example.hotel.dto.CreateRoomRequest;
import com.example.hotel.exception.ConflictException;
import com.example.hotel.service.HotelService;
import com.example.hotel.service.RoomService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoomServiceTest {

    @Autowired RoomService roomService;
    @Autowired HotelService hotelService;

    @Test
    void shouldRejectOverlappingLock() {
        var hotel = hotelService.create(new CreateHotelRequest("H", "A"));
        var room = roomService.create(new CreateRoomRequest(hotel.id(), "101", true));
        roomService.confirmAvailability(room.id(), new ConfirmAvailabilityRequest("r1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3)));
        assertThrows(ConflictException.class, () -> roomService.confirmAvailability(room.id(),
                new ConfirmAvailabilityRequest("r2", LocalDate.now().plusDays(2), LocalDate.now().plusDays(4))));
    }
}
