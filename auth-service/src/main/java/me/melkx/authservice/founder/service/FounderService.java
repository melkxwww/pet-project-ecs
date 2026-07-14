package me.melkx.authservice.founder.service;

import me.melkx.authservice.founder.repository.FounderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FounderService {
    private final FounderRepository repository;

    @Autowired
    public FounderService(FounderRepository repository) {
        this.repository = repository;
    }

    public
}
