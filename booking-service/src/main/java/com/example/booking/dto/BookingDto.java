package com.example.booking.dto;

import com.example.booking.entity.BookingStatus;
import java.time.LocalDate;

public record BookingDto(Long id, Long roomId, LocalDate startDate, LocalDate endDate, BookingStatus status, String requestId) {
}
