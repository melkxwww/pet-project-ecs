package me.melkx.authservice.repository;

import me.melkx.authservice.dto.FounderDto;
import me.melkx.authservice.entity.Founder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FounderRepository extends JpaRepository<Founder, Integer> {
    Optional<FounderDto> findByEmail(String email);
}
