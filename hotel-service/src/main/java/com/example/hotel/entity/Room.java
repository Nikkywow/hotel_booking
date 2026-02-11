package com.example.hotel.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    private String number;
    private boolean available;
    @Column(name = "times_booked")
    private long timesBooked;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public long getTimesBooked() { return timesBooked; }
    public void setTimesBooked(long timesBooked) { this.timesBooked = timesBooked; }
}
