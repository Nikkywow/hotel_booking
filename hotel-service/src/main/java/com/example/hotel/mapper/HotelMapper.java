package com.example.hotel.mapper;

import com.example.hotel.dto.HotelDto;
import com.example.hotel.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {
    public HotelDto toDto(Hotel hotel) {
        return new HotelDto(hotel.getId(), hotel.getName(), hotel.getAddress());
    }
}
