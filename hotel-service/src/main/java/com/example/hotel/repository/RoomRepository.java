package com.example.hotel.repository;

import com.example.hotel.entity.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailableTrueOrderByTimesBookedAscIdAsc();
    List<Room> findByAvailableTrue();
}
