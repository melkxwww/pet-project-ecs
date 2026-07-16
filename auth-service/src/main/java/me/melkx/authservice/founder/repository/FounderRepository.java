package me.melkx.authservice.founder.repository;

import me.melkx.authservice.founder.entity.Founder;
import me.melkx.authservice.founder.dto.FounderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FounderRepository extends JpaRepository<Founder, Integer> {
    Optional<FounderDto> findByEmail(String email);
}
