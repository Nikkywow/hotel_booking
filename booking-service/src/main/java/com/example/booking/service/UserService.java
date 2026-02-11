package com.example.booking.service;

import com.example.booking.dto.AuthRequest;
import com.example.booking.dto.AuthResponse;
import com.example.booking.dto.UserRequest;
import com.example.booking.entity.Role;
import com.example.booking.entity.UserEntity;
import com.example.booking.exception.ApiException;
import com.example.booking.repository.UserRepository;
import com.example.booking.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(UserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) throw new ApiException("Username exists");
        Role role = request.role() == null ? Role.USER : request.role();
        UserEntity u = new UserEntity();
        u.setUsername(request.username());
        u.setPassword(encoder.encode(request.password()));
        u.setRole(role);
        UserEntity saved = userRepository.save(u);
        return new AuthResponse(jwtService.generateToken(saved.getUsername(), saved.getRole()));
    }

    public AuthResponse auth(AuthRequest request) {
        UserEntity user = userRepository.findByUsername(request.username()).orElseThrow(() -> new ApiException("Invalid credentials"));
        if (!encoder.matches(request.password(), user.getPassword())) throw new ApiException("Invalid credentials");
        return new AuthResponse(jwtService.generateToken(user.getUsername(), user.getRole()));
    }

    public UserEntity createByAdmin(UserRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(u -> { throw new ApiException("Username exists"); });
        UserEntity u = new UserEntity();
        u.setUsername(request.username());
        u.setPassword(encoder.encode(request.password()));
        u.setRole(request.role());
        return userRepository.save(u);
    }

    public UserEntity updateByAdmin(UserRequest request) {
        UserEntity user = userRepository.findByUsername(request.username()).orElseThrow(() -> new ApiException("User not found"));
        if (request.password() != null) user.setPassword(encoder.encode(request.password()));
        if (request.role() != null) user.setRole(request.role());
        return userRepository.save(user);
    }

    public void deleteByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new ApiException("User not found"));
        userRepository.delete(user);
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ApiException("User not found"));
    }
}
