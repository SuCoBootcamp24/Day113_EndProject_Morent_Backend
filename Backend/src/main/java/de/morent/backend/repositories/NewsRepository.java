package de.morent.backend.repositories;

import de.morent.backend.entities.Newsletter;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NewsRepository extends CrudRepository<Newsletter, Long> {
    Optional<Newsletter> findByEmail(String email);
}
