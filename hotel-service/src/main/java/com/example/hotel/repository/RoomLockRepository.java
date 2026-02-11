package com.example.hotel.repository;

import com.example.hotel.entity.RoomLock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomLockRepository extends JpaRepository<RoomLock, Long> {
    Optional<RoomLock> findByRoomIdAndRequestId(Long roomId, String requestId);

    @Query("select l from RoomLock l where l.roomId = :roomId and l.active = true and l.startDate < :endDate and l.endDate > :startDate")
    List<RoomLock> findConflicts(@Param("roomId") Long roomId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
