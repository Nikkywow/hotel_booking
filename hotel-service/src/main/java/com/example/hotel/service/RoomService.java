package com.example.hotel.service;

import com.example.hotel.dto.ConfirmAvailabilityRequest;
import com.example.hotel.dto.CreateRoomRequest;
import com.example.hotel.dto.RoomDto;
import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.Room;
import com.example.hotel.entity.RoomLock;
import com.example.hotel.exception.ConflictException;
import com.example.hotel.exception.NotFoundException;
import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.RoomLockRepository;
import com.example.hotel.repository.RoomRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomLockRepository roomLockRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository, RoomLockRepository roomLockRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.roomLockRepository = roomLockRepository;
        this.roomMapper = roomMapper;
    }

    @Transactional
    public RoomDto create(CreateRoomRequest req) {
        Hotel hotel = hotelRepository.findById(req.hotelId()).orElseThrow(() -> new NotFoundException("Hotel not found"));
        Room room = new Room();
        room.setHotel(hotel);
        room.setNumber(req.number());
        room.setAvailable(req.available());
        room.setTimesBooked(0);
        return roomMapper.toDto(roomRepository.save(room));
    }

    public List<RoomDto> allAvailable() { return roomRepository.findByAvailableTrue().stream().map(roomMapper::toDto).toList(); }
    public List<RoomDto> recommend() { return roomRepository.findByAvailableTrueOrderByTimesBookedAscIdAsc().stream().map(roomMapper::toDto).toList(); }

    @Transactional
    public void confirmAvailability(Long roomId, ConfirmAvailabilityRequest request) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException("Room not found"));
        if (!room.isAvailable()) throw new ConflictException("Room unavailable");
        if (roomLockRepository.findByRoomIdAndRequestId(roomId, request.requestId()).isPresent()) return;
        if (!roomLockRepository.findConflicts(roomId, request.startDate(), request.endDate()).isEmpty()) throw new ConflictException("Room already locked for selected dates");
        RoomLock lock = new RoomLock();
        lock.setRoomId(roomId);
        lock.setRequestId(request.requestId());
        lock.setStartDate(request.startDate());
        lock.setEndDate(request.endDate());
        lock.setActive(true);
        roomLockRepository.save(lock);
        room.setTimesBooked(room.getTimesBooked() + 1);
        roomRepository.save(room);
    }

    @Transactional
    public void release(Long roomId, String requestId) {
        roomLockRepository.findByRoomIdAndRequestId(roomId, requestId).ifPresent(lock -> {
            lock.setActive(false);
            roomLockRepository.save(lock);
            log.info("Released lock room={} requestId={}", roomId, requestId);
        });
    }
}
