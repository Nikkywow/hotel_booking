package com.example.hotel.controller;

import com.example.hotel.dto.CreateHotelRequest;
import com.example.hotel.dto.HotelDto;
import com.example.hotel.service.HotelService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) { this.hotelService = hotelService; }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HotelDto create(@Valid @RequestBody CreateHotelRequest request) { return hotelService.create(request); }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<HotelDto> all() { return hotelService.all(); }
}
