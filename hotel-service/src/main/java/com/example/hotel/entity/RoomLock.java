package com.example.hotel.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "room_locks", uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "request_id"}))
public class RoomLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_id", nullable = false)
    private Long roomId;
    @Column(name = "request_id", nullable = false)
    private String requestId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
