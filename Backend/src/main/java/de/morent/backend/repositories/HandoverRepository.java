package de.morent.backend.repositories;

import de.morent.backend.entities.Handover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoverRepository extends JpaRepository<Handover, Long> {
}
