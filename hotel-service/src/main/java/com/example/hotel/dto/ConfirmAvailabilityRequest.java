package com.example.hotel.dto;

import java.time.LocalDate;

public record ConfirmAvailabilityRequest(String requestId, LocalDate startDate, LocalDate endDate) {
}
