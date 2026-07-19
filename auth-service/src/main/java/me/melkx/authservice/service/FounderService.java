package me.melkx.authservice.service;

import me.melkx.authservice.dto.FounderDto;
import me.melkx.authservice.exception.FounderNotFoundException;
import me.melkx.authservice.repository.FounderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FounderService {
    private final FounderRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FounderService(FounderRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public FounderDto findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(FounderNotFoundException::new);
    }
}
