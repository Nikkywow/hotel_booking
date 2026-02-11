package com.example.booking.controller;

import com.example.booking.dto.AuthRequest;
import com.example.booking.dto.AuthResponse;
import com.example.booking.dto.UserRequest;
import com.example.booking.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody UserRequest request) { return userService.register(request); }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) { return userService.auth(request); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Object create(@RequestBody UserRequest request) { return userService.createByAdmin(request); }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Object update(@RequestBody UserRequest request) { return userService.updateByAdmin(request); }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@RequestParam String username) { userService.deleteByUsername(username); }
}
