package com.example.booking.exception;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String error) {
}
