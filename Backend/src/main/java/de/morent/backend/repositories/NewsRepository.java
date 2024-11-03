package de.morent.backend.repositories;

import de.morent.backend.entities.Newsletter;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<Newsletter, Long> {
}
