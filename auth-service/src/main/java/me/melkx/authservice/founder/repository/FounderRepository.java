package me.melkx.authservice.founder.repository;

import me.melkx.authservice.founder.entity.Founder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FounderRepository extends JpaRepository<Founder, Integer> {
}
