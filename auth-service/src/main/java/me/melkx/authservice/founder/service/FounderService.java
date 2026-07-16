package me.melkx.authservice.founder.service;

import me.melkx.authservice.founder.dto.FounderCredentialsRequest;
import me.melkx.authservice.founder.dto.FounderDto;
import me.melkx.authservice.founder.exception.FounderNotFoundException;
import me.melkx.authservice.founder.repository.FounderRepository;
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
