package com.example.booking.dto;

public record RoomDto(Long id, Long hotelId, String number, boolean available, long timesBooked) {
}
