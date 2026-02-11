package com.example.hotel.config;

import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.Room;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedDataConfig {
    @Bean
    CommandLineRunner seed(HotelRepository hotelRepository, RoomRepository roomRepository) {
        return args -> {
            if (hotelRepository.count() == 0) {
                Hotel h1 = new Hotel(); h1.setName("Grand One"); h1.setAddress("Main st 1"); h1 = hotelRepository.save(h1);
                Hotel h2 = new Hotel(); h2.setName("Sky Hotel"); h2.setAddress("Main st 2"); h2 = hotelRepository.save(h2);
                Room r1 = new Room(); r1.setHotel(h1); r1.setNumber("101"); r1.setAvailable(true); r1.setTimesBooked(0);
                Room r2 = new Room(); r2.setHotel(h1); r2.setNumber("102"); r2.setAvailable(true); r2.setTimesBooked(1);
                Room r3 = new Room(); r3.setHotel(h2); r3.setNumber("201"); r3.setAvailable(true); r3.setTimesBooked(2);
                roomRepository.save(r1); roomRepository.save(r2); roomRepository.save(r3);
            }
        };
    }
}
