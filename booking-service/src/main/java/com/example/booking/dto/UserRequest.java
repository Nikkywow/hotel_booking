package com.example.booking.dto;

import com.example.booking.entity.Role;

public record UserRequest(String username, String password, Role role) {
}
