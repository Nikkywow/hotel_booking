package com.example.booking.config;

import com.example.booking.entity.Role;
import com.example.booking.entity.UserEntity;
import com.example.booking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedDataConfig {
    private final PasswordEncoder encoder;

    public SeedDataConfig(PasswordEncoder encoder) { this.encoder = encoder; }

    @Bean
    CommandLineRunner seedUsers(UserRepository repository) {
        return args -> {
            repository.findByUsername("admin").orElseGet(() -> {
                UserEntity u = new UserEntity();
                u.setUsername("admin"); u.setPassword(encoder.encode("admin123")); u.setRole(Role.ADMIN);
                return repository.save(u);
            });
            repository.findByUsername("user").orElseGet(() -> {
                UserEntity u = new UserEntity();
                u.setUsername("user"); u.setPassword(encoder.encode("user123")); u.setRole(Role.USER);
                return repository.save(u);
            });
        };
    }
}
