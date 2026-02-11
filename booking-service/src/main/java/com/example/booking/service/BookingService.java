package com.example.booking.service;

import com.example.booking.dto.BookingDto;
import com.example.booking.dto.BookingRequest;
import com.example.booking.entity.Booking;
import com.example.booking.entity.BookingStatus;
import com.example.booking.exception.ApiException;
import com.example.booking.repository.BookingRepository;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final HotelClient hotelClient;

    public BookingService(BookingRepository bookingRepository, UserService userService, HotelClient hotelClient) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.hotelClient = hotelClient;
    }

    @Transactional
    public BookingDto create(String username, BookingRequest request) {
        if (request.requestId() == null || request.requestId().isBlank()) throw new ApiException("requestId is required");
        var existing = bookingRepository.findByRequestId(request.requestId());
        if (existing.isPresent()) return toDto(existing.get());

        Long roomId = request.autoSelect() ? hotelClient.recommend().stream().findFirst().orElseThrow(() -> new ApiException("No rooms available")).id() : request.roomId();

        Booking booking = new Booking();
        booking.setUserId(userService.getByUsername(username).getId());
        booking.setRoomId(roomId);
        booking.setStartDate(request.startDate());
        booking.setEndDate(request.endDate());
        booking.setStatus(BookingStatus.PENDING);
        booking.setRequestId(request.requestId());
        booking.setCreatedAt(Instant.now());
        booking = bookingRepository.save(booking);

        try {
            log.info("bookingId={} requestId={} step=PENDING", booking.getId(), booking.getRequestId());
            hotelClient.confirm(roomId, request.requestId(), request.startDate(), request.endDate());
            booking.setStatus(BookingStatus.CONFIRMED);
        } catch (Exception ex) {
            booking.setStatus(BookingStatus.CANCELLED);
            hotelClient.release(roomId, request.requestId());
        }
        return toDto(bookingRepository.save(booking));
    }

    public List<BookingDto> myBookings(String username) {
        Long userId = userService.getByUsername(username).getId();
        return bookingRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    public BookingDto getById(String username, Long id) {
        Long userId = userService.getByUsername(username).getId();
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ApiException("Booking not found"));
        if (!booking.getUserId().equals(userId)) throw new ApiException("Access denied");
        return toDto(booking);
    }

    @Transactional
    public void cancel(String username, Long id) {
        Long userId = userService.getByUsername(username).getId();
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new ApiException("Booking not found"));
        if (!booking.getUserId().equals(userId)) throw new ApiException("Access denied");
        booking.setStatus(BookingStatus.CANCELLED);
        hotelClient.release(booking.getRoomId(), booking.getRequestId());
        bookingRepository.save(booking);
    }

    private BookingDto toDto(Booking b) {
        return new BookingDto(b.getId(), b.getRoomId(), b.getStartDate(), b.getEndDate(), b.getStatus(), b.getRequestId());
    }
}
