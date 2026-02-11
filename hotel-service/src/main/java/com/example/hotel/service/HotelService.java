package com.example.hotel.service;

import com.example.hotel.dto.CreateHotelRequest;
import com.example.hotel.dto.HotelDto;
import com.example.hotel.entity.Hotel;
import com.example.hotel.mapper.HotelMapper;
import com.example.hotel.repository.HotelRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    public HotelDto create(CreateHotelRequest req) {
        Hotel hotel = new Hotel();
        hotel.setName(req.name());
        hotel.setAddress(req.address());
        return hotelMapper.toDto(hotelRepository.save(hotel));
    }

    public List<HotelDto> all() {
        return hotelRepository.findAll().stream().map(hotelMapper::toDto).toList();
    }
}
