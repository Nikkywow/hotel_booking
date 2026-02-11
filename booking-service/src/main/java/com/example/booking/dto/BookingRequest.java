package com.example.booking.dto;

import java.time.LocalDate;

public record BookingRequest(Long roomId, LocalDate startDate, LocalDate endDate, boolean autoSelect, String requestId) {
}
