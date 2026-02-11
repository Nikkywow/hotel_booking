package com.example.hotel.controller;

import com.example.hotel.dto.ConfirmAvailabilityRequest;
import com.example.hotel.dto.CreateRoomRequest;
import com.example.hotel.dto.RoomDto;
import com.example.hotel.service.RoomService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) { this.roomService = roomService; }

    @PostMapping("/api/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomDto create(@Valid @RequestBody CreateRoomRequest request) { return roomService.create(request); }

    @GetMapping("/api/rooms")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<RoomDto> allAvailable() { return roomService.allAvailable(); }

    @GetMapping("/api/rooms/recommend")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<RoomDto> recommend() { return roomService.recommend(); }

    @PostMapping("/internal/rooms/{id}/confirm-availability")
    public ResponseEntity<Void> confirm(@PathVariable Long id, @RequestBody ConfirmAvailabilityRequest request) {
        roomService.confirmAvailability(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/internal/rooms/{id}/release")
    public ResponseEntity<Void> release(@PathVariable Long id, @RequestParam String requestId) {
        roomService.release(id, requestId);
        return ResponseEntity.ok().build();
    }
}
