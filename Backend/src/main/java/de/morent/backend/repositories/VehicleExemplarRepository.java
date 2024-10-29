package de.morent.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleExemplarRepository extends JpaRepository<VehicleExemplarRepository, Long> {
}
