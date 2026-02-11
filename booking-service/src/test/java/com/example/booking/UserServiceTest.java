package com.example.booking;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.booking.dto.UserRequest;
import com.example.booking.entity.Role;
import com.example.booking.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired UserService userService;

    @Test
    void registerShouldReturnJwt() {
        var response = userService.register(new UserRequest("u1", "p1", Role.USER));
        assertNotNull(response.token());
    }
}
