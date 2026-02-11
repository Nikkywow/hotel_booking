package com.example.booking.controller;

import com.example.booking.dto.BookingDto;
import com.example.booking.dto.BookingRequest;
import com.example.booking.service.BookingService;
import java.security.Principal;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) { this.bookingService = bookingService; }

    @PostMapping("/booking")
    @PreAuthorize("hasRole('USER')")
    public BookingDto create(@RequestBody BookingRequest request, Principal principal) {
        return bookingService.create(principal.getName(), request);
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('USER')")
    public List<BookingDto> list(Principal principal) { return bookingService.myBookings(principal.getName()); }

    @GetMapping("/booking/{id}")
    @PreAuthorize("hasRole('USER')")
    public BookingDto get(@PathVariable Long id, Principal principal) { return bookingService.getById(principal.getName(), id); }

    @DeleteMapping("/booking/{id}")
    @PreAuthorize("hasRole('USER')")
    public void cancel(@PathVariable Long id, Principal principal) { bookingService.cancel(principal.getName(), id); }
}
