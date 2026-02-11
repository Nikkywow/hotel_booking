package com.example.booking.service;

import com.example.booking.dto.RoomDto;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HotelClient {
    private final RestTemplate restTemplate;

    public HotelClient(RestTemplate restTemplate) { this.restTemplate = restTemplate; }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 400, multiplier = 2.0))
    public void confirm(Long roomId, String requestId, LocalDate start, LocalDate end) {
        var body = new java.util.HashMap<String, Object>();
        body.put("requestId", requestId);
        body.put("startDate", start.toString());
        body.put("endDate", end.toString());
        restTemplate.exchange("http://hotel-service/internal/rooms/{id}/confirm-availability", HttpMethod.POST, new HttpEntity<>(body), Void.class, roomId);
    }

    public List<RoomDto> recommend() {
        RoomDto[] rooms = restTemplate.getForObject("http://hotel-service/api/rooms/recommend", RoomDto[].class);
        return rooms == null ? List.of() : Arrays.asList(rooms);
    }

    public void release(Long roomId, String requestId) {
        restTemplate.postForEntity("http://hotel-service/internal/rooms/{id}/release?requestId={requestId}", null, Void.class, roomId, requestId);
    }

    @Recover
    public void recover(Exception ex, Long roomId, String requestId, LocalDate start, LocalDate end) {
        throw new RuntimeException("Hotel service unavailable after retries", ex);
    }
}
